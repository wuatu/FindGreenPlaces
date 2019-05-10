package com.example.cristian.findgreenplaces;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.IdUsuario;
import Clases.Imagen;
import Clases.MeGustaImagen;
import Clases.Referencias;
import Clases.SpacePhoto;

/**
 * Created by Chike on 2/12/2017.
 */

public class SpacePhotoActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    FirebaseDatabase database;

    public static final String EXTRA_SPACE_PHOTO = "SpacePhotoActivity.SPACE_PHOTO";

    private ImageView mImageView;
    AtractivoTuristico atractivoTuristico;
    Imagen imagen;

    TextView textViewNombre;
    TextView textViewFecha;
    TextView textViewNLike;
    TextView textViewVisualizaciones;
    ImageView imageViewLike;
    LinearLayout linearLayoutLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();

        textViewNombre=findViewById(R.id.textViewNombre);
        textViewFecha=findViewById(R.id.textViewFecha);
        textViewNLike=findViewById(R.id.textViewNLike);
        textViewVisualizaciones=findViewById(R.id.textViewVisualizacion);
        imageViewLike=findViewById(R.id.imageViewLike);
        linearLayoutLike=findViewById(R.id.linearLayoutLike);

        mImageView = (ImageView) findViewById(R.id.image);
        SpacePhoto spacePhoto = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);

        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagen=((Imagen)getIntent().getSerializableExtra("imagen"));

        textViewNombre.setText(IdUsuario.getNombreUsuario()+" "+IdUsuario.getApellidoUsuario());
        textViewFecha.setText(imagen.getFecha());
        textViewNLike.setText(imagen.getContadorLike());
        textViewVisualizaciones.setText(imagen.getContadorVisualizaciones());
        imageViewLike.setTag(R.drawable.likeoff);

        int contadorVisualizaciones=Integer.valueOf(imagen.getContadorVisualizaciones())+1;
        textViewVisualizaciones.setText(String.valueOf(contadorVisualizaciones));
        imagen.setContadorVisualizaciones(String.valueOf(contadorVisualizaciones));
        mDatabase.child(Referencias.IMAGENES).child(atractivoTuristico.getId()).child(imagen.getId()).setValue(imagen);

        //Consulta para saber si la imagen tiene un like del usuario
            mDatabase.child(Referencias.FOTOMEGUSTA).child(IdUsuario.getIdUsuario()).child(atractivoTuristico.getId()).child(imagen.getId()).child(Referencias.MEGUSTA).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String meGusta=dataSnapshot.getValue(String.class);
                    if(meGusta!=null){
                        if(meGusta.equals(Referencias.MEGUSTA)){
                            imageViewLike.setImageResource(R.drawable.likeon);
                            imageViewLike.setTag(R.drawable.likeon);
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Glide.with(this)
                .load(spacePhoto.getUrl())
                .asBitmap()
                .error(R.drawable.cargando)
                .listener(new RequestListener<String, Bitmap>() {

                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        onPalette(Palette.from(resource).generate());
                        mImageView.setImageBitmap(resource);

                        return false;
                    }

                    public void onPalette(Palette palette) {
                        if (null != palette) {
                            ViewGroup parent = (ViewGroup) mImageView.getParent().getParent();
                            parent.setBackgroundColor(palette.getDarkVibrantColor(Color.BLACK));
                        }
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);
        linearLayoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(imageViewLike.getTag().toString())==R.drawable.likeoff){
                    //Consulta para añadir un megusta a la imagen
                    DatabaseReference databaseReference=mDatabase.child(Referencias.FOTOMEGUSTA).child(IdUsuario.getIdUsuario()).child(atractivoTuristico.getId()).child(imagen.getId());
                    String key=databaseReference.getKey();
                    MeGustaImagen meGustaImagen=new MeGustaImagen(key,IdUsuario.getIdUsuario(),atractivoTuristico.getId(),imagen.getId(),Referencias.MEGUSTA);
                    databaseReference.setValue(meGustaImagen);
                    imageViewLike.setImageResource(R.drawable.likeon);
                    imageViewLike.setTag(R.drawable.likeon);
                    int contadorLike=Integer.valueOf(imagen.getContadorLike())+1;
                    textViewNLike.setText(String.valueOf(contadorLike));
                    imagen.setContadorLike(String.valueOf(contadorLike));
                    mDatabase.child(Referencias.IMAGENES).child(atractivoTuristico.getId()).child(imagen.getId()).setValue(imagen);
                }
                else{
                    mDatabase.child(Referencias.FOTOMEGUSTA).child(IdUsuario.getIdUsuario()).child(atractivoTuristico.getId()).child(imagen.getId()).removeValue();
                    imageViewLike.setImageResource(R.drawable.likeoff);
                    imageViewLike.setTag(R.drawable.likeoff);
                    int contadorLike=Integer.valueOf(imagen.getContadorLike());
                    if(contadorLike>0){
                        contadorLike=contadorLike-1;
                        textViewNLike.setText(String.valueOf(contadorLike));
                        imagen.setContadorLike(String.valueOf(contadorLike));
                        mDatabase.child(Referencias.IMAGENES).child(atractivoTuristico.getId()).child(imagen.getId()).setValue(imagen);
                    }

                }

            }
        });
    }

   /* private SimpleTarget target = new SimpleTarget<Bitmap>() {

        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

           onPalette(Palette.from(bitmap).generate());
           mImageView.setImageBitmap(bitmap);
        }

        public void onPalette(Palette palette) {
            if (null != palette) {
                ViewGroup parent = (ViewGroup) mImageView.getParent().getParent();
                parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
            }
        }
    };*/
}
