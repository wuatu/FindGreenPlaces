package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import Clases.Utils.CurrentDate;
import Clases.Utils.IdUsuario;
import Clases.Models.Imagen;
import Clases.Utils.Referencias;
import Clases.Utils.SubirPuntos;

public class SubirFoto extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    StorageReference mStorageReference;
    String atractivoTuristico;
    ImageView imageViewFotoPerfil;
    String uris;
    Uri uri;
    private ProgressDialog progressDialog;
    StorageReference direccion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_foto);

        mStorageReference=FirebaseStorage.getInstance().getReference();
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Subir Foto");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        atractivoTuristico= ((String) getIntent().getStringExtra("atractivoTuristico"));

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
                direccion=mStorageReference.child("fotos").child(uri.getLastPathSegment());
                Task<Uri> urlTask = direccion.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (!task.isSuccessful()) {

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

                            DatabaseReference databaseReference=mDatabase.child(Referencias.IMAGENES).child(atractivoTuristico).push();
                            String key =databaseReference.getKey();
                            Date c = Calendar.getInstance().getTime();
                            String fecha=CurrentDate.CurrentDate(c);
                            Imagen imagen=new Imagen(key,urlImagen,fecha,atractivoTuristico,IdUsuario.getIdUsuario(),IdUsuario.getNombreUsuario()+" "+IdUsuario.getApellidoUsuario(),"0","0","0",Referencias.VISIBLE);
                            databaseReference.setValue(imagen);
                            mDatabase.child("imagenesContribuidas").child(IdUsuario.getIdUsuario()).child(key).setValue(imagen);

                            Toast.makeText(SubirFoto.this,"La foto se subio exitosamente",Toast.LENGTH_SHORT).show();
                            SubirPuntos.subirPuntosUsuarioQueContribuye(SubirFoto.this,1);
                           // Toast.makeText(SubirFoto.this,"Subes "+1+" punto",Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK,
                                    new Intent().putExtra("imagen", imagen));
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
