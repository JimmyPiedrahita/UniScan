package com.example.appqrsalones;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
public class activityLoginAdministrador extends AppCompatActivity {
    FirebaseFirestore firestore;
    private EditText txt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_administrador);
        txt_password = findViewById(R.id.txt_password_admin);
        Window window = getWindow();

// Cambia el color de la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }
    public void ingresarAdministrador(View view){
        if (!txt_password.getText().toString().isEmpty()){
            firestore = FirebaseFirestore.getInstance();
            firestore.collection("claves").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null){
                        Boolean valido = false;
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()){
                            if (documentSnapshot.getString("clave").equals(txt_password.getText().toString().trim())){
                                valido = true;
                            }
                        }
                        if (valido){
                            Intent intent = new Intent(activityLoginAdministrador.this, activityListaSalones.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(this, "Clave incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }else {
            Toast.makeText(this, "Ingrese la clave", Toast.LENGTH_SHORT).show();
        }
    }
}