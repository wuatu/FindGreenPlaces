package com.example.cristian.findgreenplaces;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Clases.AdapterListViewContribucionAtractivoTuristico;
import Clases.AtractivoTuristico;
import Clases.GetImagenes;
import Clases.IdUsuario;
import Clases.Imagen;
import Clases.Referencias;
import Interfaz.OnGetDataListenerAtractivoTuristico;
import Interfaz.OnGetDataListenerImagenes;

public class VisualizarContribucionAtractivoTuristico extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    //RecyclerView recyclerViewContribucionAtractivoTuristico;
    ArrayList<AtractivoTuristico> atractivoTuristicos;
    ListView lista;
    Adapter adapter;
    AtractivoTuristico atractivoTuristico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribucion_atractivo_turistico);

        //recyclerViewContribucionAtractivoTuristico=findViewById(R.id.recyclerViewVisualizarAtractivoTuristico);
        //recyclerViewContribucionAtractivoTuristico.setLayoutManager(new LinearLayoutManager(this));
        atractivoTuristicos=new ArrayList();
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Contribuciones");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //recyclerViewContribucionAtractivoTuristico.setAdapter(adapter);
        query(new OnGetDataListenerAtractivoTuristico() {
            @Override
            public void onSuccess(AtractivoTuristico atractivoTuristico) {
                startVisualizarAtractivoTuristico(atractivoTuristico);
            }
            @Override
            public void onStart() {
            }
            @Override
            public void onFailure() {
            }
        });




/*        linearLayoutItemAtractivoTuristico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizarContribucionAtractivoTuristico.this, VisualizarAtractivoTuristicoFragment.class);
                //intent.putExtra("bandera", bandera);
                startActivity(intent);
            }
        });*/
    }

    private void query(final OnGetDataListenerAtractivoTuristico listener) {
        listener.onStart();
        mDatabase.child(Referencias.CONTRIBUCIONES).child(IdUsuario.getIdUsuario()).child(Referencias.ATRACTIVOTURISTICO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //atractivoTuristicos.removeAll(atractivoTuristicos);
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        AtractivoTuristico atractivoTuristico = dataSnapshot1.getValue(AtractivoTuristico.class);
                    if(atractivoTuristico.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                        atractivoTuristicos.add(atractivoTuristico);
                    }
                }
                //Log.v("atractivoo",String.valueOf(atractivoTuristicos.size()));
                adapter=new AdapterListViewContribucionAtractivoTuristico(VisualizarContribucionAtractivoTuristico.this,atractivoTuristicos);
                lista=(ListView) findViewById(R.id.listViewVisualizarAtractivoTuristico);
                lista.setAdapter((ListAdapter) adapter);
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try{
                                atractivoTuristico = (AtractivoTuristico) adapter.getItem(position);
                                listener.onSuccess(atractivoTuristico);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }




    public void startVisualizarAtractivoTuristico(AtractivoTuristico atractivoTuristico){
        Log.v("atractivoooo",atractivoTuristico.getId());
        GetImagenes getImagenes=new GetImagenes();
        getImagenes.getImagenesAtractivoTuristico(atractivoTuristico, new OnGetDataListenerImagenes() {
            @Override
            public void onSuccess(ArrayList<Imagen> imagenes,AtractivoTuristico atractivoTuristico1) {
                Log.v("atractivoooo",String.valueOf(imagenes.size()));
                Intent intent=new Intent(getApplicationContext(),VisualizarAtractivoTuristico.class);
                intent.putExtra("atractivoTuristico", atractivoTuristico1);
                intent.putExtra("imagenes",imagenes);
                startActivity(intent);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

}
