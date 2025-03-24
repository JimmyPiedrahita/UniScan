package com.example.appqrsalones;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.example.appqrsalones.modelo.Horario;
import com.example.appqrsalones.modelo.controllerAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.ActivityInfo;
public class activityScanner extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private RecyclerView recyclerViewHome;
    private controllerAdapter controlAdapter;
    private final List<Horario> listaHorariosConsultados = new ArrayList<>();
    private TextView txt_numero,txt_sede;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        txt_numero = findViewById(R.id.txt_numero);
        txt_sede = findViewById(R.id.txt_sede);
        recyclerViewHome = findViewById(R.id.recyclerViewHome);
        firestore = FirebaseFirestore.getInstance();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        IntentIntegrator intentIntegrator = new IntentIntegrator(activityScanner.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
        Window window = getWindow();

// Cambia el color de la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null){
            if(intentResult.getContents() == null){
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activityScanner.this, MainActivity.class);
                startActivity(intent);
            }else{
                firestore.collection("salones")
                        .document(intentResult.getContents())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        firestore.collection("horarios").get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null) {
                                    txt_numero.setText(intentResult.getContents());
                                    txt_sede.setText(documentSnapshot.getString("salon_sede"));
                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                        if(document.get("salon_numero").equals(intentResult.getContents())){
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
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "No se encontro el salon", Toast.LENGTH_SHORT).show();
                });
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void mostrarListaHorarios(Horario horario){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activityScanner.this, 1);
        recyclerViewHome.setLayoutManager(gridLayoutManager);
        listaHorariosConsultados.add(horario);
        controlAdapter = new controllerAdapter(activityScanner.this,listaHorariosConsultados);
        recyclerViewHome.setAdapter(controlAdapter);
    }


}