package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.CalificacionPromedio;
import Clases.CalificacionUsuarioAtractivoTuristico;
import Clases.Comentario;
import Clases.IdUsuario;
import Clases.Imagen;
import Clases.Referencias;

public class SetCalificacionAtractivoTuristico extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    private TextView textViewratingBar;
    ArrayList<Imagen> imagenes;
    AtractivoTuristico atractivoTuristico;
    private ImageView imageView;
    private TextView textViewOpiniones;
    private RatingBar ratingBar;
    private TextView titulo;
    Button buttonEnviarCalificacion;
    EditText editTextComentar;
    TextView textViewTituloComentar;
    private void getImagenesAtractivoTuristico(){
        Glide.with(SetCalificacionAtractivoTuristico.this)
                .load(imagenes.get(0).getUrl())
                .fitCenter()
                .centerCrop()
                .into(imageView);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));
        imagenes=new ArrayList();
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));
        setContentView(R.layout.activity_set_calificacion_atractivo_turistico);
        titulo = (TextView) findViewById(R.id.textViewTituloAT2);
        textViewratingBar=findViewById(R.id.textViewRatingBar);
        titulo = (TextView) findViewById(R.id.textViewTituloAT2);
        titulo.setText(atractivoTuristico.getNombre());
        imageView=findViewById(R.id.imageViewVAT);
        textViewratingBar=findViewById(R.id.textViewRatingBar);
        textViewOpiniones=findViewById(R.id.textViewOpinionesn);
        editTextComentar=findViewById(R.id.editTextComentar);
        textViewTituloComentar=findViewById(R.id.textViewTituloComentarios);
        buttonEnviarCalificacion=findViewById(R.id.buttonEnviarCalificacion);
        getImagenesAtractivoTuristico();
        ratingBar=findViewById(R.id.rating);
        mDatabase.child(Referencias.CALIFICACIONPROMEDIO).child(atractivoTuristico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        });
        buttonEnviarCalificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final float calificacionUsuario = ratingBar.getRating();
                //crea registro de calificacion de un atractivo turistico por usuario
                CalificacionUsuarioAtractivoTuristico calificacionUsuarioAtractivoTuristico=new CalificacionUsuarioAtractivoTuristico(calificacionUsuario);
                Query q=mDatabase.child(Referencias.CALIFICACIONUSUARIOATRACTIVOTURISTICO).child(IdUsuario.getIdUsuario()).child(atractivoTuristico.getId()).push();
                ((DatabaseReference) q).setValue(calificacionUsuarioAtractivoTuristico);
                //agrego la calificacion al atractivo turistico
                mDatabase.child(Referencias.CALIFICACIONPROMEDIO).child(atractivoTuristico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        CalificacionPromedio calificacionPromedio=dataSnapshot.getValue(CalificacionPromedio.class);
                        //double getCalificacionPromedio=calificacionPromedio.getPromedioCalificacion();
                        int getTotlaPersonas;
                        double getSumaCalificaciones;
                        if(calificacionPromedio==null){
                            getTotlaPersonas=0;
                            getSumaCalificaciones=0;
                        }
                        else{
                            getTotlaPersonas=calificacionPromedio.getTotalPersonas();
                            getSumaCalificaciones=calificacionPromedio.getSumaDeCalificaciones();
                        }
                        calculaNuevaCalificacionPromedio(getTotlaPersonas,getSumaCalificaciones,calificacionUsuario);
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(SetCalificacionAtractivoTuristico.this, android.R.style.Theme_Material_Dialog);
                        } else {
                            builder = new AlertDialog.Builder(SetCalificacionAtractivoTuristico.this);
                        }
                        builder.setTitle("Calificacion Enviada")
                                .setMessage("Calificacion Enviada con Exito!")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        /*Intent intent = new Intent(SetCalificacionAtractivoTuristico.this, VisualizarAtractivoTuristicoFragment.class);
                                        intent.putExtra("imagenes",imagenes);
                                        Log.v("ooooh",String.valueOf(imagenes.size()));
                                        intent.putExtra("atractivoTuristico", atractivoTuristico);
                                        startActivity(intent);
                                        finish();*/
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Query q2=mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).child(atractivoTuristico.getId()).push();
                String keyComentario=((DatabaseReference) q2).getKey();
                Comentario comentario=new Comentario(editTextComentar.getText().toString(),IdUsuario.getIdUsuario(),IdUsuario.getNombreUsuario(),IdUsuario.getApellidoUsuario());
                ((DatabaseReference) q2).setValue(comentario);

            }
        });

    }
    private void calculaNuevaCalificacionPromedio(int getTotlaPersonas, double getSumaCalificaciones, float calificacionUsuario) {
        double nuevaSumaCalificaciones=getSumaCalificaciones+calificacionUsuario;
        int nuevoTotalPersonas=getTotlaPersonas+1;
        double nuevoPromedio=nuevaSumaCalificaciones/nuevoTotalPersonas;
        CalificacionPromedio calificacionPromedio=new CalificacionPromedio(nuevoPromedio,nuevoTotalPersonas,nuevaSumaCalificaciones);
        mDatabase.child(Referencias.CALIFICACIONPROMEDIO).child(atractivoTuristico.getId()).setValue(calificacionPromedio);
        Log.v("rating",String.valueOf(ratingBar.getRating()));
        textViewratingBar.setText(String.valueOf(nuevoPromedio).substring(0,3));
        textViewOpiniones.setText(String.valueOf(nuevoTotalPersonas));
    }
}
