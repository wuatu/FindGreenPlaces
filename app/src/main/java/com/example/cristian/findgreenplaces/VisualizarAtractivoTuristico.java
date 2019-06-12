package com.example.cristian.findgreenplaces;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.Imagen;
import Clases.Referencias;
import Fragment.VisualizarAtractivoTuristicoFragment;
import Fragment.ComentariosATFrafment;
import Fragment.FotosATFragment;

public class VisualizarAtractivoTuristico extends AppCompatActivity{
    BottomNavigationView navigation;
    private TextView mTextMessage;
    Fragment fragmentComentariosAT;
    Fragment fragmentVisualizaAT;
    Fragment fragmentFotosAT;
    FragmentTransaction transaction;
    AtractivoTuristico atractivoTuristico;
    ArrayList<Imagen> imagenes;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    creaFragmentViualizacionAT();
                    return true;
                case R.id.navigation_dashboard:
                    creaFragmentComentarios();
                    return true;
                case R.id.navigation_notifications:
                    creaFragmentFotos();
                    return true;

            }
            return false;
        }
    };

    public void performStreamClick(){
        navigation.setSelectedItemId(R.id.navigation_dashboard);
    }

    public void iniciaFragment(){
        Bundle args = new Bundle();
        // envio el atractivo turistico serialzable al fragment
        args.putSerializable("atractivoTuristico", atractivoTuristico);
        args.putSerializable("imagenes",imagenes);

        fragmentVisualizaAT=new VisualizarAtractivoTuristicoFragment();
        fragmentVisualizaAT.setArguments(args);
        transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayoutFragmentVisualizarAtractivoTuristico,fragmentVisualizaAT); // give your fragment container id in first parameter
        transaction.commit();

        fragmentComentariosAT=new ComentariosATFrafment();
        fragmentComentariosAT.setArguments(args);

        fragmentFotosAT=new FotosATFragment();
        fragmentFotosAT.setArguments(args);
    }



    public void creaFragmentViualizacionAT(){
        transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayoutFragmentVisualizarAtractivoTuristico,fragmentVisualizaAT); // give your fragment container id in first parameter
        //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
        //finish();
    }
    public void creaFragmentComentarios(){
        Bundle args = new Bundle();
        // envio el atractivo turistico serialzable al fragment
        args.putSerializable("atractivoTuristico", atractivoTuristico);
        args.putSerializable("imagenes",imagenes);

        transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayoutFragmentVisualizarAtractivoTuristico,fragmentComentariosAT); // give your fragment container id in first parameter
        //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
        //finish();
    }
    public void creaFragmentFotos(){
        Bundle args = new Bundle();
        // envio el atractivo turistico serialzable al fragment
        args.putSerializable("atractivoTuristico", atractivoTuristico);
        args.putSerializable("imagenes",imagenes);

        transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayoutFragmentVisualizarAtractivoTuristico,fragmentFotosAT); // give your fragment container id in first parameter
        //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
        //finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_atractivo_turistico);
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();

        int contadorVisualizaciones=Integer.valueOf(atractivoTuristico.getContadorVisualizaciones())+1;
        atractivoTuristico.setContadorVisualizaciones(String.valueOf(contadorVisualizaciones));
        //textViewVisualizaciones.setText(String.valueOf(contadorVisualizaciones));
        mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).child(Referencias.CONTADORVISUALIZACIONES).setValue(String.valueOf(contadorVisualizaciones));


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        iniciaFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
