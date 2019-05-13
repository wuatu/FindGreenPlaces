package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import Clases.AtractivoTuristico;
import Clases.Contribucion;
import Clases.IdUsuario;
import Clases.Referencias;
import Clases.Usuario;

public class SetTipsDeViaje extends AppCompatActivity {
    Usuario usuario;
    AtractivoTuristico atractivoTuristico;
    private TextView titulo;
    //private EditText descripcionAntigua;
    private EditText descripcionNueva;
    Button buttonEnviarDescripcion;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_tips_de_viaje);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Editar Tips de Viaje");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        //***
        titulo=findViewById(R.id.textViewTituloAT3);
        //descripcionAntigua=findViewById(R.id.textViewDescripcionAnteriorAT);
        descripcionNueva=findViewById(R.id.textViewNuevaDescripcionAT);
        buttonEnviarDescripcion=findViewById(R.id.buttonAceptar);
        //**
        titulo.setText(atractivoTuristico.getNombre());
        //descripcionAntigua.setText(atractivoTuristico.getDescripcion());
        descripcionNueva.setText(atractivoTuristico.getTipsDeViaje());
        buttonEnviarDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SetTipsDeViaje.this)
                        .setTitle("Editar Tips")
                        .setMessage("Esta seguro que quiere realizar estos cambios?")
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                atractivoTuristico.setTipsDeViaje(descripcionNueva.getText().toString());
                                mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                                DatabaseReference databaseReference=mDatabase.child(Referencias.CONTRIBUCIONESPORAT).
                                        child(atractivoTuristico.getId()).
                                        child(IdUsuario.getIdUsuario()).push();
                                String key=databaseReference.getKey();
                                Contribucion contribucion=new Contribucion(key,atractivoTuristico.getId(),IdUsuario.getIdUsuario(),Referencias.TIPSDEVIAJE,descripcionNueva.getText().toString());
                                databaseReference.setValue(contribucion);
                                //añade contribucion (a tabla "contribucionPorUsuario") por usuario
                                mDatabase.child(Referencias.CONTRIBUCIONESPORUSUARIO).
                                        child(IdUsuario.getIdUsuario()).
                                        child(atractivoTuristico.getId()).
                                        child(key).
                                        setValue(contribucion);

                                //subir puntos
                                final DatabaseReference databaseReference1=mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario());
                                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        usuario=dataSnapshot.getValue(Usuario.class);
                                        int puntos=Integer.valueOf(usuario.getPuntos())+2;
                                        if(puntos>=100){
                                            int nivel= Integer.valueOf(usuario.getNivel());
                                            if(nivel==1){
                                                usuario.setNivel("2");
                                            }
                                            if(nivel==2){
                                                usuario.setNivel("3");
                                            }
                                        }else{
                                            usuario.setPuntos(String.valueOf(puntos));

                                        }
                                        int contribuciones=Integer.valueOf(usuario.getContribuciones())+1;
                                        usuario.setContribuciones(String.valueOf(contribuciones));
                                        databaseReference1.setValue(usuario);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(SetTipsDeViaje.this,"Datos enviados correctamente",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }
        });
        Button cancelar=findViewById(R.id.buttonCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SetTipsDeViaje.this)
                        .setTitle("Editar Tips")
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
