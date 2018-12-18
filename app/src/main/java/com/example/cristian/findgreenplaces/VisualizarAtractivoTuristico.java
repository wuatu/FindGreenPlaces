package com.example.cristian.findgreenplaces;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Placeholder;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import Clases.AtractivoTuristico;
import Fragment.Informacion;

public class VisualizarAtractivoTuristico extends AppCompatActivity{

    private TextView mTextMessage;
    Fragment fragment;
    FragmentTransaction transaction;
    AtractivoTuristico atractivoTuristico;

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
        fragment=new Informacion();
        fragment.setArguments(args);
        transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayoutFragmentVisualizarAtractivoTuristico,fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
    public void creaFragmentViualizacionInicial2(){
        transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayoutFragmentVisualizarAtractivoTuristico,fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_atractivo_turistico);
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        creaFragmentViualizacionInicial();

    }
}
