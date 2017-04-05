package com.example.giner.gymgo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class AutentificarDialog extends DialogFragment implements View.OnClickListener {

    //Widgets

        private EditText email;
        private EditText pass;
        private ImageButton botonVolver;
        private ImageButton botonAutentificar;

    //Dialogo

        private AlertDialog dialogo;
        private OnAutentificar escuchador;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Autentificación");
        View dialogAutentificacion = getActivity().getLayoutInflater().inflate(R.layout.dialog_autentificacion,null);

        //Instancio los objetos

            email = (EditText)dialogAutentificacion.findViewById(R.id.emailAutentificacion);
            pass = (EditText)dialogAutentificacion.findViewById(R.id.passwordAutentificacion);

            botonVolver = (ImageButton)dialogAutentificacion.findViewById(R.id.volver);
            botonAutentificar = (ImageButton)dialogAutentificacion.findViewById(R.id.autentificar);

        //Escuchadores

            botonAutentificar.setOnClickListener(this);
            botonVolver.setOnClickListener(this);

        //Seteo el layout en el dialogo

            builder.setView(dialogAutentificacion);

        //Hacemos el dialogo modal

            dialogo=builder.create();
            dialogo.setCanceledOnTouchOutside(false);

        //Metodo para reiniciar los widgets

            reiniciarWidgets();

        //Devolvemos al metodo el alertdialog configurado

            return dialogo;

    }

    //Metodo para reiniciar widgets

        public void reiniciarWidgets(){

            email.setText("");
            pass.setText("");

        }

    @Override
    public void onClick(View v) {

        if(v.getId()==botonAutentificar.getId()){

            //Proteger la aplicacion de errores

                escuchador.autentificarUsuario(email.getText().toString(),pass.getText().toString());
                dismiss();

        }

        else if (v.getId()==botonVolver.getId()){
            dismiss();
        }

    }

    public interface OnAutentificar{

        //Metodo para pasar el email y la contraseña a la actividad

            void autentificarUsuario(String email, String pass);

    }

    public void setOnAutentificarListener(OnAutentificar escuchador){
        this.escuchador=escuchador;
    }

}
