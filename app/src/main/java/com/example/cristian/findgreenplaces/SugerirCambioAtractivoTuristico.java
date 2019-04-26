package com.example.cristian.findgreenplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.Categoria;
import Clases.IdUsuario;
import Clases.Imagen;
import Clases.Referencias;
import Fragment.VisualizarAtractivoTuristicoFragment;

public class SugerirCambioAtractivoTuristico extends AppCompatActivity {
    TextView textViewButtonModificarCalificacion;
    TextView textViewButtonModificarDescripcion;
    TextView textViewButtonModificarCategorias;
    TextView textViewButtonModificarTips;
    TextView textViewButtonModificarHorario;
    TextView textViewButtonModificarTelefono;
    TextView textViewButtonModificarPaginaWeb;
    TextView textViewButtonModificarRedesSociales;
    ArrayList<Imagen> imagenes;
    AtractivoTuristico atractivoTuristico;
    private TextView titulo;
    private TextView textViewDescripcionAT;
    private TextView textViewSugerirCambio;
    private LinearLayout linearLayoutCategorias;
    private RatingBar ratingBar;
    private ImageView imageView;
    private TextView textViewratingBar;
    private TextView textViewOpiniones;
    LinearLayout linearLayoutcategorias;
    TagView tagGroup;
    DatabaseReference mDatabase;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerir_cambio_atractivo_turistico);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));
        titulo = (TextView) findViewById(R.id.textViewTituloAT2);
        titulo.setText(atractivoTuristico.getNombre());
        textViewDescripcionAT =findViewById(R.id.textViewDescripcionAT);
        //textViewDescripcionAT.setText(atractivoTuristico.getDescripcion());
        imageView=findViewById(R.id.imageViewVAT);

        getImagenesAtractivoTuristico();
        textViewratingBar=findViewById(R.id.textViewRatingBar);
        //textViewratingBar.setOnClickListener();
        ratingBar=findViewById(R.id.rating);

        linearLayoutcategorias=findViewById(R.id.linearLatyoutCategorias);
        tagGroup = (TagView)findViewById(R.id.tag_group2);

        textViewOpiniones=findViewById(R.id.textViewOpinionesn);
        mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                atractivoTuristico = dataSnapshot.getValue(AtractivoTuristico.class);
                textViewDescripcionAT.setText(atractivoTuristico.getDescripcion());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child(Referencias.CATEGORIAATRACTIVOTURISTICO).child(atractivoTuristico.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tagGroup.removeAll();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Categoria categoria=dataSnapshot1.getValue(Categoria.class);
                    Tag tag=new Tag(categoria.getEtiqueta());
                    tagGroup.addTag(tag);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        textViewButtonModificarDescripcion =findViewById(R.id.textViewModificarDescripcion);
        textViewButtonModificarCategorias =findViewById(R.id.textViewModificarCategorias);

        textViewButtonModificarDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe registrarse para modificar descripción de atractivo turistico!",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetDescripcionAtractivoTuristico.class);
                    intent.putExtra("imagenes", imagenes);
                    intent.putExtra("atractivoTuristico", atractivoTuristico);
                    startActivity(intent);
                }
            }
        });
        textViewButtonModificarCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe registrarse para modificar categorias de atractivo turistico!",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetCategoriasAtractivoTuristico.class);
                    intent.putExtra("imagenes", imagenes);
                    intent.putExtra("atractivoTuristico", atractivoTuristico);
                    startActivity(intent);
                }
            }
        });
    }
    private void getImagenesAtractivoTuristico(){
        Glide.with(SugerirCambioAtractivoTuristico.this)
                .load(imagenes.get(0).getUrl())
                .fitCenter()
                .centerCrop()
                .into(imageView);

    }

}
