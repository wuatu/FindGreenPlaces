package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import Clases.Imagen;
import Clases.Referencias;

public class SetCategoriasAtractivoTuristico extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    AtractivoTuristico atractivoTuristico;
    private TextView titulo;
    AutoCompleteTextView textViewCategoria;
    Button buttonEnviarCalificacion;
    int contadorCategorias=0;
    TagView tagGroup;
    ArrayList<Tag> tagCategorias;
    ArrayList<Categoria> categorias;
    ArrayList<Categoria> categoriasAñadidas;
    ArrayList<Categoria> categoriasEliminadas;
    Button addCategoria;
    ArrayList<Imagen> imagenes;
    Categoria categoriaEliminada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_categorias_atractivo_turistico);
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Editar Categorias");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        titulo = (TextView) findViewById(R.id.textViewTituloAT2);
        textViewCategoria = (AutoCompleteTextView) findViewById(R.id.autocomplete_region);
        titulo.setText(atractivoTuristico.getNombre());
        buttonEnviarCalificacion=findViewById(R.id.buttonEnviarCalificacion);
        categorias =new ArrayList<>();
        categoriasEliminadas =new ArrayList<>();
        categoriasAñadidas =new ArrayList<>();
        tagGroup = (TagView)findViewById(R.id.tag_group3);
        addCategoria= (Button) findViewById(R.id.buttonaddCategoria);
        agregarCategorias();
        mDatabase.child(Referencias.CATEGORIAATRACTIVOTURISTICO).child(atractivoTuristico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Categoria categoria =dataSnapshot1.getValue(Categoria.class);
                    String sX="   x";
                    String sCategoria=categoria.getEtiqueta();
                    sCategoria=sCategoria.concat(sX);
                    Tag tag=new Tag(sCategoria);
                    tagGroup.addTag(tag);
                    categorias.add(categoria);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(final Tag tag, int i) {
                categoriaEliminada=null;
                for (int j=0;j<categoriasAñadidas.size();j++){
                    if((categoriasAñadidas.get(j).getEtiqueta()+"   x").equalsIgnoreCase(tag.text)){
                        categoriaEliminada=categoriasAñadidas.get(j);
                        break;
                    }
                }
                tagGroup.remove(i);
                if(i<categorias.size()) {
                    categoriaEliminada = categorias.get(i);
                    categorias.remove(i);
                }
                categoriasEliminadas.add(categoriaEliminada);

            }
        });
        buttonEnviarCalificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Categoria categoria: categoriasAñadidas){
                        DatabaseReference dbrcategoria= mDatabase.child(Referencias.KEYSATRACTIVOTURISTICO).child(categoria.getEtiqueta()).push();
                        String keyCategoria2=dbrcategoria.getKey();
                        dbrcategoria.setValue(categoria);
                        categoria.setId(keyCategoria2);
                        mDatabase.child(Referencias.CATEGORIAATRACTIVOTURISTICO).child(atractivoTuristico.getId()).child(keyCategoria2).setValue(categoria);
                }
                for (Categoria categoria: categoriasEliminadas){
                    mDatabase.child(Referencias.KEYSATRACTIVOTURISTICO).child(categoria.getEtiqueta()).child(categoria.getId()).removeValue();
                    mDatabase.child(Referencias.CATEGORIAATRACTIVOTURISTICO).child(atractivoTuristico.getId()).child(categoria.getId()).removeValue();
                }
                categoriasEliminadas.clear();
                categoriasAñadidas.clear();
                categorias.clear();
                Toast.makeText(SetCategoriasAtractivoTuristico.this,"Categorias actualizadas correctamente",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        Button cancelar=findViewById(R.id.buttonCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SetCategoriasAtractivoTuristico.this)
                        .setTitle("Editar Descripción")
                        .setMessage("Esta seguro que quiere cancelar?")
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
    }
    public void agregarCategorias(){
        addCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textViewCategoria= (AutoCompleteTextView) findViewById(R.id.autocomplete_region);

                if(!textViewCategoria.getText().toString().isEmpty())
                {
                    String sX="   x";
                    String sCategoria=textViewCategoria.getText().toString();
                    Categoria categoria=new Categoria(sCategoria);
                    categoriasAñadidas.add(categoria);
                    sCategoria=sCategoria.concat(sX);
                    Tag tag=new Tag(sCategoria);
                    tagGroup.addTag(tag);
                    contadorCategorias++;
                    textViewCategoria.setText("");

                }
                else {
                    Snackbar.make(v, "Debe Agregar al Menos una Categoria", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        //set click listener
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int i) {
                tagGroup.remove(i);
                contadorCategorias--;
                categorias.remove(i);
            }
        });
        //set delete listener
        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
            }
        });



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

}
