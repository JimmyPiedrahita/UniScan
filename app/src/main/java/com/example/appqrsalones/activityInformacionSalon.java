package com.example.appqrsalones;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.appqrsalones.modelo.Horario;
import com.example.appqrsalones.modelo.controllerAdapterDelete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;
public class activityInformacionSalon extends AppCompatActivity {
    private TextView numero, sede;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    final List<Horario> listaHorarios = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button btn_eliminar,btn_descargar;
    private controllerAdapterDelete controlAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_salon);
        numero = findViewById(R.id.inf_txt_numero_salon);
        sede = findViewById(R.id.inf_txt_sede_salon);
        recyclerView = findViewById(R.id.recyclerViewHorarioDelete);
        btn_eliminar = findViewById(R.id.btn_eliminar_salon);
        btn_descargar = findViewById(R.id.btn_descargar);
        Bundle bundle = getIntent().getExtras();
        Window window = getWindow();

// Cambia el color de la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }

        if (bundle != null){
            numero.setText(bundle.getString("numero"));
            sede.setText(bundle.getString("sede"));
            descargarQr(bundle.getString("codigo"));
            consultarHorarios(bundle.getString("numero"));
        }
        btn_eliminar.setOnClickListener(v -> {
            if(bundle != null){
                eliminarSalon(bundle.getString("numero"));
            }
        });
        btn_descargar.setOnClickListener(v -> {
            if(bundle != null){
                descargarQr(bundle.getString("numero"));
            }
        });

    }


    public void descargarQr(String url){
        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagenRef = storageRef.child("CodigosQr/"+url);
        imagenRef.getDownloadUrl().addOnSuccessListener(uri -> {
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Codigo_qr_salon_"+url+".jpg");
            downloadManager.enqueue(request);
            Toast.makeText(getApplicationContext(), "Descarga iniciada", Toast.LENGTH_SHORT).show();
        });
    }
    public void eliminarSalon(String numero_salon){
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("salones").document(numero_salon).delete().addOnSuccessListener(unused -> {
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference().child("CodigosQr/"+numero_salon);
            storageRef.delete().addOnSuccessListener(unused1 -> {
                firestore.collection("horarios").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null){
                            for (DocumentSnapshot document : querySnapshot.getDocuments()){
                                if(document.getString("salon_numero").equals(numero_salon)){
                                    document.getReference().delete();
                                }
                            }
                            Toast.makeText(this, "Salon eliminado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }).addOnFailureListener(Throwable::printStackTrace);
            Intent intent = new Intent(activityInformacionSalon.this, activityListaSalones.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al eliminar salon", Toast.LENGTH_SHORT).show();
        });
    }

    public void consultarHorarios(String numero_salon){
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("horarios").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null){
                    for (DocumentSnapshot document : querySnapshot.getDocuments()){
                        if(document.get("salon_numero").equals(numero_salon)){
                            Horario horario = new Horario(
                                    document.getString("horario_dia"),
                                    document.getString("horario_hora_final"),
                                    document.getString("horario_hora_inicial"),
                                    document.getString("horario_id"),
                                    document.getString("horario_materia"),
                                    document.getString("salon_numero")
                            );
                            mostrarListaHorarios(horario);
                        }
                    }
                }
            }
        });
    }
    public void mostrarListaHorarios(Horario horario){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activityInformacionSalon.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        listaHorarios.add(horario);
        controlAdapter = new controllerAdapterDelete(activityInformacionSalon.this,listaHorarios);
        recyclerView.setAdapter(controlAdapter);

    }
}