package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import Clases.Models.AtractivoTuristico;
import Clases.Models.Categoria;
import Clases.Models.Contribucion;
import Clases.Utils.IdUsuario;
import Clases.Models.Imagen;
import Clases.Utils.Referencias;
import Clases.Utils.SubirPuntos;

public class SugerirCambioAtractivoTuristico extends AppCompatActivity {
    ArrayList<Categoria> categoriasNuevas=new ArrayList<>();
    TextView textViewButtonModificarNombre;
    TextView textViewButtonModificarDescripcion;
    TextView textViewButtonModificarCategorias;
    TextView textViewButtonModificarTips;
    TextView textViewButtonModificarHorario;
    TextView textViewButtonModificarTelefono;
    TextView textViewButtonModificarPaginaWeb;
    TextView textViewButtonModificarRedesSociales;
    ArrayList<Categoria> categorias;
    ArrayList<Imagen> imagenes;
    AtractivoTuristico atractivoTuristico;
    private TextView titulo;
    private TextView descripcion;
    TextView tips;
    TextView horario;
    TextView telefono;
    TextView pagina;
    TextView redes;
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
    String nivel="";
    private static final int NOMBRE = 0;
    private static final int DESCRIPCION = 1;
    private static final int TIPS = 2;
    private static final int HORARIO = 3;
    private static final int TELEFONO = 4;
    private static final int PAGINA = 5;
    private static final int REDES = 6;
    private static final int CATEGORIAS = 7;
    Button buttonAceptar;
    boolean isCategoria=false;
    ArrayList<Contribucion> contribuciones=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerir_cambio_atractivo_turistico);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Vista Previa Contribución");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));
        categorias=((ArrayList<Categoria>)getIntent().getSerializableExtra("categorias"));
        buttonAceptar=findViewById(R.id.buttonAceptar);
        titulo = (TextView) findViewById(R.id.textViewTituloAT2);
        titulo.setText(atractivoTuristico.getNombre());

        //textViewDescripcionAT.setText(atractivoTuristico.getDescripcion());
        textViewButtonModificarTips=findViewById(R.id.textViewModificarTips);
        textViewButtonModificarHorario=findViewById(R.id.textViewModificarHorario);
        textViewButtonModificarTelefono=findViewById(R.id.textViewModificarTelefono);
        textViewButtonModificarPaginaWeb=findViewById(R.id.textViewModificarPagina);
        textViewButtonModificarRedesSociales=findViewById(R.id.textViewModificarRedes);

        descripcion =findViewById(R.id.textViewDescripcionAT);
        descripcion.setText(atractivoTuristico.getDescripcion());
        tips= findViewById(R.id.textViewTipsAT);
        tips.setText(atractivoTuristico.getTipsDeViaje());
        horario= findViewById(R.id.textViewHorarioAT);
        horario.setText(atractivoTuristico.getHorarioDeAtencion());
        telefono= findViewById(R.id.textViewTelefonoAT);
        telefono.setText(atractivoTuristico.getTelefono());
        pagina= findViewById(R.id.textViewPaginaAT);
        pagina.setText(atractivoTuristico.getPaginaWeb());
        redes= findViewById(R.id.textViewRedesAT);
        redes.setText(atractivoTuristico.getRedesSociales());

        imageView=findViewById(R.id.imageViewVAT);


        getImagenesAtractivoTuristico();
        textViewratingBar=findViewById(R.id.textViewRatingBar);
        //textViewratingBar.setOnClickListener();
        ratingBar=findViewById(R.id.rating);

        linearLayoutcategorias=findViewById(R.id.linearLatyoutCategorias);
        tagGroup = (TagView)findViewById(R.id.tag_group2);

        textViewOpiniones=findViewById(R.id.textViewOpinionesn);
        /*mDatabase.child(Referencias.CATEGORIAATRACTIVOTURISTICO).child(atractivoTuristico.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tagGroup.removeAll();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Categoria categoria=dataSnapshot1.getValue(Categoria.class);
                    Tag tag=new Tag(categoria.getEtiqueta());
                    tagGroup.addTag(tag);
                    categorias.add(categoria);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
        for (Categoria categoria:categorias){
            Tag tag=new Tag(categoria.getEtiqueta());
            tag.layoutColor = getResources().getColor(R.color.colorPrimary);
            tagGroup.addTag(tag);
            Log.v("categoriass",categoria.getEtiqueta());
        }
        textViewButtonModificarNombre =findViewById(R.id.textViewModificarNombre);
        textViewButtonModificarDescripcion =findViewById(R.id.textViewModificarDescripcion);
        textViewButtonModificarCategorias =findViewById(R.id.textViewModificarCategorias);

        //consulta para saber nivel de usuario
        mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario()).child(Referencias.NIVEL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nivel=dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Button cancelar=findViewById(R.id.buttonCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SugerirCambioAtractivoTuristico.this)
                        .setTitle("Editar Nombre")
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
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(SugerirCambioAtractivoTuristico.this)
                        .setTitle("Enviar Cambios")
                        .setMessage("Esta seguro que quiere realizar estos cambios?")
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //modifico el atractivo turistico con la nueva información
                                mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                                for (Contribucion contribucion:contribuciones){
                                    //añade contribucion (a tabla "contribucionPorAt") por atractivo turistio
                                    DatabaseReference databaseReference=mDatabase.child(Referencias.CONTRIBUCIONESPORAT).
                                            child(atractivoTuristico.getId()).
                                            child(IdUsuario.getIdUsuario()).push();
                                    String key=databaseReference.getKey();
                                    contribucion.setId(key);
                                    databaseReference.setValue(contribucion);

                                    //añade contribucion (a tabla "contribucionPorUsuario") por usuario
                                    mDatabase.child(Referencias.CONTRIBUCIONESPORUSUARIO).
                                            child(IdUsuario.getIdUsuario()).
                                            child(atractivoTuristico.getId()).
                                            child(key).
                                            setValue(contribucion);
                                }
                                if(isCategoria) {
                                    mDatabase.child(Referencias.CATEGORIAATRACTIVOTURISTICO).child(atractivoTuristico.getId()).removeValue();
                                    for (Categoria categoria : categorias) {
                                        DatabaseReference dbrcategoria = mDatabase.child(Referencias.KEYSATRACTIVOTURISTICO).child(categoria.getEtiqueta());
                                        //String keyCategoria2 = dbrcategoria.getKey();
                                        dbrcategoria.setValue(categoria);
                                        categoria.setId(categoria.getEtiqueta());
                                        mDatabase.child(Referencias.CATEGORIAATRACTIVOTURISTICO).child(atractivoTuristico.getId()).child(categoria.getEtiqueta()).setValue(categoria);
                                    }
                                }
                                isCategoria=false;
                                if(contribuciones!=null){
                                    if(contribuciones.size()>0){
                                        SubirPuntos.subirPuntosUsuarioQueContribuye(SugerirCambioAtractivoTuristico.this,contribuciones.size());
                                        Toast.makeText(SugerirCambioAtractivoTuristico.this,"Subes "+contribuciones.size()+" puntos",Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        SubirPuntos.subirPuntosUsuarioQueContribuye(SugerirCambioAtractivoTuristico.this,1);
                                        Toast.makeText(SugerirCambioAtractivoTuristico.this,"Subes "+1+" punto",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                setResult(RESULT_OK, new Intent().putExtra("atractivoTuristico", atractivoTuristico).
                                        putExtra("contribuciones",contribuciones).
                                        putExtra("categorias",categorias));
                                finish();

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();


            }
        });
        textViewButtonModificarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe registrarse para modificar nombre de atractivo turistico!",Toast.LENGTH_SHORT).show();
                }else {
                    if(nivel.equals("0") || nivel.equals("1")){
                        if(atractivoTuristico.getIdUsuario().equals(IdUsuario.idUsuario)){
                            Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetNombreAT.class);
                            intent.putExtra("imagenes", imagenes);
                            intent.putExtra("atractivoTuristico", atractivoTuristico);
                            //startActivity(intent);
                            startActivityForResult(intent,NOMBRE);
                        }
                        else{
                            Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe ser nivel 3 para modificar nombre de otro usuario",Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(nivel.equals("2") || nivel.equals("3")){
                        Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetNombreAT.class);
                        intent.putExtra("imagenes", imagenes);
                        intent.putExtra("atractivoTuristico", atractivoTuristico);
                        startActivityForResult(intent,NOMBRE);
                    }

                }
            }
        });
        textViewButtonModificarDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe registrarse para modificar descripción de atractivo turistico!",Toast.LENGTH_SHORT).show();
                }else {
                    if(nivel.equals("0") || nivel.equals("1")){
                        if(atractivoTuristico.getIdUsuario().equals(IdUsuario.idUsuario)){
                            Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetDescripcionAtractivoTuristico.class);
                            intent.putExtra("imagenes", imagenes);
                            intent.putExtra("atractivoTuristico", atractivoTuristico);
                            startActivityForResult(intent,DESCRIPCION);
                        }
                        else{
                            Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe ser nivel 3 para modificar descripción de otro usuario",Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(nivel.equals("2") || nivel.equals("3")){
                        Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetDescripcionAtractivoTuristico.class);
                        intent.putExtra("imagenes", imagenes);
                        intent.putExtra("atractivoTuristico", atractivoTuristico);
                        startActivityForResult(intent,DESCRIPCION);
                    }

                }
            }
        });
        textViewButtonModificarTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe registrarse para contribuir en atractivo turistico",Toast.LENGTH_SHORT).show();
                }else {
                    if(nivel.equals("0") || nivel.equals("1")){
                        if(atractivoTuristico.getIdUsuario().equals(IdUsuario.idUsuario)){
                            Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetTipsDeViaje.class);
                            intent.putExtra("imagenes", imagenes);
                            intent.putExtra("atractivoTuristico", atractivoTuristico);
                            startActivityForResult(intent,TIPS);
                        }
                        else{
                            Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe ser nivel 3 para contribuir en tips de otro usuario",Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(nivel.equals("2") || nivel.equals("3")){
                        Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetTipsDeViaje.class);
                        intent.putExtra("imagenes", imagenes);
                        intent.putExtra("atractivoTuristico", atractivoTuristico);
                        startActivityForResult(intent,TIPS);
                    }
                }
            }
        });
        textViewButtonModificarCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe registrarse para contribuir en atractivo turistico",Toast.LENGTH_SHORT).show();
                }else {
                    if(nivel.equals("0") ){
                        if(atractivoTuristico.getIdUsuario().equals(IdUsuario.idUsuario)){
                            Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetCategoriasAtractivoTuristico.class);
                            intent.putExtra("imagenes", imagenes);
                            intent.putExtra("atractivoTuristico", atractivoTuristico);
                            intent.putExtra("categorias", categorias);
                            startActivityForResult(intent,CATEGORIAS);
                        }
                        else{
                            Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe ser nivel 2 para contribuir en categoria de otro usuario",Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(nivel.equals("1")|| nivel.equals("2") || nivel.equalsIgnoreCase("3")){
                        Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetCategoriasAtractivoTuristico.class);
                        intent.putExtra("imagenes", imagenes);
                        intent.putExtra("atractivoTuristico", atractivoTuristico);
                        intent.putExtra("categorias", categorias);
                        startActivityForResult(intent,CATEGORIAS);
                    }

                }
            }
        });
        textViewButtonModificarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe registrarse para contribuir en atractivo turistico",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SugerirCambioAtractivoTuristico.this,SetHorarioDeAtencion.class);
                    intent.putExtra("imagenes", imagenes);
                    intent.putExtra("atractivoTuristico", atractivoTuristico);
                    startActivityForResult(intent,HORARIO);
                }
            }
        });
        textViewButtonModificarTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe registrarse para contribuir en atractivo turistico",Toast.LENGTH_SHORT).show();
                }else {
                Intent intent = new Intent(SugerirCambioAtractivoTuristico.this,SetTelefonoAT.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                    startActivityForResult(intent,TELEFONO);
                }
            }
        });
        textViewButtonModificarPaginaWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe registrarse para contribuir en atractivo turistico",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetPaginaWebAT.class);
                    intent.putExtra("imagenes", imagenes);
                    intent.putExtra("atractivoTuristico", atractivoTuristico);
                    startActivityForResult(intent,PAGINA);
                }
            }
        });
        textViewButtonModificarRedesSociales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(SugerirCambioAtractivoTuristico.this,"Debe registrarse para contribuir en atractivo turistico!",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SugerirCambioAtractivoTuristico.this, SetRedesSocialesAT.class);
                    intent.putExtra("imagenes", imagenes);
                    intent.putExtra("atractivoTuristico", atractivoTuristico);
                    startActivityForResult(intent,REDES);
                }
            }
        });
    }
    private void getImagenesAtractivoTuristico(){
        if(imagenes.size()>0) {
            Glide.with(SugerirCambioAtractivoTuristico.this)
                    .load(imagenes.get(0).getUrl())
                    .fitCenter()
                    .centerCrop()
                    .into(imageView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NOMBRE && resultCode == RESULT_OK) {
            String nombre = data.getStringExtra("nombre");
            Contribucion contribucion = (Contribucion) data.getSerializableExtra("contribucion");
            titulo.setText(nombre);
            atractivoTuristico.setNombre(titulo.getText().toString());
            contribuciones.add(contribucion);
        }
        if (requestCode == DESCRIPCION && resultCode == RESULT_OK) {
            String nombre = data.getStringExtra("descripcion");
            Contribucion contribucion = (Contribucion) data.getSerializableExtra("contribucion");
            descripcion.setText(nombre);
            atractivoTuristico.setDescripcion(descripcion.getText().toString());
            contribuciones.add(contribucion);
        }
        if (requestCode == TIPS && resultCode == RESULT_OK) {
            String nombre = data.getStringExtra("nombre");
            Contribucion contribucion = (Contribucion) data.getSerializableExtra("contribucion");
            tips.setText(nombre);
            atractivoTuristico.setTipsDeViaje(tips.getText().toString());
            contribuciones.add(contribucion);
        }
        if (requestCode == HORARIO && resultCode == RESULT_OK) {
            String nombre = data.getStringExtra("nombre");
            Contribucion contribucion = (Contribucion) data.getSerializableExtra("contribucion");
            horario.setText(nombre);
            atractivoTuristico.setHorarioDeAtencion(horario.getText().toString());
            contribuciones.add(contribucion);
        }
        if (requestCode == TELEFONO && resultCode == RESULT_OK) {
            String nombre = data.getStringExtra("nombre");
            Contribucion contribucion = (Contribucion) data.getSerializableExtra("contribucion");
            telefono.setText(nombre);
            atractivoTuristico.setTelefono(telefono.getText().toString());
            contribuciones.add(contribucion);
        }
        if (requestCode == PAGINA && resultCode == RESULT_OK) {
            String nombre = data.getStringExtra("nombre");
            Contribucion contribucion = (Contribucion) data.getSerializableExtra("contribucion");
            pagina.setText(nombre);
            atractivoTuristico.setPaginaWeb(pagina.getText().toString());
            contribuciones.add(contribucion);
        }
        if (requestCode == REDES && resultCode == RESULT_OK) {
            String nombre = data.getStringExtra("nombre");
            Contribucion contribucion = (Contribucion) data.getSerializableExtra("contribucion");
            redes.setText(nombre);
            atractivoTuristico.setRedesSociales(redes.getText().toString());
            contribuciones.add(contribucion);
        }
        if (requestCode == CATEGORIAS && resultCode == RESULT_OK) {
            //String nombre = data.getStringExtra("nombre");
            ArrayList<Contribucion> contribuciones2 = (ArrayList<Contribucion>) data.getSerializableExtra("contribuciones");
            categorias= (ArrayList<Categoria>) data.getSerializableExtra("categorias");
            categoriasNuevas= (ArrayList<Categoria>) data.getSerializableExtra("categoriasNuevas");
            //Log.v("riico",categorias2.get(0).getEtiqueta());
            //titulo.setText(nombre);
            //atractivoTuristico.setRedesSociales();
            for (Contribucion contribucion:contribuciones2) {
                contribuciones.add(contribucion);
            }

            for (Categoria categoria : categoriasNuevas) {
                Tag tag=new Tag(categoria.getEtiqueta());
                tag.layoutColor = getResources().getColor(R.color.colorPrimary);
                tagGroup.addTag(tag);
                //categorias.add(categoria);
            }
            isCategoria=true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

}
