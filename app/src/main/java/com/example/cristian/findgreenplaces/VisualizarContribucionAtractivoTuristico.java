package com.example.cristian.findgreenplaces;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import Clases.Adapter.AdapterListViewContribucionAtractivoTuristico;
import Clases.Models.AtractivoTuristico;
import Clases.Utils.GetImagenes;
import Clases.Utils.IdUsuario;
import Clases.Models.Imagen;
import Clases.Utils.Referencias;
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
                    AtractivoTuristico atractivoTuristico2 = dataSnapshot1.getValue(AtractivoTuristico.class);
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico2.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AtractivoTuristico atractivoTuristico = dataSnapshot.getValue(AtractivoTuristico.class);
                            if(atractivoTuristico!=null){
                                if(atractivoTuristico.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                                    atractivoTuristicos.add(atractivoTuristico);
                                }
                            }
                            adapter=new AdapterListViewContribucionAtractivoTuristico(VisualizarContribucionAtractivoTuristico.this,atractivoTuristicos);
                            lista=(ListView) findViewById(R.id.listViewVisualizarAtractivoTuristico);
                            lista.setAdapter((ListAdapter) adapter);
                            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try{
                                        AtractivoTuristico atractivoTuristico;
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

                        }
                    });

                }
                //Log.v("atractivoo",String.valueOf(atractivoTuristicos.size()));

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
