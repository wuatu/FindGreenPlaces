package com.example.cristian.findgreenplaces;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import Clases.Models.AtractivoTuristico;
import Clases.Models.Imagen;

public class DialogoVisualizarAtractivoTuristico extends AppCompatActivity implements Serializable {

    private TextView titulo;
    private TextView descripcion;
    private ImageView foto;
    private Button botonMas;
    LinearLayout linearLayout;
    AtractivoTuristico atractivoTuristico;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    ArrayList<Imagen> imagenes;
    private RatingBar ratingBar;
    private TextView textViewratingBar;
    private TextView textViewOpiniones;
    private LinearLayout linearLayoutProgressBar;
    private LinearLayout linearLayoutContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogo_visualizar_atractivo_turistico);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        imagenes=new ArrayList();
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));


        linearLayout=findViewById(R.id.contenedorDialogoAT);
        //getSupportActionBar().hide();


        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int ancho=dm.widthPixels;
        int alto=dm.heightPixels;
        getWindow().setLayout((int) (ancho*.99),(int)(alto*.30));
        getWindow().setGravity(Gravity.BOTTOM);

        linearLayoutProgressBar=findViewById(R.id.LinearLayoutProgressBar);
        linearLayoutContenido=linearLayout=findViewById(R.id.contenedorDialogoAT);

        titulo = (TextView) findViewById(R.id.textViewTituloAT);
        titulo.setText(atractivoTuristico.getNombre());
        descripcion=findViewById(R.id.textViewDescripcion);
        descripcion.setText(atractivoTuristico.getDescripcion());
        foto=findViewById(R.id.imageViewMiniFotoAtractivoTuristico);
        textViewOpiniones=findViewById(R.id.textViewOpinionesn);
        ratingBar=findViewById(R.id.rating);
        textViewratingBar=findViewById(R.id.textViewRatingBar);

        textViewratingBar.setText(atractivoTuristico.getCalificacion());
        ratingBar.setRating(Float.parseFloat(atractivoTuristico.getCalificacion()));
        textViewOpiniones.setText(String.valueOf(atractivoTuristico.getContadorOpiniones()));
        ratingBar.setEnabled(false);


        Glide.with(DialogoVisualizarAtractivoTuristico.this)
                .load(atractivoTuristico.getUrlFoto())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if(isFromMemoryCache){
                            linearLayoutProgressBar.setVisibility(View.GONE);
                            linearLayoutContenido.setVisibility(View.VISIBLE);
                        }
                        else{
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    linearLayoutProgressBar.setVisibility(View.GONE);
                                    linearLayoutContenido.setVisibility(View.VISIBLE);
                                }
                            }, 500);
                        }
                        return false;
                    }
                })
                .centerCrop()
                .into(foto);

        linearLayout();
    }


    private void linearLayout(){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DialogoVisualizarAtractivoTuristico.this, VisualizarAtractivoTuristico.class);
                intent.putExtra("imagenes",imagenes);
                //Log.v("ooooh",String.valueOf(imagenes.size()));
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);
                finish();
            }
        });
    }


}
