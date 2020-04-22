package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Clases.AtractivoTuristico;
import Clases.Contribucion;
import Clases.IdUsuario;
import Clases.Referencias;
import Clases.Usuario;

public class SetNombreAT extends AppCompatActivity {
    Usuario usuario;
    AtractivoTuristico atractivoTuristico;
    private TextView titulo;
    //private EditText nombreAntigua;
    private EditText nombreNuevo;
    Button butttonAceptar;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nombre_at);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Editar Nombre");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        //***
        titulo=findViewById(R.id.textViewTituloAT3);

        nombreNuevo =findViewById(R.id.textViewNuevaNombreAT);
        butttonAceptar =findViewById(R.id.buttonAceptar);
        //**
        titulo.setText(atractivoTuristico.getNombre());
        nombreNuevo.setText(atractivoTuristico.getNombre());
        butttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atractivoTuristico.setNombre(nombreNuevo.getText().toString());
                Contribucion contribucion=new Contribucion("",atractivoTuristico.getId(),IdUsuario.getIdUsuario(),Referencias.NOMBRE,nombreNuevo.getText().toString(),Referencias.VISIBLE);

                //Toast.makeText(SetNombreAT.this,"Datos enviados correctamente",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK,
                        new Intent().putExtra("nombre", atractivoTuristico.getNombre()).
                                putExtra("contribucion",contribucion));
                finish();

            }
        });
        Button cancelar=findViewById(R.id.buttonCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SetNombreAT.this)
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

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
