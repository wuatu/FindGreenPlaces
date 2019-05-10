package Fragment;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Clases.AdapterListViewComentarioAT;
import Clases.AtractivoTuristico;
import Clases.AtractivoTuristicoMeGusta;
import Clases.CalificacionPromedio;
import Clases.Categoria;
import Clases.Comentario;
import Clases.ContadorMeGustaAtractivoTuristico;
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
public class VisualizarAtractivoTuristicoFragment extends Fragment implements View.OnClickListener, LocationListener {

    ArrayList<Categoria> categorias;
    private TextView titulo;
    private TextView textViewDescripcionAT;
    private TextView textViewSugerirCambio;
    private TextView textViewAñadirInformacionAdicional;
    private LinearLayout linearLayoutAñadirInformacionAdicional;
    private RatingBar ratingBar;
    private RatingBar ratingBar2;
    private ImageView imageView;
    //private TextView textViewratingBar;
    private TextView textViewratingBar2;
    //private TextView textViewOpiniones;
    private TextView textViewOpiniones2;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    ArrayList<Imagen> imagenes;
    AtractivoTuristico atractivoTuristico;
    View view;
    TextView textViewVerMasComentarios;
    Button botonCalificar;

    LinearLayout linearLayoutcategorias;
    TagView tagGroup;

    private ListView lista;
    private Adapter adapter;
    ArrayList<Comentario> model;
    ListView listView;
    Fragment fragmentComentariosAT;
    FragmentTransaction transaction;
    LinearLayout linearLayoutComentario;
    ImageView imageViewLike;
    LatLng currentLatLng;
    TextView textViewVisualizaciones;
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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_visualizar_atractivo_turistico, container, false);
        Toolbar toolbar=view.findViewById(R.id.toolbar_camera);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Información");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        linearLayoutAñadirInformacionAdicional=view.findViewById(R.id.linearLatyoutInformacionAdicional);
        textViewVisualizaciones=view.findViewById(R.id.textViewVisualizacion);
        textViewAñadirInformacionAdicional=view.findViewById(R.id.textViewAñadirInformacionAdicional);
        textViewVerMasComentarios=view.findViewById(R.id.textViewVerMasComentarios);
        linearLayoutComentario=view.findViewById(R.id.comentario);
        lista=view.findViewById(R.id.ma_lv_lista);
        model=new ArrayList<>();
        botonCalificar=view.findViewById(R.id.botonCalificar);

        int contadorVisualizaciones=Integer.valueOf(atractivoTuristico.getContadorVisualizaciones())+1;
        atractivoTuristico.setContadorVisualizaciones(String.valueOf(contadorVisualizaciones));
        textViewVisualizaciones.setText(String.valueOf(contadorVisualizaciones));
        mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).child(Referencias.CONTADORVISUALIZACIONES).setValue(String.valueOf(contadorVisualizaciones));

        //setListViewHeightBasedOnChildren(lista);
        ImageView imageViewIr=view.findViewById(R.id.imageViewIr);
        imageViewIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+atractivoTuristico.getLatitud()+","+atractivoTuristico.getLongitud()+"&travelmode=driving"));
                startActivity(intent);
            }
        });
