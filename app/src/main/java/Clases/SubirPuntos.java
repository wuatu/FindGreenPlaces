package Clases;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.cristian.findgreenplaces.R;
import com.example.cristian.findgreenplaces.SetDescripcionAtractivoTuristico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SubirPuntos {
    Usuario usuario;
    DatabaseReference mDatabase;

    public SubirPuntos() {
    }

    public void SubirPuntos(final Activity activity) {
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
        //subir puntos
        final DatabaseReference databaseReference1=mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario());
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario=dataSnapshot.getValue(Usuario.class);
                int puntos=Integer.valueOf(usuario.getPuntos())+1;
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
                    activity.finish();

                }
                int contribuciones=Integer.valueOf(usuario.getContribuciones())+1;
                usuario.setContribuciones(String.valueOf(contribuciones));
                databaseReference1.setValue(usuario);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
