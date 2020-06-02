package com.example.cristian.findgreenplaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Clases.Models.AtractivoTuristico;

import Clases.Utils.IdUsuario;
import Clases.Models.Imagen;
import Clases.Utils.Referencias;
import Clases.Models.Usuario;

public class DialogoReportarFoto extends AppCompatActivity {

    Imagen imagen;
    AtractivoTuristico atractivoTuristico;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogo_reportar_foto);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
//        getSupportActionBar().hide();
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int ancho=dm.widthPixels;
        int alto=dm.heightPixels;
        getWindow().setLayout((int) (ancho*.8),LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        TextView textViewReportar=findViewById(R.id.textViewReportar);
        atractivoTuristico= (AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico");
        imagen=((Imagen)getIntent().getSerializableExtra("imagen"));
        position=(int)getIntent().getSerializableExtra("position");

        TextView textViewEliminar=findViewById(R.id.textViewEliminar);
        LinearLayout linearLayoutEliminar=findViewById(R.id.linearLayoutEliminar);
        if(!imagen.getIdUsuario().equalsIgnoreCase(IdUsuario.getIdUsuario())){
            textViewEliminar.setVisibility(View.GONE);
            linearLayoutEliminar.setVisibility(View.GONE);
        }

        textViewEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DialogoReportarFoto.this)
                        .setTitle("Eliminar imagen")
                        .setMessage("Esta seguro que quiere eliminar esta imagen?")
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDatabase.child(Referencias.IMAGENES).child(imagen.getIdAtractivo()).child(imagen.getId()).removeValue();
                                mDatabase.child(Referencias.IMAGENESCONTRIBUIDAS).child(IdUsuario.getIdUsuario()).child(imagen.getId()).removeValue();
                                Toast.makeText(DialogoReportarFoto.this,"La imagen ha sido eliminada",Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, new Intent().putExtra("imagen", imagen).putExtra("position",position));
                                finish();
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
            }
        });

        textViewReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogoReportarFoto.this,"La imagen ha sido reportada  ",Toast.LENGTH_SHORT).show();
                //consulta para ver cuantos reportes tiene
                int contadorReportes=Integer.valueOf(imagen.getContadorReportes())+1;
                if(contadorReportes>=10){
                    //con 10 reportes la foto se elimina
                    imagen.setVisible(Referencias.INVISIBLE);
                    mDatabase.child(Referencias.IMAGENES).child(imagen.getIdAtractivo()).child(imagen.getId()).setValue(imagen);
                }else{
                    imagen.setContadorReportes(String.valueOf(contadorReportes));
                    mDatabase.child(Referencias.IMAGENES).child(imagen.getIdAtractivo()).child(imagen.getId()).setValue(imagen);
                }




                final DatabaseReference databaseReference=mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Usuario usuario=dataSnapshot.getValue(Usuario.class);
                        int puntos=Integer.valueOf(usuario.getPuntos());
                        if(puntos>0){
                            puntos=puntos-1;
                            if(puntos<0){
                                int nivel= Integer.valueOf(usuario.getNivel());
                                if(nivel==2){
                                    usuario.setNivel("1");
                                    usuario.setPuntos("99");
                                    usuario.setNombreNivel(Referencias.PRINCIPIANTE);

                                }
                                if(nivel==3){
                                    usuario.setNivel("2");
                                    usuario.setNivel("99");
                                    usuario.setNombreNivel(Referencias.AVANZADO);
                                }
                            }else{
                                usuario.setPuntos(String.valueOf(puntos));
                            }
                            databaseReference.setValue(usuario);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                finish();
            }

        });


    }
}
