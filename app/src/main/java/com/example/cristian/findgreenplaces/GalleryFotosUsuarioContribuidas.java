package com.example.cristian.findgreenplaces;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Clases.Utils.IdUsuario;
import Clases.Models.Imagen;
import Clases.Utils.Referencias;
import Clases.Models.SpacePhoto;

/**
 * Created by Chike on 2/12/2017.
 */

public class GalleryFotosUsuarioContribuidas extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase= database.getReference();
    ArrayList<Imagen> imagenes=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView) toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Fotos Contribuidas");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        mDatabase.child(Referencias.IMAGENES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imagenes.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        Imagen imagen = dataSnapshot2.getValue(Imagen.class);
                        if(imagen.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                            if(imagen.getIdUsuario()!=null) {
                                if (imagen.getIdUsuario().equalsIgnoreCase(IdUsuario.idUsuario) && imagen.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                                    imagenes.add(imagen);
                                }
                            }

                        }
                    }
                    SpacePhoto[] getSpacePhotos=new SpacePhoto[imagenes.size()];

                    int i=0;
                    for(Imagen imagen:imagenes){
                        if(imagen.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                            SpacePhoto a = new SpacePhoto(imagen.getUrl(), imagen.getId().toString());
                            getSpacePhotos[i] = a;
                            i++;
                        }
                    }
                    Log.v("taza",String.valueOf(getSpacePhotos.length));
                    GalleryFotosUsuarioContribuidas.ImageGalleryAdapter adapter = new GalleryFotosUsuarioContribuidas.ImageGalleryAdapter(GalleryFotosUsuarioContribuidas.this, getSpacePhotos);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*mDatabase.child(Referencias.IMAGENES).child(IdUsuario.getIdUsuario()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SpacePhoto[] getSpacePhotos;
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Imagen imagen=dataSnapshot1.getValue(Imagen.class);
                    imagenes.add(imagen);
                }
                getSpacePhotos=new SpacePhoto[imagenes.size()];

                int i=0;
                for(Imagen imagen:imagenes){
                    if(imagen.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                        SpacePhoto a = new SpacePhoto(imagen.getUrl(), imagen.getId().toString());
                        getSpacePhotos[i] = a;
                        i++;
                    }
                }
                Log.v("taza",String.valueOf(getSpacePhotos.length));
                SpaceGalleryActivity.ImageGalleryAdapter adapter = new SpaceGalleryActivity.ImageGalleryAdapter(SpaceGalleryActivity.this, getSpacePhotos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }

    public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the layout
            View photoView = inflater.inflate(R.layout.item_photo, parent, false);

            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {
            final SpacePhoto spacePhoto = mSpacePhotos[position];
            ImageView imageView = holder.mPhotoImageView;

            CircularProgressDrawable circularProgressDrawable=new CircularProgressDrawable(GalleryFotosUsuarioContribuidas.this);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();

            Glide.with(mContext)
                    .load(spacePhoto.getUrl())
                    .placeholder(circularProgressDrawable)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.v("fotoError",spacePhoto.getTitle());
                            Log.v("fotoError",spacePhoto.getUrl());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (mSpacePhotos.length);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    SpacePhoto spacePhoto = mSpacePhotos[position];

                    Intent intent = new Intent(mContext, SpacePhotoActivity.class);
                    intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
                    intent.putExtra("imagen",imagenes.get(position));
                    startActivity(intent);
                }
            }
        }

        private SpacePhoto[] mSpacePhotos;
        private Context mContext;

        public ImageGalleryAdapter(Context context, SpacePhoto[] spacePhotos) {
            mContext = context;
            mSpacePhotos = spacePhotos;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
