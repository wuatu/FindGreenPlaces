package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Clases.AtractivoTuristico;
import Clases.Referencias;

public class SetDescripcionAtractivoTuristico extends AppCompatActivity {

    AtractivoTuristico atractivoTuristico;
    private TextView titulo;
    private EditText descripcionAntigua;
    private EditText descripcionNueva;
    Button buttonEnviarDescripcion;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_descripcion_atractivo_turistico);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        //***
        titulo=findViewById(R.id.textViewTituloAT3);
        descripcionAntigua=findViewById(R.id.textViewDescripcionAnteriorAT);
        descripcionNueva=findViewById(R.id.textViewNuevaDescripcionAT);
        buttonEnviarDescripcion=findViewById(R.id.buttonEnviarDescripcion);
        //**
        titulo.setText(atractivoTuristico.getNombre());
        descripcionAntigua.setText(atractivoTuristico.getDescripcion());
        descripcionNueva.setText(descripcionAntigua.getText().toString());
        buttonEnviarDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atractivoTuristico.setDescripcion(descripcionNueva.getText().toString());
                mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(SetDescripcionAtractivoTuristico.this, android.R.style.Theme_Material_Dialog);
                } else {
                    builder = new AlertDialog.Builder(SetDescripcionAtractivoTuristico.this);
                }
                builder.setTitle("Descripcion Actualizada")
                        .setMessage("Descripcion Actualizada con Exito!")
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
