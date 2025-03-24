package com.example.appqrsalones;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.appqrsalones.modelo.Horario;
import com.example.appqrsalones.modelo.Salon;
import com.example.appqrsalones.modelo.controllerAdapter;
import com.example.appqrsalones.modelo.controllerAdapterSalon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class activityListaSalones extends AppCompatActivity {
    private RecyclerView recyclerView;
    final List<Salon> listaSalones = new ArrayList<>();
    private controllerAdapterSalon controllerAdapterSalon;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_salones);
        recyclerView = findViewById(R.id.recyclerViewSalones);
        consultarSalones();

        Window window = getWindow();

// Cambia el color de la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }

    }
    public void consultarSalones(){
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("salones").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null){
                    for (DocumentSnapshot documentSnapshot : querySnapshot){
                        Salon salon = new Salon(
                                documentSnapshot.getString("salon_numero"),
                                documentSnapshot.getString("salon_sede"),
                                documentSnapshot.getString("salon_codigo_qr")
                        );
                        mostrarListaSalones(salon);
                    }
                }
            }
        });
    }
    public void mostrarListaSalones(Salon salon){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activityListaSalones.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        listaSalones.add(salon);
        controllerAdapterSalon = new controllerAdapterSalon(activityListaSalones.this,listaSalones);
        recyclerView.setAdapter(controllerAdapterSalon);
    }
    public void informacionSalon(View view){
        Intent intent = new Intent(activityListaSalones.this, activityInformacionSalon.class);
        startActivity(intent);
    }
    public void agregarSalon(View view){
        Intent intent = new Intent(activityListaSalones.this, activityAgregarSalon.class);
        startActivity(intent);
    }
}