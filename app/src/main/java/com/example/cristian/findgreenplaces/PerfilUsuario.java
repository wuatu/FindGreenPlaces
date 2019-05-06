package com.example.cristian.findgreenplaces;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import Clases.IdUsuario;

public class PerfilUsuario extends AppCompatActivity {
    ImageView imageViewFotoPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        imageViewFotoPerfil=findViewById(R.id.imageViewFotoPerfil);
        if(IdUsuario.getUrl().equals("")) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.com_facebook_profile_picture_blank_square)
                    .fitCenter()
                    .into(imageViewFotoPerfil);
        }else{
            Glide.with(getApplicationContext())
                    .load(IdUsuario.getUrl())
                    .fitCenter()
                    .into(imageViewFotoPerfil);

        }
        TextView nombre=findViewById(R.id.textViewNombre);
        nombre.setText(IdUsuario.getNombreUsuario()+" "+IdUsuario.getApellidoUsuario());
        TextView correo=findViewById(R.id.textViewCorreo);
        correo.setText(IdUsuario.getCorreo());
        TextView contribuciones=findViewById(R.id.textViewNContribucion);
    }
}
