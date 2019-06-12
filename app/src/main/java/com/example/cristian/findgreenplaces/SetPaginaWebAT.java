package com.example.cristian.findgreenplaces;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Color;
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

        import java.util.ArrayList;

        import Clases.AtractivoTuristico;
        import Clases.Contribucion;
        import Clases.IdUsuario;
        import Clases.Imagen;
        import Clases.Referencias;
        import Clases.Usuario;

public class SetPaginaWebAT extends AppCompatActivity {
    Button buttonAceptar;
    Button buttonCancelar;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    AtractivoTuristico atractivoTuristico;
    ArrayList<Imagen> imagenes;
    EditText editTextPagina;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pagina_web_at);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Editar Página Web");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        buttonAceptar=findViewById(R.id.buttonAceptar);
        buttonCancelar=findViewById(R.id.buttonCancelar);
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));
        editTextPagina=findViewById(R.id.editTextPagina);
        if(!atractivoTuristico.getTelefono().equals("")){
            editTextPagina.setText(atractivoTuristico.getPaginaWeb());
        }
        aceptar();
        cancelar();
    }

    public void aceptar(){
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextPagina.getText().equals("")){
                    atractivoTuristico.setPaginaWeb(editTextPagina.getText().toString());
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                }
                atractivoTuristico.setPaginaWeb(editTextPagina.getText().toString());
                Contribucion contribucion=new Contribucion("",atractivoTuristico.getId(),IdUsuario.getIdUsuario(),Referencias.PAGINAWEB,editTextPagina.getText().toString());

                setResult(RESULT_OK,
                        new Intent().putExtra("nombre", atractivoTuristico.getPaginaWeb())
                                .putExtra("contribucion",contribucion));
                finish();
            }
        });
    }

    public void cancelar(){
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SetPaginaWebAT.this)
                        .setTitle("Información")
                        .setMessage("Seguro que quieres cancelar?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
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

