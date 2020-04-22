package com.example.cristian.findgreenplaces;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Clases.IdUsuario;
import Clases.Referencias;
import Clases.Usuario;

public class PerfilUsuario extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    ImageView imageViewFotoPerfil;
    View progressBar;
    ProgressBar progressBar2;
    LinearLayout linearLayoutPerfil;
    Button buttonEditarPerfil;
    LinearLayout linearLayoutMedallas;
    LinearLayout linearLayoutBronce;
    LinearLayout linearLayoutPlata;
    LinearLayout linearLayoutOro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Perfil");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonEditarPerfil=findViewById(R.id.buttonEditarPerfil);
        linearLayoutPerfil=findViewById(R.id.linearLayoutPerfil);

        linearLayoutMedallas=findViewById(R.id.LayoutMedallas);
        linearLayoutBronce=findViewById(R.id.LayoutBronce);
        linearLayoutPlata=findViewById(R.id.LayoutPlata);
        linearLayoutOro=findViewById(R.id.LayoutOro);
        //barra circular
        progressBar=findViewById(R.id.progressBar1);
        showProgress(true);

        progressBar2=findViewById(R.id.seekBar);

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
        imageViewFotoPerfil=findViewById(R.id.imageViewFotoPerfil1);
        if(IdUsuario.getUrl().equals("")) {
            Glide.with(PerfilUsuario.this)
                    .load(R.drawable.com_facebook_profile_picture_blank_square)
                    .fitCenter()
                    .transform(new CircleTransform(PerfilUsuario.this))
                    .into(imageViewFotoPerfil);
        }else{
            Glide.with(PerfilUsuario.this)
                    .load(IdUsuario.getUrl())
                    .transform(new CircleTransform(PerfilUsuario.this))
                    .fitCenter()
                    .into(imageViewFotoPerfil);
            Log.v("aburri",IdUsuario.getUrl());

        }

        mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario=dataSnapshot.getValue(Usuario.class);

                TextView nombre=findViewById(R.id.textViewNombre);
                nombre.setText(IdUsuario.getNombreUsuario()+" "+IdUsuario.getApellidoUsuario());
                TextView correo=findViewById(R.id.textViewCorreo);
                correo.setText(IdUsuario.getCorreo());
                TextView contribuciones=findViewById(R.id.textViewNContribuciones);
                contribuciones.setText(usuario.getContribuciones());

                TextView nivel=findViewById(R.id.textViewNivel);
                nivel.setText(usuario.getNivel());
                TextView puntos=findViewById(R.id.textViewPuntos);
                puntos.setText(usuario.getPuntos());
                TextView nombreNivel=findViewById(R.id.textViewNombreNivel);
                nombreNivel.setText(usuario.getNombreNivel());
                progressBar2.setProgress(Integer.valueOf(usuario.getPuntos()));
                progressBar2.setEnabled(false);
                showProgress(false);
                LinearLayout linearLayoutProgressBar=findViewById(R.id.linearLayoutProgressBar);
                linearLayoutProgressBar.setVisibility(View.GONE);
                linearLayoutPerfil.setVisibility(View.VISIBLE);
                if(nivel.getText().toString().equalsIgnoreCase("1")){
                    linearLayoutBronce.setVisibility(View.VISIBLE);
                    linearLayoutMedallas.setVisibility(View.VISIBLE);
                }
                if(nivel.getText().toString().equalsIgnoreCase("2")){
                    linearLayoutBronce.setVisibility(View.VISIBLE);
                    linearLayoutPlata.setVisibility(View.VISIBLE);
                    linearLayoutMedallas.setVisibility(View.VISIBLE);
                }
                if(Integer.valueOf(nivel.getText().toString())>=3){
                    linearLayoutPlata.setVisibility(View.VISIBLE);
                    linearLayoutBronce.setVisibility(View.VISIBLE);
                    linearLayoutOro.setVisibility(View.VISIBLE);
                    linearLayoutMedallas.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(PerfilUsuario.this,EditarPerfil.class);

                startActivity(intent);
            }
        });
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            //mLoginFormView=findViewById(R.id.progress_bar);

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
