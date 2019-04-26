package Clases;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.findgreenplaces.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Fragment.ComentariosATFrafment;

public class AdapterListViewComentarioAT extends BaseAdapter {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mDatabase=database.getReference();
    String comentarioMeGusta;
    Context context;
    ArrayList<Comentario> comentarios;
    AtractivoTuristico atractivoTuristico;
    public AdapterListViewComentarioAT(Context context, ArrayList<Comentario> comentarios, AtractivoTuristico atractivoTuristico){
        this.context=context;
        this.comentarios=comentarios;
        this.atractivoTuristico=atractivoTuristico;
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
        TextView comentario=(TextView)convertView.findViewById(R.id.textViewComentario);
        final TextView contadorLike=(TextView)convertView.findViewById(R.id.textViewNLike);
        final TextView contadorDislike=(TextView)convertView.findViewById(R.id.textViewNDislike);


        nombre.setText(comentarios.get(position).getNombreUsuario());
        apellido.setText(comentarios.get(position).getApellidoUsuario());
        comentario.setText(comentarios.get(position).getComentario());
        contadorLike.setText(comentarios.get(position).getContadorLike());
        contadorDislike.setText(comentarios.get(position).getContadorDislike());

        final ImageView imageViewLike=(ImageView)convertView.findViewById(R.id.imageViewLike);
        final ImageView imageViewDislike=(ImageView)convertView.findViewById(R.id.imageViewDislike);


        //consulta para saber si el usuario dio like a un comentario
        ComentarioMeGusta comentarioMeGusta;
        Query q=mDatabase.child(Referencias.COMENTARIOMEGUSTA).
                child(IdUsuario.getIdUsuario()).
                child(atractivoTuristico.getId());

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    Log.v("leyla", dataSnapshot1.getValue().toString());
                        ComentarioMeGusta comentarioMeGusta = dataSnapshot1.getValue(ComentarioMeGusta.class);

                        String meGustaComentario = comentarioMeGusta.getMeGustaComentario();
                        String idComentario=comentarioMeGusta.getIdComentario();

                        if (meGustaComentario.equals(Referencias.MEGUSTA) && idComentario.equals(comentarios.get(position).getId())) {
                            imageViewLike.setImageResource(R.drawable.likeon);
                            imageViewLike.setTag("likeon");
                        }
                        if (meGustaComentario.equals(Referencias.NOMEGUSTA)&& idComentario.equals(comentarios.get(position).getId())) {
                            imageViewDislike.setImageResource(R.drawable.dislikeon);
                            imageViewDislike.setTag("dislikeon");
                        }
                    }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageViewLike.getTag().toString().equals("likeon")) {
                    imageViewLike.setImageResource(R.drawable.likeoff);
                    imageViewLike.setTag("likeoff");
                    disminuyeContadorLikesYEliminaRegistro(position);
                }
                else {
                    if(imageViewDislike.getTag().toString().equals("dislikeon")) {
                        imageViewDislike.setImageResource(R.drawable.dislikeoff);
                        imageViewDislike.setTag("dislikeoff");
                        disminuyeContadorDislikesYEliminaRegistro(position);
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

                    imageViewLike.setImageResource(R.drawable.likeon);
                    imageViewLike.setTag("likeon");
                }
                //Toast.makeText(AdapterListViewComentarioAT.this.context, imageViewLike.getTag().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        imageViewDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageViewDislike.getTag().toString().equals("dislikeon")) {
                    imageViewDislike.setImageResource(R.drawable.dislikeoff);
                    imageViewDislike.setTag("dislikeoff");
                    disminuyeContadorDislikesYEliminaRegistro(position);

                }
                else{
                    if(imageViewLike.getTag().toString().equals("likeon")) {
                        imageViewLike.setImageResource(R.drawable.likeoff);
                        imageViewLike.setTag("likeoff");
                        disminuyeContadorLikesYEliminaRegistro(position);
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


                    imageViewDislike.setImageResource(R.drawable.dislikeon);
                    imageViewDislike.setTag("dislikeon");
                }

            }
        });
        return convertView;
    }

    public void disminuyeContadorLikesYEliminaRegistro(final int position){
        //cambio el contador de like de la base de datos y del objeto
                   int a=(Integer.parseInt(comentarios.get(position).getContadorLike())-1);
                    comentarios.get(position).setContadorLike(String.valueOf(a));
                    mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).child(atractivoTuristico.getId()).
                            child(comentarios.get(position).getId()).child(Referencias.CONTADORLIKE).setValue(comentarios.get(position).getContadorLike());

                    //eliminar registro me gusta comentario
                    mDatabase.child(Referencias.COMENTARIOMEGUSTA).
                            child(IdUsuario.getIdUsuario()).
                            child(atractivoTuristico.getId()).
                            child(comentarios.get(position).getId()).removeValue();
    }
    public void disminuyeContadorDislikesYEliminaRegistro(final int position) {

        //cambio el contador de like de la base de datos y del objeto
        int a = (Integer.parseInt(comentarios.get(position).getContadorDislike()) - 1);
        comentarios.get(position).setContadorDislike(String.valueOf(a));
        mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).child(atractivoTuristico.getId()).
                child(comentarios.get(position).getId()).child(Referencias.CONTADORDISLIKE).setValue(comentarios.get(position).getContadorDislike());

        //eliminar registro me gusta comentario
        mDatabase.child(Referencias.COMENTARIOMEGUSTA).
                child(IdUsuario.getIdUsuario()).
                child(atractivoTuristico.getId()).
                child(comentarios.get(position).getId()).removeValue();
    }
}