//https://www.google.com/maps/dir/?api=1&origin=47.5951518,-122.3316393&destination=47.5951518,-112.3316393&travelmode=driving
        mDatabase.child(Referencias.ATRACTIVOTURISTICOESCOMENTADOPORUSUARIO).child(atractivoTuristico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.removeAll(model);
                int i=0;
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    model.add(dataSnapshot1.getValue(Comentario.class));
                    Comentario comentario=dataSnapshot1.getValue(Comentario.class);
                    LinearLayout layoutNombreApellido = view.findViewById(R.id.comentario2);

                    TextView textViewNombreApellido=new TextView(VisualizarAtractivoTuristicoFragment.this.getActivity());
                    textViewNombreApellido.setTextColor(Color.BLACK);
                    textViewNombreApellido.setText(comentario.getNombreUsuario()+" "+comentario.getApellidoUsuario());

                    TextView textViewComentario=new TextView(VisualizarAtractivoTuristicoFragment.this.getActivity());
                    textViewComentario.setText(comentario.getComentario());

                    layoutNombreApellido.addView(textViewNombreApellido);
                    layoutNombreApellido.addView(textViewComentario);

                    if(i==1){
                        break;
                    }
                    i++;
                }

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
        //textViewratingBar=view.findViewById(R.id.textViewRatingBar);
        textViewratingBar2=view.findViewById(R.id.textViewRatingBar2);
        //textViewratingBar.setOnClickListener(this);
        textViewratingBar2.setOnClickListener(this);
        ratingBar=view.findViewById(R.id.rating);
        ratingBar2=view.findViewById(R.id.rating2);

        linearLayoutcategorias=view.findViewById(R.id.linearLatyoutCategorias);
        tagGroup = (TagView)view.findViewById(R.id.tag_group2);

        //textViewOpiniones=view.findViewById(R.id.textViewOpinionesn);
        textViewOpiniones2=view.findViewById(R.id.textViewOpiniones2);

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
        int i=0;
        if(!atractivoTuristico.getTelefono().equals("")){
            i++;
            creaInformacionAdicional("Teléfono",atractivoTuristico.getTelefono());
        }
        if(!atractivoTuristico.getHorarioDeAtencion().equals("")){
            i++;
            creaInformacionAdicional("Horario de Atención",atractivoTuristico.getHorarioDeAtencion());
        }
        if(!atractivoTuristico.getPaginaWeb().equals("")){
            i++;
            creaInformacionAdicional("Página Web",atractivoTuristico.getPaginaWeb());
        }
        if(!atractivoTuristico.getRedesSociales().equals("")){
            i++;
            creaInformacionAdicional("Redes Sociales",atractivoTuristico.getRedesSociales());
        }
        if (i==4){
            textViewAñadirInformacionAdicional.setVisibility(view.GONE);
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
        mDatabase.child(Referencias.CALIFICACIONPROMEDIO).child(atractivoTuristico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CalificacionPromedio calificacionPromedio=dataSnapshot.getValue(CalificacionPromedio.class);
                if(calificacionPromedio!=null){
                    String calificacion=String.valueOf(calificacionPromedio.getPromedioCalificacion());
                    //textViewratingBar.setText(calificacion.substring(0,3));
                    //ratingBar.setRating(Float.parseFloat(calificacion));
                    //textViewOpiniones.setText(String.valueOf(calificacionPromedio.getTotalPersonas()));
                    //ratingBar.setEnabled(false);

                    textViewratingBar2.setText(calificacion.substring(0,3));
                    ratingBar2.setRating(Float.parseFloat(calificacion));
                    textViewOpiniones2.setText(String.valueOf(calificacionPromedio.getTotalPersonas()));
                    ratingBar2.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        textViewVerMasComentarios();
        botonCalificar();
        imageViewLike=view.findViewById(R.id.imageViewLike);
        imageViewLike.setTag(R.drawable.likeoff);
        //comprobar like del usuario al atractivo turistico
        mDatabase.child(Referencias.USUARIOLEGUSTAATRACTIVOTURISTICO).child(IdUsuario.getIdUsuario()).child(atractivoTuristico.getId()).child(Referencias.MEGUSTA).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String meGusta=dataSnapshot.getValue(String.class);
                if(meGusta!=null){
                    if(meGusta.equals(Referencias.MEGUSTA)){
                        imageViewLike.setImageResource(R.drawable.likeon);
                        imageViewLike.setTag(R.drawable.likeon);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        TextView textViewContadorLike=view.findViewById(R.id.contadorLikes);
        textViewContadorLike.setText(atractivoTuristico.getContadorMeGusta());
        setImageView(imageViewLike, textViewContadorLike);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.camara, menu);
        return;
    }

    public void setImageView(final ImageView imageViewLike, final TextView textViewContadorLike){
        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(imageViewLike.getTag().toString())==(R.drawable.likeon)){
                    imageViewLike.setImageResource(R.drawable.likeoff);
                    imageViewLike.setTag(R.drawable.likeoff);
                    //elimina el me gusta de un usuario con un atractivo turistico
                    mDatabase.child(Referencias.USUARIOLEGUSTAATRACTIVOTURISTICO).
                            child(IdUsuario.getIdUsuario()).
                            child(atractivoTuristico.getId()).removeValue();

                    int a=(Integer.valueOf(atractivoTuristico.getContadorMeGusta()));
                    if(a>0){
                        a=a-1;
                        atractivoTuristico.setContadorMeGusta(String.valueOf(a));
                        textViewContadorLike.setText(atractivoTuristico.getContadorMeGusta());

                        //aumetar en 1 el me gusta del atractivo turistico en la base de datos
                        mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                    }


                }else{
                    imageViewLike.setImageResource(R.drawable.likeon);
                    imageViewLike.setTag(R.drawable.likeon);

                    AtractivoTuristicoMeGusta atractivoTuristicoMeGusta=new AtractivoTuristicoMeGusta(atractivoTuristico.getId(),IdUsuario.getIdUsuario(),atractivoTuristico.getId(),Referencias.MEGUSTA);

                    //añade y vincula el me gusta de un usuario con un atractivo turistico
                    mDatabase.child(Referencias.USUARIOLEGUSTAATRACTIVOTURISTICO).
                            child(IdUsuario.getIdUsuario()).
                            child(atractivoTuristico.getId()).
                            setValue(atractivoTuristicoMeGusta);
                    String a=String.valueOf(Integer.valueOf(atractivoTuristico.getContadorMeGusta())+1);
                    atractivoTuristico.setContadorMeGusta(a);
                    textViewContadorLike.setText(atractivoTuristico.getContadorMeGusta());

                    //aumetar en 1 el me gusta del atractivo turistico en la base de datos
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                }
            }
        });
    }

    public void botonCalificar(){
        botonCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizarAtractivoTuristicoFragment.this.getActivity(), SetCalificacionAtractivoTuristico.class);
                intent.putExtra("imagenes", imagenes);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);
            }
        });
    }
    public void verComentarios(Comentario comentario){

    }

    public void textViewVerMasComentarios(){
        textViewVerMasComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VisualizarAtractivoTuristico)getActivity()).performStreamClick();
               // ((VisualizarAtractivoTuristico)getActivity()).mOnNavigationItemSelectedListener(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }
        });
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
        switch (v.getId()) {
            case R.id.textViewRatingBar2:

                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatLng=null;

                location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

}
