package Clases;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.findgreenplaces.DialogoReportarComentario;
import com.example.cristian.findgreenplaces.DialogoVisualizarAtractivoTuristico;
import com.example.cristian.findgreenplaces.MenuPrincipal;
import com.example.cristian.findgreenplaces.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import Fragment.ComentariosATFrafment;

public class AdapterListViewComentarioAT extends BaseAdapter implements Serializable{
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mDatabase=database.getReference();
    String comentarioMeGusta;
    Context context;
    ArrayList<Comentario> comentarios;
    HashMap<String,ComentarioMeGusta> comentarioMeGustas;
    AtractivoTuristico atractivoTuristico;
    View view;

    int i=0;
    public AdapterListViewComentarioAT(Context context, ArrayList<Comentario> comentarios, AtractivoTuristico atractivoTuristico,HashMap<String,ComentarioMeGusta> comentarioMeGustas,View view){
        this.context=context;
        this.comentarios=comentarios;
        this.atractivoTuristico=atractivoTuristico;
        this.comentarioMeGustas=comentarioMeGustas;
        this.view= view;
    }
    @Override
    public int getCount() {
        return comentarios.size();
    }

    @Override
    public Object getItem(int position) {
        return comentarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView =layoutInflater.inflate(R.layout.row_list_view_comentarios_at,null);

        }
        final TextView nombre=(TextView) convertView.findViewById(R.id.textViewNombre);
        TextView apellido=(TextView)convertView.findViewById(R.id.textViewApellido);
        final TextView comentario=(TextView)convertView.findViewById(R.id.textViewComentario);
        final TextView contadorLike=(TextView)convertView.findViewById(R.id.textViewNLike);
        final TextView contadorDislike=(TextView)convertView.findViewById(R.id.textViewNDislike);

        final ImageView imageViewLike=(ImageView)convertView.findViewById(R.id.imageViewLike);
        final ImageView imageViewDislike=(ImageView)convertView.findViewById(R.id.imageViewDislike);
        final ImageView imageViewBotonPuntos=(ImageView)convertView.findViewById(R.id.botonPuntos);

        nombre.setText(comentarios.get(position).getNombreUsuario());
        apellido.setText(comentarios.get(position).getApellidoUsuario());
        comentario.setText(comentarios.get(position).getComentario());
        contadorLike.setText(comentarios.get(position).getContadorLike());
        contadorDislike.setText(comentarios.get(position).getContadorDislike());
        imageViewLike.setImageResource((comentarios.get(position).getImageViewLike()));
        imageViewDislike.setImageResource((comentarios.get(position).getImageViewDislike()));
        imageViewBotonPuntos.setTag(comentarios.get(position).getId());

        //Log.v("leyla", comentarios.get(position).getImageViewLike().getTag()+"TTT");

            if (comentarioMeGustas.get(comentarios.get(position).getId()) != null) {
                ComentarioMeGusta comentarioMeGusta1 = comentarioMeGustas.get(comentarios.get(position).getId());
                String meGustaComentario = comentarioMeGusta1.getMeGustaComentario();
                String idComentario = comentarioMeGusta1.getIdComentario();

                if (meGustaComentario.equals(Referencias.MEGUSTA) && idComentario.equals(comentarios.get(position).getId())) {

                    imageViewLike.setImageResource(R.drawable.likeon);
                    imageViewLike.setTag(R.drawable.likeon);
                }
                if (meGustaComentario.equals(Referencias.NOMEGUSTA) && idComentario.equals(comentarios.get(position).getId())) {
                    imageViewDislike.setImageResource(R.drawable.dislikeon);
                    imageViewDislike.setTag(R.drawable.dislikeon);
                }
            }


        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("leyla", imageViewLike.getTag().toString()+"1");
                if(comentarios.get(position).getImageViewLike()==(R.drawable.likeon)) {
                    disminuyeContadorLikesYEliminaRegistro(position,contadorLike);
                    Log.v("leyla", imageViewLike.getTag().toString()+"2");

                    comentarios.get(position).setImageViewLike(R.drawable.likeoff);
                    comentarioMeGustas.remove(comentarios.get(position).getId());
                    imageViewLike.setTag(R.drawable.likeoff);
                    imageViewLike.setImageResource(R.drawable.likeoff);

                    comentarios.get(position).setImageViewDislike(R.drawable.dislikeoff);
                    imageViewDislike.setTag(R.drawable.dislikeoff);
                    imageViewDislike.setImageResource(R.drawable.dislikeoff);
                    disminuyePuntos(position);
                }
                else{
                    Log.v("leyla", imageViewLike.getTag().toString()+"3");
                    if(comentarios.get(position).getImageViewDislike()==(R.drawable.dislikeon)) {
                        disminuyeContadorDislikesYEliminaRegistro(position,contadorDislike);
                        comentarios.get(position).setImageViewDislike(R.drawable.dislikeoff);
                        imageViewDislike.setTag(R.drawable.dislikeoff);
                        imageViewDislike.setImageResource(R.drawable.dislikeoff);
                    }
                    //cambio el contador de like de la base de datos y del objeto
                    int a=(Integer.parseInt(comentarios.get(position).getContadorLike())+1);
                    comentarios.get(position).setContadorLike(String.valueOf(a));
                    contadorLike.setText(comentarios.get(position).getContadorLike());

                    mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).child(atractivoTuristico.getId()).
                            child(comentarios.get(position).getId()).child(Referencias.CONTADORLIKE).setValue(comentarios.get(position).getContadorLike());

