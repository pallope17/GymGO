package com.example.giner.gymgo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.Manifest.permission.READ_CONTACTS;


public class RecuperarPassActivity extends AppCompatActivity implements View.OnClickListener {


    //Variables

        private static final String TAG = "LoginActivity";
        private FirebaseAuth autentificacion;
        private FirebaseAuth.AuthStateListener autentificadorListener;

        private EditText mEmailView;
        private View mProgressView;
        private View mLoginFormView;
        private Button botonRecuperarPass;
        private Button botonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_pass);

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

        //Instancio los widgets

            mEmailView = (EditText)findViewById(R.id.emailRecuperarPass);
            botonRecuperarPass = (Button)findViewById(R.id.recuperarPassButton);
            botonVolver = (Button)findViewById(R.id.volver);
            botonRecuperarPass.setOnClickListener(this);
            botonVolver.setOnClickListener(this);
            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);

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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            botonRecuperarPass.setVisibility(show ? View.GONE : View.VISIBLE);
            botonVolver.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            botonRecuperarPass.setVisibility(show ? View.GONE : View.VISIBLE);
            botonVolver.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==botonRecuperarPass.getId()){
            if(mEmailView.getText().toString().isEmpty()||!mEmailView.getText().toString().contains("@")||!mEmailView.getText().toString().contains(".")){
                //Condicionales del campo email
                if(mEmailView.getText().toString().isEmpty()){
                    mEmailView.setError("El campo del email esta vacio");
                }
                else if(!mEmailView.getText().toString().contains("@")||!mEmailView.getText().toString().contains(".")){
                    mEmailView.setError("El email introducido no es válido");
                }
                else{
                    mEmailView.setError(null);
                }
            }
            else{
                showProgress(true);
                autentificacion = FirebaseAuth.getInstance();
                autentificacion.sendPasswordResetEmail(mEmailView.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showProgress(false);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email de recuperacion de contraseña ha sido enviado");
                            Toasty.info(RecuperarPassActivity.this, "El email ha sido enviado al correo. Comprueba el correo y cambia la contraseña", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.d(TAG, "Email no registrado");
                            Toasty.error(RecuperarPassActivity.this,task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            onRestart();
                        }
                    }
                });
            }
        }

        else if(v.getId()==botonVolver.getId()){
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mEmailView.setText("");
    }

    public void onBackPressed(){
    }

}

