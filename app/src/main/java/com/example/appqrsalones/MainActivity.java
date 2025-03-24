package com.example.appqrsalones;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    private EditText txt_email_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_email_login = findViewById(R.id.txt_email_login);
        Window window = getWindow();

// Cambia el color de la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }

    }
    //Validacion de correo institucional
    public void ingresarLogin(View view){
        String dominio = "unimayor.edu.co";
        if(!isValidEmailFromDomain(txt_email_login.getText().toString().trim(),dominio)){
            Toast.makeText(this, "Correo no valido", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(MainActivity.this, activityScanner.class);
            startActivity(intent);
        }
    }
    public final static boolean isValidEmailFromDomain(CharSequence target, String domain) {
        if (target == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            return false;
        } else {
            return target.toString().endsWith(domain);
        }
    }
    //Ingresar a login administrador
    public void ingresarAdministrador(View view){
        Intent intent = new Intent(MainActivity.this, activityLoginAdministrador.class);
        startActivity(intent);
    }


}