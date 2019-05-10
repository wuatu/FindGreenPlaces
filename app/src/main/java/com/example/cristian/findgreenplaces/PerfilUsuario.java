package com.example.cristian.findgreenplaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

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
                    .transform(new CircleTransform(PerfilUsuario.this)).into(imageViewFotoPerfil);
        }else{
            Glide.with(getApplicationContext())
                    .load(IdUsuario.getUrl())
                    .transform(new CircleTransform(PerfilUsuario.this)).into(imageViewFotoPerfil);

        }
        TextView nombre=findViewById(R.id.textViewNombre);
        nombre.setText(IdUsuario.getNombreUsuario()+" "+IdUsuario.getApellidoUsuario());
        TextView correo=findViewById(R.id.textViewCorreo);
        correo.setText(IdUsuario.getCorreo());
        TextView contribuciones=findViewById(R.id.textViewNContribucion);
    }
    public static class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {


            int size = 1000;


            Bitmap result = pool.get(size, size, Bitmap.Config.ALPHA_8);

            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }
    }
}
