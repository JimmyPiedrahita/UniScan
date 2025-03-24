package com.example.appqrsalones;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.example.appqrsalones.modelo.Salon;
import com.example.appqrsalones.modelo.controllerAdapter;
import com.example.appqrsalones.modelo.Horario;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
public class activityAgregarSalon extends AppCompatActivity {
    FirebaseFirestore firestore;
    controllerAdapter controlAdapter;
    RecyclerView recyclerView;
    private StorageReference storageReference;
    final List<Horario> listaHorarios = new ArrayList<>();
    private EditText txt_numero, txt_sede;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_salon);
        txt_numero = findViewById(R.id.txt_input_numero);
        txt_sede = findViewById(R.id.txt_input_sede);
        recyclerView = findViewById(R.id.recyclerViewHorario);
        storageReference = FirebaseStorage.getInstance().getReference();
        Window window = getWindow();

// Cambia el color de la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }


    //AGREGACION DE HORARIOS PARA CADA SALON =======================================================
    public void agregarHorario(View view){


        LayoutInflater inflater = getLayoutInflater();
        View vista = inflater.inflate(R.layout.dialog_agregar_horarios, (ViewGroup) findViewById(com.google.android.material.R.id.layout));
        EditText txt_materia = vista.findViewById(R.id.dialog_txt_nombre_materia);
        EditText txt_hora_ini = vista.findViewById(R.id.dialog_txt_hora_inicial);
        EditText txt_hora_fin = vista.findViewById(R.id.dialog_txt_hora_final);
        EditText txt_dia = vista.findViewById(R.id.dialog_txt_dia);
        AlertDialog.Builder ventanaEmergente = new AlertDialog.Builder(activityAgregarSalon.this, R.style.DialogTheme);




        ventanaEmergente.setView(vista).setPositiveButton("Guardar", (dialog, which) -> {

            if(
                    txt_dia.getText().toString().trim().isEmpty() ||
                            txt_materia.getText().toString().trim().isEmpty() ||
                            txt_hora_ini.getText().toString().trim().isEmpty() ||
                            txt_hora_fin.getText().toString().trim().isEmpty()){
                Toast.makeText(activityAgregarSalon.this, "Faltan campos del horario", Toast.LENGTH_LONG).show();
            }else {
                Horario horario = new Horario(
                        txt_dia.getText().toString().trim(),
                        txt_hora_fin.getText().toString().trim(),
                        txt_hora_ini.getText().toString().trim(),
                        generadorHorarioId(),
                        txt_materia.getText().toString().trim(),
                        "000"
                );
                mostrarListaHorarios(horario);
                dialog.dismiss();

            }
        }).setNegativeButton("Cancelar", (dialog, which) -> {

            dialog.dismiss();

        });
        ventanaEmergente.show();

    }



    protected String generadorHorarioId(){
        Random random = new Random();
        int horario_id_generado;
        do{
            horario_id_generado = random.nextInt(1001)+1000;
        }while(validarHorarioIdGenerado(horario_id_generado));
        return String.valueOf(horario_id_generado);
    }
    public boolean validarHorarioIdGenerado(int codigo_generado) {
        String horario_id_generado = String.valueOf(codigo_generado);
        firestore = FirebaseFirestore.getInstance();
        final AtomicBoolean valido = new AtomicBoolean(false);
        firestore.collection("horarios").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String horario_id = document.getId();
                        if (!horario_id.equals(horario_id_generado)) {
                            valido.set(true);
                        }
                    }
                }
            }
        });
        return valido.get();
    }
    public void mostrarListaHorarios(Horario horario){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activityAgregarSalon.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        listaHorarios.add(horario);
        controlAdapter = new controllerAdapter(activityAgregarSalon.this,listaHorarios);
        recyclerView.setAdapter(controlAdapter);
    }
    //GENERACION DE CODIGO QR Y GUARDAR SALON EN LA BASE DE DATOS =================================================
    public void guardarInformacionSalon(View view){
        if(!txt_numero.getText().toString().trim().isEmpty() && !txt_sede.getText().toString().isEmpty()){
            validarNumeroSalonGenerado(txt_numero.getText().toString().trim(),txt_sede.getText().toString().trim());
        }else{
            Toast.makeText(this, "Ingrese todos los campos del salon", Toast.LENGTH_SHORT).show();
        }
    }
    public void validarNumeroSalonGenerado(String numero, String sede) {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("salones").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    boolean valido = true;
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String salon_numero = document.getId();
                        if(salon_numero.equals(numero)){
                            valido = false;
                            Toast.makeText(this, "El salon ya existe", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if(valido = true){
                        generarSalonConQr(numero, sede);
                    }
                }
            }
        });
    }
    public void generarSalonConQr(String txt_numero_salon, String sede){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(txt_numero_salon, BarcodeFormat.QR_CODE, 750,750);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference imageReference = storageReference.child("CodigosQr/" + txt_numero_salon);
            UploadTask uploadTask = imageReference.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Salon salon = new Salon(txt_numero_salon, sede, uri.toString());
                    guardarSalon(salon);
                });
            }).addOnFailureListener(e -> {
                e.printStackTrace();
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void guardarSalon(Salon salon_generado){
        firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("salones").document(salon_generado.getSalon_numero());
        Map<String, Object> salon = new HashMap<>();
        salon.put("salon_numero", salon_generado.getSalon_numero());
        salon.put("salon_sede", salon_generado.getSalon_sede());
        salon.put("salon_codigo_qr", salon_generado.getCodigo_qr());
        documentReference.set(salon).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Agregado Correctamente", Toast.LENGTH_SHORT).show();
            guardarHorarios(salon_generado.getSalon_numero());
            Intent intent = new Intent(activityAgregarSalon.this, activityListaSalones.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> Toast.makeText(this, "Error al cargar salon", Toast.LENGTH_SHORT).show());
    }
    public void guardarHorarios(String numero){
        firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference;
        for (Horario h : listaHorarios){
            documentReference = firestore.collection("horarios").document(h.getHorario_id());
            Map<String, Object> horario = new HashMap<>();
            horario.put("horario_dia", h.getHorario_dia());
            horario.put("horario_hora_final", h.getHorario_hora_final());
            horario.put("horario_hora_inicial", h.getHorario_hora_inicial());
            horario.put("horario_id", h.getHorario_id());
            horario.put("horario_materia", h.getHorario_materia());
            horario.put("salon_numero", numero);
            documentReference.set(horario).addOnSuccessListener(aVoid ->
                    Log.e("Horario agregado", "horarios"))
                    .addOnFailureListener(e ->
                            Log.e("Error", "horarios"));
        }

    }

}