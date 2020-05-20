package Clases.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cristian.findgreenplaces.DialogoReportarComentario;
import com.example.cristian.findgreenplaces.PerfilUsuario;
import com.example.cristian.findgreenplaces.PerfilUsuarioOtro;
import com.example.cristian.findgreenplaces.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import Clases.Models.AtractivoTuristico;
import Clases.Models.Comentario;
import Clases.Models.ComentarioMeGusta;
import Clases.Utils.IdUsuario;
import Clases.Utils.Referencias;
import Clases.Utils.SubirPuntos;

public class AdapterListViewComentarioAT extends BaseAdapter implements Serializable{
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mDatabase=database.getReference();
    String comentarioMeGusta;
    Context context;
    ArrayList<Comentario> comentarios;
    HashMap<String, ComentarioMeGusta> comentarioMeGustas;
    AtractivoTuristico atractivoTuristico;
    View view;

    int i=0;

    public AdapterListViewComentarioAT(FirebaseDatabase database, DatabaseReference mDatabase, String comentarioMeGusta, Context context, ArrayList<Comentario> comentarios, HashMap<String, ComentarioMeGusta> comentarioMeGustas, AtractivoTuristico atractivoTuristico, View view, int i) {
        this.database = database;
        this.mDatabase = mDatabase;
        this.comentarioMeGusta = comentarioMeGusta;
        this.context = context;
        this.comentarios = comentarios;
        this.comentarioMeGustas = comentarioMeGustas;
        this.atractivoTuristico = atractivoTuristico;
        this.view = view;
        this.i = i;
    }

    public AdapterListViewComentarioAT(Context context, ArrayList<Comentario> comentarios, AtractivoTuristico atractivoTuristico, HashMap<String,ComentarioMeGusta> comentarioMeGustas, View view){
        this.context=context;
        this.comentarios=comentarios;
        this.atractivoTuristico=atractivoTuristico;
        this.comentarioMeGustas=comentarioMeGustas;
        this.view= view;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    public String getComentarioMeGusta() {
        return comentarioMeGusta;
    }

    public void setComentarioMeGusta(String comentarioMeGusta) {
        this.comentarioMeGusta = comentarioMeGusta;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public HashMap<String, ComentarioMeGusta> getComentarioMeGustas() {
        return comentarioMeGustas;
    }

    public void setComentarioMeGustas(HashMap<String, ComentarioMeGusta> comentarioMeGustas) {
        this.comentarioMeGustas = comentarioMeGustas;
    }

    public AtractivoTuristico getAtractivoTuristico() {
        return atractivoTuristico;
    }

    public void setAtractivoTuristico(AtractivoTuristico atractivoTuristico) {
        this.atractivoTuristico = atractivoTuristico;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
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
        final LinearLayout linearLayoutNombreApellido=(LinearLayout)convertView.findViewById(R.id.linearLayoutNombreApellido);

        nombre.setText(comentarios.get(position).getNombreUsuario());
        apellido.setText(comentarios.get(position).getApellidoUsuario());
        comentario.setText(comentarios.get(position).getComentario());
        contadorLike.setText(comentarios.get(position).getContadorLike());
        contadorDislike.setText(comentarios.get(position).getContadorDislike());
        imageViewLike.setImageResource((comentarios.get(position).getImageViewLike()));
        imageViewDislike.setImageResource((comentarios.get(position).getImageViewDislike()));
        imageViewBotonPuntos.setTag(comentarios.get(position).getId());
        linearLayoutNombreApellido.setTag(comentarios.get(position).getId());
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
                    SubirPuntos.disminuyePuntosOtrosUsuarios((Activity) context,comentarios.get(position).getIdUsuario(),1);
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

                    SubirPuntos.aumentaPuntosOtrosUsuarios((Activity) context,comentarios.get(position).getIdUsuario(),1);

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
                        SubirPuntos.disminuyePuntosOtrosUsuarios((Activity) context,comentarios.get(position).getIdUsuario(),1);

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
        linearLayoutNombreApellido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comentario comentario1=comentarios.get(position);
                comentario1.getIdUsuario();
                Intent intent = new Intent(context, PerfilUsuarioOtro.class);
                intent.putExtra("comentario", (Comentario)comentario1);
                context.startActivity(intent);
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



}

