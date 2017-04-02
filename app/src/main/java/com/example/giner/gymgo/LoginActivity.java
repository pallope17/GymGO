package com.example.giner.gymgo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    //Variables

        private static final String TAG = "LoginActivity";
        private FirebaseAuth autentificacion;
        private FirebaseAuth.AuthStateListener autentificadorListener;

    // UI references.
    private EditText email;
    private EditText pass;
    private Button botonLogin;
    private TextView registrarse;
    private TextView olvidoPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

            email = (EditText)findViewById(R.id.email);
            pass = (EditText)findViewById(R.id.password);
            botonLogin = (Button)findViewById(R.id.email_sign_in_button);
            botonLogin.setOnClickListener(this);
            registrarse = (TextView)findViewById(R.id.registrase);
            olvidoPass = (TextView)findViewById(R.id.recuperarPass);
            registrarse.setOnClickListener(this);
            olvidoPass.setOnClickListener(this);

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

        if (v.getId() == botonLogin.getId()) {

            autentificacion.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Al completarse muestra un log para informar de que ha sido logueado correctamente, si falla el login mostrar'a un log y toast al usuario
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Login correcto.");
                        OnLoginCorrecto();
                    } else {
                        Log.d(TAG, "Login fallido");
                        Toast.makeText(LoginActivity.this, "El inicio de sesion ha fallado", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        else if(v.getId() == registrarse.getId()){
            Intent intencionRegistrar = new Intent(this,registroActivity.class);
            startActivity(intencionRegistrar);
        }

        else if(v.getId() == olvidoPass.getId()){
            Toast.makeText(LoginActivity.this, "Recuperar pass", Toast.LENGTH_SHORT).show();

        }
    }

    public void OnLoginCorrecto(){
        Intent intencionInicioSesion = new Intent(this,MainActivity.class);
        startActivity(intencionInicioSesion);
    }

}

