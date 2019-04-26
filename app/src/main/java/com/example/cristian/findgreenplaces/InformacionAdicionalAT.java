package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.Imagen;
import Clases.Referencias;

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
                Intent intent = new Intent(InformacionAdicionalAT.this, HorarioDeAtencion.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);
            }
        });
    }

    public void textViewTelefono(){
        textViewTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformacionAdicionalAT.this, TelefonoAT.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);
            }
        });
    }

    public void textViewPagina(){
        textViewPagina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformacionAdicionalAT.this, PaginaWebAT.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);
            }
        });
    }

    public void textViewRedesSociales(){
        textViewRedesSociales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformacionAdicionalAT.this, RedesSocialesAT.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);
            }
        });
    }

    public void aceptar(){
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextHorario.getText().equals("")){
                    atractivoTuristico.setHorarioDeAtencion(editTextHorario.getText().toString());
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                }
                if(!editTextTelefono.getText().equals("")){
                    atractivoTuristico.setTelefono(editTextTelefono.getText().toString());
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                }
                if(!editTextPagina.getText().equals("")){
                    atractivoTuristico.setPaginaWeb(editTextPagina.getText().toString());
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                }
                if(!editTextRedesSociales.getText().equals("")){
                    atractivoTuristico.setRedesSociales(editTextRedesSociales.getText().toString());
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                }
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(InformacionAdicionalAT.this,AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Información")
                        .setMessage("Seguro Quieres Enviar Estos Datos?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(InformacionAdicionalAT.this, VisualizarAtractivoTuristico.class);
                                intent.putExtra("imagenes", imagenes);
                                intent.putExtra("atractivoTuristico", atractivoTuristico);
                                startActivity(intent);
                                Toast.makeText(InformacionAdicionalAT.this,"Datos enviados correctamente!",Toast.LENGTH_SHORT).show();
                                finish();
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
        });
    }

    public void cancelar(){
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(InformacionAdicionalAT.this,AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Información")
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
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}
