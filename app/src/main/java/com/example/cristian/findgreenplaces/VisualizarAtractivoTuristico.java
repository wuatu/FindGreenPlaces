package com.example.cristian.findgreenplaces;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Placeholder;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.Imagen;
import Fragment.Informacion;

public class VisualizarAtractivoTuristico extends AppCompatActivity{

    private TextView mTextMessage;
    Fragment fragment;
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
                    creaFragmentViualizacionInicial2();
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    public void creaFragmentViualizacionInicial(){
        Bundle args = new Bundle();
        // envio el atractivo turistico serialzable al fragment
        args.putSerializable("atractivoTuristico", atractivoTuristico);
        args.putSerializable("imagenes",imagenes);
        fragment=new Informacion();
        fragment.setArguments(args);
        transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayoutFragmentVisualizarAtractivoTuristico,fragment); // give your fragment container id in first parameter
        //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
        //finish();
    }
    public void creaFragmentViualizacionInicial2(){
        transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayoutFragmentVisualizarAtractivoTuristico,fragment); // give your fragment container id in first parameter
        //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_atractivo_turistico);
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        //imagenes=new ArrayList();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        creaFragmentViualizacionInicial();
        //finish();
    }
    private void getImagenesAtractivoTuristico(){

        Query q=mDatabase.child("imagenes").child(atractivoTuristico.getId());
        Log.v("oooh",q.getRef().toString());

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Imagen imagen=dataSnapshot1.getValue(Imagen.class);
                    imagenes.add(imagen);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /**/
    }
}
