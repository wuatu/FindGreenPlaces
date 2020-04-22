package com.example.cristian.findgreenplaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import Clases.IdUsuario;
import Clases.Referencias;

public class FotoPerfil extends AppCompatActivity {
    private static String PREFS_KEY = "mispreferencias";
    private static String URL = "url";


    ImageView imageViewFotoPerfil;
    private static final int INTENT_EXTRA_IMAGES=1;
    private static final int GALLERY_INTENT=1;
    private ProgressDialog progressDialog;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    StorageReference mStorageReference;
    String urlImagen;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_perfil);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        //textView.setText("Foto de Perfil");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_focused));

        mStorageReference=FirebaseStorage.getInstance().getReference();
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        TextView textViewCambiarFotoPerfil=findViewById(R.id.textViewCambiarFotoPerfil);
        imageViewFotoPerfil=findViewById(R.id.imageViewFotoPerfil);
        progressDialog=new ProgressDialog(FotoPerfil.this);
        if(IdUsuario.getUrl().equals("")) {
            Glide.with(getApplicationContext())
                .load(R.drawable.com_facebook_profile_picture_blank_square)
                .fitCenter()
                .into(imageViewFotoPerfil);
        }else{
            Glide.with(getApplicationContext())
                    .load(IdUsuario.getUrl())
                    .fitCenter()
                    .into(imageViewFotoPerfil);

        }
        textViewCambiarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewFotoPerfil.setVisibility(View.VISIBLE);
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,INTENT_EXTRA_IMAGES);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
            progressDialog.setTitle("Subiendo Foto");
            progressDialog.setMessage("Subiendo Foto a Base de Datos!");
            progressDialog.setCancelable(true);
            progressDialog.show();
            //item_photo = data.getParcelableArrayListExtra("item_photo");
            //Uri[] uri=new Uri[item_photo.size()];
            Uri uri=data.getData();
            //for (int i =0 ; i < item_photo.size(); i++) {
            final StorageReference direccion=mStorageReference.child(IdUsuario.getIdUsuario()).child(uri.getLastPathSegment());
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
                        Uri downloadUri = task.getResult();
                        urlImagen = downloadUri.toString();
                        IdUsuario.setUrl(urlImagen);
                        guardarValorString(FotoPerfil.this,URL,urlImagen);
                        mDatabase.child(Referencias.USUARIO).
                                child(IdUsuario.getIdUsuario()).child("urlFotoPerfil").setValue(urlImagen);
                        Log.v("descargar",urlImagen);
                        Glide.with(FotoPerfil.this)
                                .load(urlImagen)
                                .fitCenter()
                                .into(imageViewFotoPerfil);
                        Toast.makeText(FotoPerfil.this,"La foto se subio exitosamente!",Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK,
                                new Intent().putExtra("imagen", urlImagen));
                        //finish();


                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
        //}
    }
    public static void guardarValorString(Context context, String keyPref, String valor) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(keyPref, valor);
        editor.commit();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
