package com.example.giner.gymgo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables

        private static final String TAG = "LoginActivity";
        private FirebaseAuth autentificacion;
        private FirebaseAuth.AuthStateListener autentificadorListener;
        private EditText emailRegistro;
        private EditText passRegistro;
        private Button botonRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        autentificacion = FirebaseAuth.getInstance();

        //Configuramos que responda a los cambios en el estado de inicio de sesion.

            autentificadorListener = new FirebaseAuth.AuthStateListener() {

                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    FirebaseUser usuario = firebaseAuth.getCurrentUser();

                    if(usuario!=null){
                        //El usuario ha iniciado sesion
                        //Log para informar del inicio de sesion
                        Log.d(TAG, "El metodo onAuthStateChanged ha sido llamado, resultado: signed in. UID: "+usuario.getUid());
                    }
                    else{
                        //El usuario ha cerrado sesion
                        //Log para informar del cierre de sesion
                        Log.d(TAG, "El metodo onAuthStateChanged ha sido llamado, resultado: signer out. UID: ");
                    }

                }
            };

        // Instancio los widgets

            emailRegistro = (EditText)findViewById(R.id.emailRegistro);
            passRegistro = (EditText)findViewById(R.id.passwordRegistro);
            botonRegistro = (Button)findViewById(R.id.buttonRegistrar);
            botonRegistro.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        autentificacion.addAuthStateListener(autentificadorListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(autentificadorListener!=null){
            autentificacion.removeAuthStateListener(autentificadorListener);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==botonRegistro.getId()){

            autentificacion.createUserWithEmailAndPassword(emailRegistro.getText().toString(),passRegistro.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Usuario registrado");
                        finish();
                    }
                    else{
                        Log.d(TAG, "Fallo en el registro");
                        Toast.makeText(RegistroActivity.this,"Error en el registro",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}

