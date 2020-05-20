package com.example.cristian.findgreenplaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Clases.Models.AtractivoTuristico;
import Clases.Models.Comentario;
import Clases.Utils.IdUsuario;
import Clases.Utils.Referencias;
import Clases.Models.Usuario;

public class DialogoReportarComentario extends AppCompatActivity {
    Comentario comentario;
    AtractivoTuristico atractivoTuristico;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogo_reportar_comentario);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
//        getSupportActionBar().hide();
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int ancho=dm.widthPixels;
        int alto=dm.heightPixels;
        getWindow().setLayout((int) (ancho*.8),LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        TextView textViewReportar=findViewById(R.id.textViewReportar);
        comentario= (Comentario) getIntent().getSerializableExtra("comentario");
        atractivoTuristico= (AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico");
//        position=(int) getIntent().getSerializableExtra("position");
        textViewReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogoReportarComentario.this,"El comentario ha sido reportado",Toast.LENGTH_SHORT).show();
                int reporte=Integer.valueOf(comentario.getContadorReportes())+1;

                //con 10 reportes el comentarios se elimina
                if(Integer.valueOf(comentario.getContadorReportes())>=10){
                    comentario.setVisible(Referencias.INVISIBLE);
                    comentario.setContadorReportes(String.valueOf(reporte));
                    mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).
                            child(atractivoTuristico.getId()).
                            child(comentario.getId()).setValue(comentario);
                }
                else{
                    comentario.setContadorReportes(String.valueOf(reporte));
                    mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).
                            child(atractivoTuristico.getId()).
                            child(comentario.getId()).setValue(comentario);
                }
                final DatabaseReference databaseReference=mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Usuario usuario=dataSnapshot.getValue(Usuario.class);
                        int puntos=Integer.valueOf(usuario.getPuntos());
                        if(puntos>0){
                            puntos=puntos-1;
                            if(puntos<0){
                                int nivel= Integer.valueOf(usuario.getNivel());
                                if(nivel==2){
                                    usuario.setNivel("1");
                                    usuario.setPuntos("99");
                                    usuario.setNombreNivel(Referencias.PRINCIPIANTE);

                                }
                                if(nivel==3){
                                    usuario.setNivel("2");
                                    usuario.setNivel("99");
                                    usuario.setNombreNivel(Referencias.AVANZADO);
                                }
                            }else{
                                usuario.setPuntos(String.valueOf(puntos));
                            }
                            databaseReference.setValue(usuario);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                finish();
            }
        });

    }
}
