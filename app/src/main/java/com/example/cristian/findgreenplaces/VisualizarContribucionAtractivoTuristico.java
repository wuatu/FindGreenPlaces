package com.example.cristian.findgreenplaces;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.IdUsuario;
import Clases.Referencias;

public class VisualizarContribucionAtractivoTuristico extends AppCompatActivity {
    TextView titulo;
    TextView ciudad;
    TextView comuna;
    LinearLayout linearLayoutItemAtractivoTuristico;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    ArrayList<AtractivoTuristico> atractivoTuristicos;
    boolean bandera=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribucion_atractivo_turistico);
        titulo=findViewById(R.id.textViewTitulo);
        ciudad=findViewById(R.id.textViewCiudad);
        comuna=findViewById(R.id.textViewComuna);
        linearLayoutItemAtractivoTuristico=findViewById(R.id.linearLayoutItemAtractivoTuristico);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        atractivoTuristicos=new ArrayList<>();
        mDatabase.child(Referencias.CONTRIBUCIONES).child(IdUsuario.getIdUsuario()).child(Referencias.ATRACTIVOTURISTICO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    AtractivoTuristico atractivoTuristico=dataSnapshot1.getValue(AtractivoTuristico.class);
                    titulo.setText(atractivoTuristico.getNombre());
                    ciudad.setText(atractivoTuristico.getCiudad());
                    comuna.setText(atractivoTuristico.getComuna());
                    atractivoTuristicos.add(atractivoTuristico);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        linearLayoutItemAtractivoTuristico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizarContribucionAtractivoTuristico.this, VisualizarAtractivoTuristico.class);
                //intent.putExtra("bandera", bandera);
                startActivity(intent);
            }
        });
    }
}
