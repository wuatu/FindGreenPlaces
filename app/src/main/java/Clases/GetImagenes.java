package Clases;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.cristian.findgreenplaces.DialogoVisualizarAtractivoTuristico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import Interfaz.OnGetDataListenerImagenes;

public class GetImagenes implements Serializable {
    ArrayList<Imagen> imagenes;
    DatabaseReference mDatabase2;
    FirebaseDatabase database2;

    public GetImagenes() {
    }

    public GetImagenes(ArrayList<Imagen> imagenes, DatabaseReference mDatabase2, FirebaseDatabase database2) {
        this.imagenes = imagenes;
        this.mDatabase2 = mDatabase2;
        this.database2 = database2;
    }

    public ArrayList<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public DatabaseReference getmDatabase2() {
        return mDatabase2;
    }

    public void setmDatabase2(DatabaseReference mDatabase2) {
        this.mDatabase2 = mDatabase2;
    }

    public FirebaseDatabase getDatabase2() {
        return database2;
    }

    public void setDatabase2(FirebaseDatabase database2) {
        this.database2 = database2;
    }

    /*public void buscaImagenes(AtractivoTuristico atractivoTuristico){
                item_photo=new ArrayList<>();
                database=FirebaseDatabase.getInstance();
                mDatabase=database.getReference();
                Query q=mDatabase.child(Referencias.IMAGENES).child(atractivoTuristico.getId());
                Log.v("oooh",q.getRef().toString());
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            Imagen imagen=dataSnapshot1.getValue(Imagen.class);
                            item_photo.add(imagen);
                            Log.v("bkn",String.valueOf(item_photo.size()));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }*/
    public void getImagenesAtractivoTuristico(final AtractivoTuristico atractivoTuristico, final OnGetDataListenerImagenes listener){
        listener.onStart();
        imagenes=new ArrayList<>();
        database2=FirebaseDatabase.getInstance();
        mDatabase2=database2.getReference();
        Query q2=mDatabase2.child(Referencias.IMAGENES).child(atractivoTuristico.getId());
        Log.v("oooooh",q2.getRef().toString());
        q2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Imagen imagen=dataSnapshot1.getValue(Imagen.class);
                    imagenes.add(imagen);
                    Log.v("bkn",String.valueOf(imagenes.size()));
                }
                listener.onSuccess(imagenes,atractivoTuristico);
                Log.v("bkn","asdasdad");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public ArrayList<Imagen> getImagenesAtractivoTuristicos(){
        return imagenes;
    }
}