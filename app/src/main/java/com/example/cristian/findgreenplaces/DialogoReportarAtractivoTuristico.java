
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
import Clases.Models.Comentario;
import Clases.Utils.IdUsuario;
import Clases.Utils.Referencias;
import Clases.Models.Usuario;

public class DialogoReportarAtractivoTuristico extends AppCompatActivity {
    Comentario comentario;
    AtractivoTuristico atractivoTuristico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogo_reportat_atractivo_turistico);
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

        TextView textViewEliminar=findViewById(R.id.textViewEliminar);
        LinearLayout linearLayoutEliminar=findViewById(R.id.linearLayoutEliminar);
        if(!atractivoTuristico.getIdUsuario().equalsIgnoreCase(IdUsuario.getIdUsuario())){
            textViewEliminar.setVisibility(View.GONE);
            linearLayoutEliminar.setVisibility(View.GONE);
        }

        textViewEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DialogoReportarAtractivoTuristico.this)
                        .setTitle("Eliminar Atractivo Turístico")
                        .setMessage("Esta seguro que quiere eliminar un atractivo turístico?")
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).removeValue();
                                Toast.makeText(DialogoReportarAtractivoTuristico.this,"El atractivo turistico ha sido eliminado",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .show();
            }
        });

        textViewReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogoReportarAtractivoTuristico.this,"El atractivo turístico ha sido reportado",Toast.LENGTH_SHORT).show();
                int contadorReportes=Integer.valueOf(atractivoTuristico.getContadorReportes())+1;
                if(contadorReportes>=10){
                    atractivoTuristico.setVisible(Referencias.INVISIBLE);
                    atractivoTuristico.setContadorReportes(String.valueOf(contadorReportes));
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                    mDatabase.child(Referencias.CONTRIBUCIONES).child(atractivoTuristico.getIdUsuario()).child(Referencias.ATRACTIVOTURISTICO).setValue(atractivoTuristico);
                }else {
                    atractivoTuristico.setContadorReportes(String.valueOf(contadorReportes));
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
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
