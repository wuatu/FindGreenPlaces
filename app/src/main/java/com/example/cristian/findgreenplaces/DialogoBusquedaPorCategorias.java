package com.example.cristian.findgreenplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogoBusquedaPorCategorias extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_por_categorias);
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

        LinearLayout linearLayoutAtractivoTuristico=findViewById(R.id.linearLayoutAtractivoTuristico);
        linearLayoutAtractivoTuristico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent().putExtra("busqueda", "Atractivo turístico"));
                finish();
            }
        });

        LinearLayout linearLayoutRestaurant=findViewById(R.id.linearLayoutRestaurant);
        linearLayoutRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent().putExtra("busqueda", "Restaurant"));
                finish();
            }
        });

        LinearLayout linearLayoutAlojamiento=findViewById(R.id.linearLayoutAlojamiento);
        linearLayoutAlojamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent().putExtra("busqueda", "Alojamiento"));
                finish();
            }
        });

        LinearLayout linearLayoutBalneario=findViewById(R.id.linearLayoutBalneario);
        linearLayoutBalneario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent().putExtra("busqueda", "Balneario"));
                finish();
            }
        });

        LinearLayout linearLayoutPanorama=findViewById(R.id.linearLayoutPanorama);
        linearLayoutPanorama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent().putExtra("busqueda", "Panorama"));
                finish();
            }
        });

        LinearLayout linearLayoutDeporte=findViewById(R.id.linearLayoutDeporte);
        linearLayoutDeporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent().putExtra("busqueda", "Deporte"));
                finish();
            }
        });

        LinearLayout linearLayoutMuseo=findViewById(R.id.linearLayoutMuseo);
        linearLayoutMuseo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent().putExtra("busqueda", "Museo"));
                finish();
            }
        });

        LinearLayout linearLayoutMonumentos=findViewById(R.id.linearLayoutMonumentos);
        linearLayoutMonumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent().putExtra("busqueda", "Monumentos"));
                finish();
            }
        });

        LinearLayout linearLayoutVerTodo=findViewById(R.id.linearLayoutVerTodo);
        linearLayoutVerTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent().putExtra("busqueda", "Ver todo"));
                finish();
            }
        });
    }
}
