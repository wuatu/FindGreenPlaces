package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;

import Clases.AtractivoTuristico;
import Clases.CurrentDate;
import Clases.IdUsuario;
import Clases.Imagen;
import Clases.Referencias;

public class SubirFoto extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    StorageReference mStorageReference;
    AtractivoTuristico atractivoTuristico;
    ImageView imageViewFotoPerfil;
    String uris;
    Uri uri;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_foto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mStorageReference=FirebaseStorage.getInstance().getReference();
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();

        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        uris= ((String) getIntent().getStringExtra("imagen"));
        uri=uri.parse(uris);
        imageViewFotoPerfil =findViewById(R.id.imageViewFotoPerfil);
        Log.v("descargar",uri.toString());
        if(uri!=null){
            Log.v("descargar",uri.toString());
            Glide.with(SubirFoto.this)
                    .load(uri.toString())
                    .fitCenter()
                    .into(imageViewFotoPerfil);
        }
        Button aceptar=findViewById(R.id.buttonAceptar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(SubirFoto.this);
                progressDialog.setTitle("Subir Foto");
                progressDialog.setMessage("Subiendo Foto...");
                progressDialog.setCancelable(true);
                progressDialog.show();
                final StorageReference direccion=mStorageReference.child("fotos").child(uri.getLastPathSegment());
                Task<Uri> urlTask = direccion.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return direccion.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            //obtengo la url de firebase
                            Uri downloadUri = task.getResult();
                            String urlImagen = downloadUri.toString();

                            DatabaseReference databaseReference=mDatabase.child(Referencias.IMAGENES).child(atractivoTuristico.getId()).push();
                            String key =databaseReference.getKey();
                            Date c = Calendar.getInstance().getTime();
                            String fecha=CurrentDate.CurrentDate(c);
                            Imagen imagen=new Imagen(key,urlImagen,fecha,IdUsuario.getIdUsuario(),IdUsuario.getNombreUsuario()+" "+IdUsuario.getApellidoUsuario(),"0","0");
                            databaseReference.setValue(imagen);
                            mDatabase.child(Referencias.IMAGENES).child(IdUsuario.getIdUsuario()).setValue(imagen);

                            Toast.makeText(SubirFoto.this,"La foto se subio exitosamente",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SubirFoto.this,"Error al subir foto",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        Button cancelar=findViewById(R.id.buttonCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SubirFoto.this)
                        .setTitle("Subir Imagen")
                        .setMessage("Esta seguro que quiere cancelar?")
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

    }

}
