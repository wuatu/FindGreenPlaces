package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.Categoria;
import Clases.Contribucion;
import Clases.IdUsuario;
import Clases.Imagen;
import Clases.Referencias;
import Clases.SubirPuntos;

public class InformacionAdicionalAT extends AppCompatActivity {
    TextView textViewHorario;
    TextView textViewTelefono;
    TextView textViewPagina;
    TextView textViewRedesSociales;
    EditText editTextHorario;
    EditText editTextTelefono;
    EditText editTextPagina;
    EditText editTextRedesSociales;
    Button buttonAceptar;
    Button buttonCancelar;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    AtractivoTuristico atractivoTuristico;
    ArrayList<Imagen> imagenes;
    static final int horario=1;
    static final int telefono=2;
    static final int redes=3;
    static final int pagina=4;
    ArrayList<Contribucion> contribuciones=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_adicional_at);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));
        buttonAceptar=findViewById(R.id.buttonAceptar);
        buttonCancelar=findViewById(R.id.buttonCancelar);
        textViewHorario=findViewById(R.id.textViewHorario);
        textViewTelefono=findViewById(R.id.textViewTelefono);
        textViewPagina=findViewById(R.id.textViewPagina);
        textViewRedesSociales=findViewById(R.id.textViewRedesSociales);



        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Información Adicional");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextHorario=findViewById(R.id.editTextHorario);
        editTextTelefono=findViewById(R.id.editTextTelefono);
        editTextPagina=findViewById(R.id.editTextPagina);
        editTextRedesSociales=findViewById(R.id.editTextRedes);
        /*if(atractivoTuristico.getHorarioDeAtencion()!=null){
            editTextHorario.setText(atractivoTuristico.getHorarioDeAtencion());
        }
        if(atractivoTuristico.getTelefono()!=null){
            editTextTelefono.setText(atractivoTuristico.getTelefono());
        }
        if(atractivoTuristico.getPaginaWeb()!=null){
            editTextPagina.setText(atractivoTuristico.getPaginaWeb());
        }
        if(atractivoTuristico.getRedesSociales()!=null){
            editTextRedesSociales.setText(atractivoTuristico.getRedesSociales());
        }*/
        cancelar();
        aceptar();
        textViewHorario();
        textViewTelefono();
        textViewPagina();
        textViewRedesSociales();
    }

    public void textViewHorario(){
        textViewHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformacionAdicionalAT.this, SetHorarioDeAtencion.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivityForResult(intent,horario);
            }
        });
    }

    public void textViewTelefono(){
        textViewTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformacionAdicionalAT.this, SetTelefonoAT.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivityForResult(intent,telefono);
            }
        });
    }

    public void textViewPagina(){
        textViewPagina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformacionAdicionalAT.this, SetPaginaWebAT.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivityForResult(intent,pagina);
            }
        });
    }

    public void textViewRedesSociales(){
        textViewRedesSociales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformacionAdicionalAT.this, SetRedesSocialesAT.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivityForResult(intent,redes);
            }
        });
    }

    public void aceptar(){
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(InformacionAdicionalAT.this)
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
                                SubirPuntos subirPuntos=new SubirPuntos();
                                setResult(RESULT_OK, new Intent().putExtra("atractivoTuristico", atractivoTuristico).
                                        putExtra("contribuciones",contribuciones));
                                subirPuntos.SubirPuntos(InformacionAdicionalAT.this);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
    }

    public void cancelar(){
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(InformacionAdicionalAT.this)
                        .setTitle("Información")
                        .setMessage("Seguro Quieres Cancelar?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == horario && resultCode == RESULT_OK) {
            String nombre = data.getStringExtra("nombre");
            Contribucion contribucion = (Contribucion) data.getSerializableExtra("contribucion");
            atractivoTuristico.setHorarioDeAtencion(nombre);
            contribuciones.add(contribucion);
        }
        if (requestCode == telefono && resultCode == RESULT_OK) {
            String nombre = data.getStringExtra("nombre");
            Contribucion contribucion = (Contribucion) data.getSerializableExtra("contribucion");
            atractivoTuristico.setTelefono(nombre);
            contribuciones.add(contribucion);
        }
        if (requestCode == redes && resultCode == RESULT_OK) {
            String nombre = data.getStringExtra("nombre");
            Contribucion contribucion = (Contribucion) data.getSerializableExtra("contribucion");
            atractivoTuristico.setRedesSociales(nombre);
            contribuciones.add(contribucion);
        }
        if (requestCode == pagina && resultCode == RESULT_OK) {
            String nombre = data.getStringExtra("nombre");
            Contribucion contribucion = (Contribucion) data.getSerializableExtra("contribucion");
            atractivoTuristico.setPaginaWeb(nombre);
            contribuciones.add(contribucion);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
