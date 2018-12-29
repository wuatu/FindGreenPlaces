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

import java.util.ArrayList;

import Interfaz.OnGetDataListenerImagenes;

public class GetImagenes {
    ArrayList<Imagen> imagenes;
    DatabaseReference mDatabase2;
    FirebaseDatabase database2;

    public GetImagenes() {
    }

    /*public void buscaImagenes(AtractivoTuristico atractivoTuristico){
        imagenes=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        Query q=mDatabase.child(Referencias.IMAGENES).child(atractivoTuristico.getId());
        Log.v("oooh",q.getRef().toString());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Imagen imagen=dataSnapshot1.getValue(Imagen.class);
                    imagenes.add(imagen);
                    Log.v("bkn",String.valueOf(imagenes.size()));
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