                    DatabaseReference databaseReference=mDatabase.push();
                    String id=databaseReference.getKey();
                    ComentarioMeGusta comentarioMeGusta=new ComentarioMeGusta(comentarios.get(position).getId(), IdUsuario.getIdUsuario(),atractivoTuristico.getId(),comentarios.get(position).getId(),Referencias.MEGUSTA);

                    //add añadir registro me gusta comentario
                    mDatabase.child(Referencias.COMENTARIOMEGUSTA).
                            child(IdUsuario.getIdUsuario()).
                            child(atractivoTuristico.getId()).
                            child(comentarioMeGusta.getId()).
                            setValue(comentarioMeGusta);

                    comentarios.get(position).setImageViewLike(R.drawable.likeon);
                    imageViewLike.setTag(R.drawable.likeon);
                    imageViewLike.setImageResource(R.drawable.likeon);

                    aumentaPuntos(position);

                }
            }
        });

        imageViewDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comentarios.get(position).getImageViewDislike()==(R.drawable.dislikeon)) {
                    disminuyeContadorDislikesYEliminaRegistro(position,contadorDislike);
                    comentarios.get(position).setImageViewDislike(R.drawable.dislikeoff);
                    comentarioMeGustas.remove(comentarios.get(position).getId());
                    imageViewDislike.setTag(R.drawable.dislikeoff);
                    imageViewDislike.setImageResource(R.drawable.dislikeoff);

                }
                else{
                    if(comentarios.get(position).getImageViewLike()==(R.drawable.likeon)) {
                        disminuyeContadorLikesYEliminaRegistro(position,contadorLike);
                        comentarios.get(position).setImageViewLike(R.drawable.likeoff);
                        imageViewLike.setTag(R.drawable.likeoff);
                        imageViewLike.setImageResource(R.drawable.likeoff);
                        disminuyePuntos(position);
                    }

                    //cambio el contador de like de la base de datos y del objeto
                    int a=(Integer.parseInt(comentarios.get(position).getContadorDislike())+1);
                    comentarios.get(position).setContadorDislike(String.valueOf(a));
                    contadorDislike.setText(comentarios.get(position).getContadorDislike());
                    mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).child(atractivoTuristico.getId()).
                            child(comentarios.get(position).getId()).child(Referencias.CONTADORDISLIKE).setValue(comentarios.get(position).getContadorDislike());

                    DatabaseReference databaseReference=mDatabase.push();
                    String id=databaseReference.getKey();
                    ComentarioMeGusta comentarioMeGusta=new ComentarioMeGusta(comentarios.get(position).getId(), IdUsuario.getIdUsuario(),atractivoTuristico.getId(),comentarios.get(position).getId(),Referencias.NOMEGUSTA);

                    //add añadir registro me gusta comentario
                    mDatabase.child(Referencias.COMENTARIOMEGUSTA).
                            child(IdUsuario.getIdUsuario()).
                            child(atractivoTuristico.getId()).
                            child(comentarioMeGusta.getId()).
                            setValue(comentarioMeGusta);


                    comentarios.get(position).setImageViewDislike(R.drawable.dislikeon);

                    imageViewDislike.setTag(R.drawable.dislikeon);
                    imageViewDislike.setImageResource(R.drawable.dislikeon);



                }

            }
        });


        imageViewBotonPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //view.setBackgroundColor(context.getResources().getColor(R.color.com_facebook_button_login_silver_background_color_pressed));
                Intent intent = new Intent(context, DialogoReportarComentario.class);
                Comentario comentario1 =comentarios.get(position);
                intent.putExtra("comentario", (Comentario)comentario1);
              //intent.putExtra("position", position);
