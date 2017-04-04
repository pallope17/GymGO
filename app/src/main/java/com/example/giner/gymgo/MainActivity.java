package com.example.giner.gymgo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Widgets

        private TextView user;
        private TextView email;
        private static final String TAG = "MainActivity";
        private FirebaseUser userLogueado;
        private FirebaseAuth autentificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autentificacion = FirebaseAuth.getInstance();

        //Recupero el usuario logueado

            userLogueado = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //Intancio los widgets

        user = (TextView)findViewById(R.id.nombreUser);
        email = (TextView)findViewById(R.id.emailUser);

        //Seteo los datos del usuario

        Log.d(TAG, userLogueado.getEmail());
        user.setText(userLogueado.getUid().toString());
        email.setText(userLogueado.getEmail().toString());

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.datos) {

        }
        else if (id == R.id.cambiarCorreo) {

            Toast.makeText(this, "Cambiar Correo", Toast.LENGTH_SHORT).show();

        }
        else if (id == R.id.cambiarPass) {
            cambiarPassword();
        }
        else if (id == R.id.eliminarUser) {
            eliminarUsuario();
        }
        else if (id == R.id.cerrarSesion) {
            cerrarSesion();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cambiarPassword(){

        userLogueado = FirebaseAuth.getInstance().getCurrentUser();

        autentificacion.sendPasswordResetEmail(userLogueado.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Log.d(TAG, "Email para cambiar la contraseña enviado");
                    Toast.makeText(MainActivity.this, "El email ha sido enviado al correo. Comprueba el correo y cambia la contraseña", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Log.d(TAG, "Fallo en el envio del email");
                    Toast.makeText(MainActivity.this, "Ha ocurrido un fallo en el envio del email", Toast.LENGTH_SHORT).show();
                }

            }
        });

        finish();

    }

    public void eliminarUsuario(){

        //Falta volver a autentificar al usuario antes de eliminarlo.

       //Metodo para eliminar el usuario

        userLogueado = FirebaseAuth.getInstance().getCurrentUser();

        userLogueado.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"Usuario eliminado.");
                    Toast.makeText(MainActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT);
                }
                else{
                    Log.d(TAG,"Fallo en la eliminacion del usuario");
                    Toast.makeText(MainActivity.this, "Ha ocurrido un error al eliminar el usuario", Toast.LENGTH_SHORT);
                }
            }
        });

        finish();

    }

    public void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
        finish();
    }

}
