package com.example.cristian.findgreenplaces;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Clases.Models.AtractivoTuristico;
import Clases.Models.CalificacionPromedio;
import Clases.Models.CalificacionUsuarioAtractivoTuristico;
import Clases.Models.Comentario;
import Clases.Utils.IdUsuario;
import Clases.Models.Imagen;
import Clases.Utils.Referencias;
import Clases.Utils.SubirPuntos;

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
        if(imagenes.size()>0) {
            Glide.with(SetCalificacionAtractivoTuristico.this)
                    .load(imagenes.get(0).getUrl())
                    .fitCenter()
                    .centerCrop()
                    .into(imageView);
        }

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

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Calificar");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titulo = (TextView) findViewById(R.id.textViewTituloAT2);
        textViewratingBar=findViewById(R.id.textViewRatingBar);
        titulo = (TextView) findViewById(R.id.textViewTituloAT2);
        titulo.setText(atractivoTuristico.getNombre());
        imageView=findViewById(R.id.imageViewVAT);

        textViewOpiniones=findViewById(R.id.textViewOpinionesn);
        editTextComentar=findViewById(R.id.editTextComentar);
        editTextComentar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.editTextComentar) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        //textViewTituloComentar=findViewById(R.id.textViewTituloComentarios);
        buttonEnviarCalificacion=findViewById(R.id.buttonEnviarCalificacion);
        getImagenesAtractivoTuristico();
        ratingBar=findViewById(R.id.rating);
        mDatabase.child(Referencias.CALIFICACIONPROMEDIO).child(atractivoTuristico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CalificacionPromedio calificacionPromedio=dataSnapshot.getValue(CalificacionPromedio.class);
                if(calificacionPromedio!=null){
                    String calificacion=String.valueOf(calificacionPromedio.getPromedioCalificacion());
                    //textViewratingBar.setText(calificacion.substring(0,3));
                    //ratingBar.setRating(Float.parseFloat(calificacion));
                    textViewratingBar.setText("5.0");
                    ratingBar.setRating(Float.parseFloat("5.0"));
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
                if(calificacionUsuario<=0){
                    Toast.makeText(SetCalificacionAtractivoTuristico.this,"Debe Calificar",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editTextComentar.getText().toString().equals("")){
                    Toast.makeText(SetCalificacionAtractivoTuristico.this,"Debe Comentar",Toast.LENGTH_SHORT).show();
                    return;
                }
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

                        Toast.makeText(SetCalificacionAtractivoTuristico.this,"Calificación enviada exitosamente!",Toast.LENGTH_SHORT).show();
                        SubirPuntos.aumentaPuntosOtrosUsuarios(SetCalificacionAtractivoTuristico.this,IdUsuario.getIdUsuario(),1);
                        //Toast.makeText(SetCalificacionAtractivoTuristico.this,"Subes "+1+" punto",Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK,
                                new Intent().putExtra("nombre", atractivoTuristico.getNombre()).
                                        putExtra("opiniones",textViewOpiniones.getText().toString()).
                                        putExtra("rating",textViewratingBar.getText().toString()));
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Query q2=mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).child(atractivoTuristico.getId()).push();
                String keyComentario=((DatabaseReference) q2).getKey();
                Comentario comentario=new Comentario(keyComentario,editTextComentar.getText().toString(),IdUsuario.getIdUsuario(),IdUsuario.getNombreUsuario(),IdUsuario.getApellidoUsuario(),"0","0",Referencias.VISIBLE);
                ((DatabaseReference) q2).setValue(comentario);
                editTextComentar.setText("");

            }
        });

        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = ratingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    if(stars>5){
                        stars=5;
                    }
                    ratingBar.setRating(stars);
                    v.setPressed(false);
                    textViewratingBar.setText(String.valueOf(stars)+".0");
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }
        });

    }
    private void calculaNuevaCalificacionPromedio(int getTotlaPersonas, double getSumaCalificaciones, float calificacionUsuario) {
        double nuevaSumaCalificaciones=getSumaCalificaciones+calificacionUsuario;
        int nuevoTotalPersonas=getTotlaPersonas+1;
        double nuevoPromedio=nuevaSumaCalificaciones/nuevoTotalPersonas;
        System.out.println("nuevo promedio: "+nuevoPromedio);
        Log.v("nuevo promedio: ", String.valueOf(nuevoPromedio));
        atractivoTuristico.setCalificacion(String.valueOf(nuevoPromedio).substring(0,3));
        atractivoTuristico.setContadorOpiniones(String.valueOf(nuevoTotalPersonas));
        mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
        CalificacionPromedio calificacionPromedio=new CalificacionPromedio(nuevoPromedio,nuevoTotalPersonas,nuevaSumaCalificaciones);
        mDatabase.child(Referencias.CALIFICACIONPROMEDIO).child(atractivoTuristico.getId()).setValue(calificacionPromedio);
        Log.v("rating",String.valueOf(ratingBar.getRating()));
        textViewratingBar.setText(String.valueOf(nuevoPromedio).substring(0,3));
        textViewOpiniones.setText(String.valueOf(nuevoTotalPersonas));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

}
