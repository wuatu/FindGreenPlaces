package Fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.cristian.findgreenplaces.InformacionAdicionalAT;
import com.example.cristian.findgreenplaces.R;
import com.example.cristian.findgreenplaces.SetCalificacionAtractivoTuristico;
import com.example.cristian.findgreenplaces.SetCategoriasAtractivoTuristico;
import com.example.cristian.findgreenplaces.SetDescripcionAtractivoTuristico;
import com.example.cristian.findgreenplaces.SugerirCambioAtractivoTuristico;
import com.example.cristian.findgreenplaces.VisualizarAtractivoTuristico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Clases.AdapterListViewComentarioAT;
import Clases.AtractivoTuristico;
import Clases.CalificacionPromedio;
import Clases.Categoria;
import Clases.Comentario;
import Clases.IdUsuario;
import Clases.Imagen;
import Clases.Referencias;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VisualizarAtractivoTuristicoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VisualizarAtractivoTuristicoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisualizarAtractivoTuristicoFragment extends Fragment implements View.OnClickListener {
    AtractivoTuristico atractivoTuristico;
    ArrayList<Categoria> categorias;
    private TextView titulo;
    private TextView textViewDescripcionAT;
    private TextView textViewSugerirCambio;
    private TextView textViewAñadirInformacionAdicional;
    private LinearLayout linearLayoutAñadirInformacionAdicional;
    private RatingBar ratingBar;
    private ImageView imageView;
    private TextView textViewratingBar;
    private TextView textViewOpiniones;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    ArrayList<Imagen> imagenes;
    View view;

    LinearLayout linearLayoutcategorias;
    TagView tagGroup;

    private ListView lista;
    private Adapter adapter;
    ArrayList<Comentario> model;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VisualizarAtractivoTuristicoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VisualizarAtractivoTuristicoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VisualizarAtractivoTuristicoFragment newInstance(String param1, String param2) {
        VisualizarAtractivoTuristicoFragment fragment = new VisualizarAtractivoTuristicoFragment();
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
        Log.v("oooh",String.valueOf(imagenes.size()));

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_visualizar_atractivo_turistico, container, false);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        linearLayoutAñadirInformacionAdicional=view.findViewById(R.id.linearLatyoutInformacionAdicional);
        textViewAñadirInformacionAdicional=view.findViewById(R.id.textViewAñadirInformacionAdicional);
        lista=view.findViewById(R.id.ma_lv_lista);
        model=new ArrayList<>();
        lista=(ListView)view.findViewById(R.id.ma_lv_lista);
        mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).child(atractivoTuristico.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.removeAll(model);
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    model.add(dataSnapshot1.getValue(Comentario.class));
                }
                adapter=new AdapterListViewComentarioAT(getActivity(),model);
                lista.setAdapter((ListAdapter) adapter);
                Log.v("taza3",String.valueOf(model.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        textViewSugerirCambio=(TextView) view.findViewById(R.id.textViewSugerirCambio);
        titulo = (TextView) view.findViewById(R.id.textViewTituloAT2);
        titulo.setText(atractivoTuristico.getNombre());
        textViewDescripcionAT =view.findViewById(R.id.textViewDescripcionAT);
        //textViewDescripcionAT.setText(atractivoTuristico.getDescripcion());
        imageView=view.findViewById(R.id.imageViewVAT);

        getImagenesAtractivoTuristico();
        textViewratingBar=view.findViewById(R.id.textViewRatingBar);
        textViewratingBar.setOnClickListener(this);
        ratingBar=view.findViewById(R.id.rating);

        linearLayoutcategorias=view.findViewById(R.id.linearLatyoutCategorias);
        tagGroup = (TagView)view.findViewById(R.id.tag_group2);

        textViewOpiniones=view.findViewById(R.id.textViewOpinionesn);
        mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                atractivoTuristico = dataSnapshot.getValue(AtractivoTuristico.class);
                textViewDescripcionAT.setText(atractivoTuristico.getDescripcion());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child(Referencias.CALIFICACIONPROMEDIO).child(atractivoTuristico.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CalificacionPromedio calificacionPromedio=dataSnapshot.getValue(CalificacionPromedio.class);
                if(calificacionPromedio!=null){
                    String calificacion=String.valueOf(calificacionPromedio.getPromedioCalificacion());
                    textViewratingBar.setText(calificacion.substring(0,3));
                    ratingBar.setRating(Float.parseFloat(calificacion));
                    textViewOpiniones.setText(String.valueOf(calificacionPromedio.getTotalPersonas()));
                    ratingBar.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child(Referencias.CATEGORIAATRACTIVOTURISTICO).child(atractivoTuristico.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tagGroup.removeAll();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Categoria categoria=dataSnapshot1.getValue(Categoria.class);
                    Tag tag=new Tag(categoria.getEtiqueta());
                    tagGroup.addTag(tag);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        textViewSugerirCambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizarAtractivoTuristicoFragment.this.getActivity(), SugerirCambioAtractivoTuristico.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);
            }
        });

        if(!atractivoTuristico.getTelefono().equals("")){
            creaInformacionAdicional("Teléfono",atractivoTuristico.getTelefono());
        }
        if(!atractivoTuristico.getHorarioDeAtencion().equals("")){
            creaInformacionAdicional("Horario de Atención",atractivoTuristico.getHorarioDeAtencion());
        }
        if(!atractivoTuristico.getPaginaWeb().equals("")){
            creaInformacionAdicional("Página Web",atractivoTuristico.getPaginaWeb());
        }
        if(!atractivoTuristico.getRedesSociales().equals("")){
            creaInformacionAdicional("Redes Sociales",atractivoTuristico.getRedesSociales());
        }

        textViewAñadirInformacionAdicional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizarAtractivoTuristicoFragment.this.getActivity(), InformacionAdicionalAT.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);
            }
        });
        return view;
    }


    public void creaInformacionAdicional(String titulo,String contenido){
        LinearLayout linearLayout=new LinearLayout(VisualizarAtractivoTuristicoFragment.this.getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView= new TextView(VisualizarAtractivoTuristicoFragment.this.getActivity());
        textView.setText(titulo);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(null, Typeface.BOLD);
        TextView textView1= new TextView(VisualizarAtractivoTuristicoFragment.this.getActivity());
        textView1.setText(contenido);
        linearLayout.addView(textView);
        linearLayout.addView(textView1);
        linearLayoutAñadirInformacionAdicional.addView(linearLayout);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewRatingBar:

            break;
        }
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
    private void getImagenesAtractivoTuristico(){
                Glide.with(VisualizarAtractivoTuristicoFragment.this)
                        .load(imagenes.get(0).getUrl())
                        .fitCenter()
                        .centerCrop()
                        .into(imageView);

    }
}