//                intent.putExtra("comentarios", comentarios);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                context.startActivity(intent);
                //view.setBackgroundColor(context.getResources().getColor(R.color.com_facebook_button_background_color_focused));
            }
        });
        return convertView;
    }

    public void disminuyeContadorLikesYEliminaRegistro(final int position,TextView contadorLike){
        //cambio el contador de like de la base de datos y del objeto
        if(Integer.parseInt(comentarios.get(position).getContadorLike())>0) {
            int a = (Integer.parseInt(comentarios.get(position).getContadorLike()) - 1);
            comentarios.get(position).setContadorLike(String.valueOf(a));
            contadorLike.setText(comentarios.get(position).getContadorLike());
            mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).child(atractivoTuristico.getId()).
                    child(comentarios.get(position).getId()).child(Referencias.CONTADORLIKE).setValue(comentarios.get(position).getContadorLike());

            //eliminar registro me gusta comentario
            mDatabase.child(Referencias.COMENTARIOMEGUSTA).
                    child(IdUsuario.getIdUsuario()).
                    child(atractivoTuristico.getId()).
                    child(comentarios.get(position).getId()).removeValue();

        }
    }
    public void disminuyeContadorDislikesYEliminaRegistro(final int position, TextView contadorDislike) {
        if(Integer.parseInt(comentarios.get(position).getContadorDislike())>0){
        //cambio el contador de like de la base de datos y del objeto
        int a = (Integer.parseInt(comentarios.get(position).getContadorDislike()) - 1);
        comentarios.get(position).setContadorDislike(String.valueOf(a));
        contadorDislike.setText(comentarios.get(position).getContadorDislike());
        mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).child(atractivoTuristico.getId()).
                child(comentarios.get(position).getId()).child(Referencias.CONTADORDISLIKE).setValue(comentarios.get(position).getContadorDislike());

        //eliminar registro me gusta comentario
       mDatabase.child(Referencias.COMENTARIOMEGUSTA).
                child(IdUsuario.getIdUsuario()).
                child(atractivoTuristico.getId()).
                child(comentarios.get(position).getId()).removeValue();
    }
    }

    //Consulta aumenta 1 punto cada vez que un usuario agrega el "me gusta" a una foto
    public void aumentaPuntos(int position){
        final DatabaseReference databaseReference1= mDatabase.child(Referencias.USUARIO).child(comentarios.get(position).getIdUsuario());
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                int puntos=Integer.valueOf(usuario.getPuntos())+1;
                if(puntos>=100){
                    int nivel= Integer.valueOf(usuario.getNivel());
                    if(nivel==1){
                        usuario.setNivel("2");
                        usuario.setPuntos("0");
                        usuario.setNombreNivel(Referencias.AVANZADO);
                    }
                    if(nivel==2){
                        usuario.setNivel("3");
                        usuario.setPuntos("0");
                        usuario.setNombreNivel(Referencias.EXPERTO);
                    }
                }else{
                    usuario.setPuntos(String.valueOf(puntos));
                }
                databaseReference1.setValue(usuario);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Consulta disminuye 1 punto cada vez que un usuario quita el "me gusta" a una foto
    public void disminuyePuntos(int position){

        final DatabaseReference databaseReference1= mDatabase.child(Referencias.USUARIO).child(comentarios.get(position).getIdUsuario());
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            usuario.setPuntos("99");
                            usuario.setNombreNivel(Referencias.AVANZADO);
                        }
                    }else{
                        usuario.setPuntos(String.valueOf(puntos));
                    }
                    databaseReference1.setValue(usuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

