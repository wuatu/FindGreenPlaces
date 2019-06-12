package com.example.cristian.findgreenplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.CalificacionPromedio;
import Clases.Imagen;
import Clases.Referencias;

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

        titulo = (TextView) findViewById(R.id.textViewTituloAT);
        titulo.setText(atractivoTuristico.getNombre());
        descripcion=findViewById(R.id.textViewDescripcion);
        descripcion.setText(atractivoTuristico.getDescripcion());
        foto=findViewById(R.id.imageViewMiniFotoAtractivoTuristico);
        textViewOpiniones=findViewById(R.id.textViewOpinionesn);
        ratingBar=findViewById(R.id.rating);
        textViewratingBar=findViewById(R.id.textViewRatingBar);
        /*mDatabase.child(Referencias.CALIFICACIONPROMEDIO).child(atractivoTuristico.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CalificacionPromedio calificacionPromedio=dataSnapshot.getValue(CalificacionPromedio.class);
                if(calificacionPromedio!=null){
                    String calificacion=String.valueOf(calificacionPromedio.getPromedioCalificacion());
                    textViewratingBar.setText(calificacion.substring(0,3));
                    ratingBar.setRating(Float.parseFloat(calificacion));
                    textViewOpiniones.setText(String.valueOf(calificacionPromedio.getTotalPersonas()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        textViewratingBar.setText(atractivoTuristico.getCalificacion());
        ratingBar.setRating(Float.parseFloat(atractivoTuristico.getCalificacion()));
        textViewOpiniones.setText(String.valueOf(atractivoTuristico.getContadorOpiniones()));
        ratingBar.setEnabled(false);
        Glide.with(getApplicationContext())
                .load(atractivoTuristico.getUrlFoto())
                .centerCrop()
                .into(foto);
        linearLayout();
    }


    private void linearLayout(){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImagenesAtractivoTuristico();
            }
        });
    }
    private void getImagenesAtractivoTuristico(){

        Query q=mDatabase.child(Referencias.IMAGENES).child(atractivoTuristico.getId());
        Log.v("oooh", q.getRef().toString());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Imagen imagen=dataSnapshot1.getValue(Imagen.class);
                    imagenes.add(imagen);
                }
                finish();
                Intent intent = new Intent(DialogoVisualizarAtractivoTuristico.this, VisualizarAtractivoTuristico.class);
                intent.putExtra("imagenes",imagenes);
                Log.v("ooooh",String.valueOf(imagenes.size()));
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);

                /*if(imagenes.size()>0) {

                    Glide.with(getApplicationContext())
                            .load(imagenes.get(0).getUrl())

                            .centerCrop()
                            .into(foto);
                }
                    linearLayout();*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    /**/
    }

}
