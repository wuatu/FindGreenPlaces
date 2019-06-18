package Clases;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.example.cristian.findgreenplaces.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class SubirPuntos {



    //sube puntos a usuario que contribuye
    public static void subirPuntosUsuarioQueContribuye(final Activity activity, final int puntosAdquiridos) {
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
        //subir puntos
        final DatabaseReference databaseReference1=mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario());
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                verificaNivelYAumentaPuntos(activity,usuario,puntosAdquiridos);
                int contribuciones=Integer.valueOf(usuario.getContribuciones())+1;
                usuario.setContribuciones(String.valueOf(contribuciones));
                databaseReference1.setValue(usuario);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    public static void verificaNivelYAumentaPuntos(final Activity activity, Usuario usuario, int puntosAdquiridos){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
        final DatabaseReference databaseReference1= mDatabase.child(Referencias.USUARIO).child(usuario.getId());
        int puntos=Integer.valueOf(usuario.getPuntos())+puntosAdquiridos;
        if(puntos>=100){
            int nivel= Integer.valueOf(usuario.getNivel());
            if(nivel==0){
                new AlertDialog.Builder(activity)
                        .setTitle("Subes de Nivel")
                        .setMessage("Subes a Nivel 1 Principiante" )
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        })
                        .setIcon(R.drawable.medallabronce)
                        .show();
                usuario.setNivel("2");
            }
            if(nivel==1){
                new AlertDialog.Builder(activity)
                        .setTitle("Subes de Nivel")
                        .setMessage("Subes a Nivel 2 Avanzado" )
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();

                            }
                        })
                        .setIcon(R.drawable.medallaplata)
                        .show();
                usuario.setNivel("2");
            }
            if(nivel==2){new AlertDialog.Builder(activity)
                    .setTitle("Subes de Nivel")
                    .setMessage("Subes a Nivel 3 experto" )
                    //.setIcon(R.drawable.aporte)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    })
                    .setIcon(R.drawable.medallaoro)
                    .show();
                usuario.setNivel("3");
            }
        }else{
            usuario.setPuntos(String.valueOf(puntos));
            databaseReference1.setValue(usuario);
        }
    }

    public static void verificaNivelYDisminuyePuntos(Activity activity, Usuario usuario, int puntosEliminados){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
        final DatabaseReference databaseReference1= mDatabase.child(Referencias.USUARIO).child(usuario.getId());
        int puntos=Integer.valueOf(usuario.getPuntos());
        if(puntos>=0){
            puntos=puntos-puntosEliminados;
            if(puntos<0){
                int nivel= Integer.valueOf(usuario.getNivel());
                if(nivel==0){
                    new AlertDialog.Builder(activity)
                            .setTitle("Bajas de Nivel")
                            .setMessage("Bajas a Nivel 1 Principiante" )
                            //.setIcon(R.drawable.aporte)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(R.drawable.medallabronce)
                            .show();
                    usuario.setNivel("2");
                }
                if(nivel==1){
                    new AlertDialog.Builder(activity)
                            .setTitle("Bajas de Nivel")
                            .setMessage("Bajas a Nivel 2 Avanzado" )
                            //.setIcon(R.drawable.aporte)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(R.drawable.medallaplata)
                            .show();
                    usuario.setNivel("2");
                }
                if(nivel==2){new AlertDialog.Builder(activity)
                        .setTitle("Bajas de Nivel")
                        .setMessage("Bajas a Nivel 3 experto" )
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.medallaoro)
                        .show();
                    usuario.setNivel("3");
                }
            }else{
                usuario.setPuntos(String.valueOf(puntos));
                databaseReference1.setValue(usuario);
            }
        }
    }

    //Consulta disminuye 1 punto cada vez que un usuario quita el "me gusta"
    public static void disminuyePuntosOtrosUsuarios(final Activity activity, String idUsuario,final int puntosEliminados){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
        final DatabaseReference databaseReference1= mDatabase.child(Referencias.USUARIO).child(idUsuario);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                verificaNivelYDisminuyePuntos(activity,usuario,puntosEliminados);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Consulta aumenta 1 punto cada vez que un usuario agrega el "me gusta"
    public static void aumentaPuntosOtrosUsuarios(final Activity activity, String idUsuario, final int puntosObtenidos){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
        final DatabaseReference databaseReference1= mDatabase.child(Referencias.USUARIO).child(idUsuario);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                verificaNivelYAumentaPuntos(activity,usuario,puntosObtenidos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
