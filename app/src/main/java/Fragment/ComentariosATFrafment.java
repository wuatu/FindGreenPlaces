package Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cristian.findgreenplaces.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import Clases.AdapterListViewComentarioAT;
import Clases.AtractivoTuristico;
import Clases.Comentario;
import Clases.ComentarioMeGusta;
import Clases.IdUsuario;
import Clases.Imagen;
import Clases.Referencias;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ComentariosATFrafment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ComentariosATFrafment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComentariosATFrafment extends android.app.Fragment implements View.OnClickListener {
    ArrayList<Imagen> imagenes;
    AtractivoTuristico atractivoTuristico;
    private ListView lista;
    private Adapter adapter;
    ArrayList<Comentario> model;
    View view;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    HashMap<String,ComentarioMeGusta> comentarioMeGustas=new HashMap<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ComentariosATFrafment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComentariosATFrafment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComentariosATFrafment newInstance(String param1, String param2) {
        ComentariosATFrafment fragment = new ComentariosATFrafment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        atractivoTuristico = ((AtractivoTuristico)getArguments().getSerializable("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getArguments().getSerializable("imagenes"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_comentarios_at, container, false);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        lista=view.findViewById(R.id.ma_lv_lista);
        model=new ArrayList<>();
        lista=(ListView)view.findViewById(R.id.ma_lv_lista);

        //imagenLikeOn
        mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).child(atractivoTuristico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.removeAll(model);
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    model.add(dataSnapshot1.getValue(Comentario.class));
                }
                //consulta para saber si el usuario dio like a un comentario
                ComentarioMeGusta comentarioMeGusta;
                Query q=mDatabase.child(Referencias.COMENTARIOMEGUSTA).
                        child(IdUsuario.getIdUsuario()).
                        child(atractivoTuristico.getId());
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {

                            ComentarioMeGusta comentarioMeGusta = dataSnapshot1.getValue(ComentarioMeGusta.class);
                            comentarioMeGustas.put(comentarioMeGusta.getId(),comentarioMeGusta);
                        }

                        for ( Comentario comentario : model)
                        {
                            //comentario.contexto(ComentariosATFrafment.this.getActivity());
                            comentario.setImageViewLike(R.drawable.likeoff);
                            comentario.setImageViewDislike(R.drawable.dislikeoff);
                            //comentario.getImageViewDislike().setTag(R.drawable.dislikeoff);
                            if(comentarioMeGustas.get(comentario.getId())!=null){
                                if(comentarioMeGustas.get(comentario.getId()).getMeGustaComentario().equals(Referencias.MEGUSTA)) {
                                    /*comentario.getImageViewLike().setImageResource(R.drawable.likeon);
                                    comentario.getImageViewLike().setTag(R.drawable.likeon);
                                    comentario.getImageViewDislike().setImageResource(R.drawable.dislikeoff);
                                    comentario.getImageViewDislike().setTag(R.drawable.dislikeoff);*/
                                    comentario.setImageViewLike(R.drawable.likeon);
                                    comentario.setImageViewDislike(R.drawable.dislikeoff);
                                }
                                if(comentarioMeGustas.get(comentario.getId()).getMeGustaComentario().equals(Referencias.NOMEGUSTA)) {
                                    /*comentario.getImageViewDislike().setImageResource(R.drawable.dislikeon);
                                    comentario.getImageViewDislike().setTag(R.drawable.dislikeon);
                                    comentario.getImageViewLike().setImageResource(R.drawable.likeoff);
                                    comentario.getImageViewLike().setTag(R.drawable.likeoff);*/
                                    comentario.setImageViewLike(R.drawable.likeoff);
                                    comentario.setImageViewDislike(R.drawable.dislikeon);
                                }

                            }

                        }
                        adapter=new AdapterListViewComentarioAT(getActivity(),model,atractivoTuristico,comentarioMeGustas,view);
                        lista.setAdapter((ListAdapter) adapter);
                        Log.v("taza3",String.valueOf(model.size()));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void setImageViewLike(){

    }







    @Override
    public void onClick(View v) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
