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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;

import Clases.Adapter.AdapterListViewContribucionAtractivoTuristico;
import Clases.Models.AtractivoTuristico;
import Clases.Models.Comentario;
import Clases.Utils.GetImagenes;
import Clases.Utils.IdUsuario;
import Clases.Models.Imagen;
import Clases.Models.MeGustaImagen;
import Clases.Utils.Referencias;
import Clases.Models.Usuario;
import Interfaz.OnGetDataListenerAtractivoTuristico;
import Interfaz.OnGetDataListenerImagenes;

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
    ArrayList<Imagen> imagenes;
    private static final int IMAGENES = 1;
    ListView lista;
    Adapter adapter;
    ArrayList<AtractivoTuristico> atractivoTuristicos;

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
        imagenes=new ArrayList();
        atractivoTuristicos=new ArrayList();

        //barra circular
        progressBar=findViewById(R.id.progressBar1);
        showProgress(true);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        mDatabase.child(Referencias.USUARIOFOTODEPERFIL).
                child(IdUsuario.getIdUsuario()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Imagen imagen= dataSnapshot1.getValue(Imagen.class);
                    imagenes.add(imagen);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        imageViewFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(Referencias.FOTOMEGUSTA).child(IdUsuario.getIdUsuario()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<MeGustaImagen> meGustaImagens=new ArrayList<>();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            if (dataSnapshot1.exists()) {
                                Log.v("llegue",dataSnapshot1.getKey());
                                MeGustaImagen meGustaImagen = dataSnapshot1.getValue(MeGustaImagen.class);
                                meGustaImagens.add(meGustaImagen);

                            }
                        }
                        Intent intent = new Intent(PerfilUsuario.this, VisualizacionDeImagen.class);
                        intent.putExtra("imagenes", imagenes);
                        intent.putExtra("meGustaImagens", meGustaImagens);
                        intent.putExtra("position", 0);
                        startActivityForResult(intent,IMAGENES);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
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
                /*
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
                }*/
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

        query(new OnGetDataListenerAtractivoTuristico() {
            @Override
            public void onSuccess(AtractivoTuristico atractivoTuristico) {
                startVisualizarAtractivoTuristico(atractivoTuristico);
            }
            @Override
            public void onStart() {
            }
            @Override
            public void onFailure() {
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

    private void query(final OnGetDataListenerAtractivoTuristico listener) {
        listener.onStart();
        mDatabase.child(Referencias.CONTRIBUCIONES).child(IdUsuario.getIdUsuario()).child(Referencias.ATRACTIVOTURISTICO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //atractivoTuristicos.removeAll(atractivoTuristicos);
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    AtractivoTuristico atractivoTuristico2 = dataSnapshot1.getValue(AtractivoTuristico.class);
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico2.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AtractivoTuristico atractivoTuristico = dataSnapshot.getValue(AtractivoTuristico.class);
                            if (atractivoTuristico != null) {
                                if (atractivoTuristico.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                                    atractivoTuristicos.add(atractivoTuristico);
                                }
                                adapter = new AdapterListViewContribucionAtractivoTuristico(PerfilUsuario.this, atractivoTuristicos);
                                lista = (ListView) findViewById(R.id.listViewVisualizarAtractivoTuristico);
                                lista.setAdapter((ListAdapter) adapter);
                                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        try {
                                            AtractivoTuristico atractivoTuristico = (AtractivoTuristico) adapter.getItem(position);
                                            listener.onSuccess(atractivoTuristico);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                //Log.v("atractivoo",String.valueOf(atractivoTuristicos.size()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public void startVisualizarAtractivoTuristico(AtractivoTuristico atractivoTuristico){
        Log.v("atractivoooo",atractivoTuristico.getId());
        GetImagenes getImagenes=new GetImagenes();
        getImagenes.getImagenesAtractivoTuristico(atractivoTuristico, new OnGetDataListenerImagenes() {
            @Override
            public void onSuccess(ArrayList<Imagen> imagenes,AtractivoTuristico atractivoTuristico1) {
                Log.v("atractivoooo",String.valueOf(imagenes.size()));
                Intent intent=new Intent(getApplicationContext(),VisualizarAtractivoTuristico.class);
                intent.putExtra("atractivoTuristico", atractivoTuristico1);
                intent.putExtra("imagenes",imagenes);
                startActivity(intent);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
