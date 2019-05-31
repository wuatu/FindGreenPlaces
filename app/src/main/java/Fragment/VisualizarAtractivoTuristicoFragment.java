package Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.cristian.findgreenplaces.DialogoReportarAtractivoTuristico;
import com.example.cristian.findgreenplaces.InformacionAdicionalAT;
import com.example.cristian.findgreenplaces.R;
import com.example.cristian.findgreenplaces.SetCalificacionAtractivoTuristico;
import com.example.cristian.findgreenplaces.SubirFoto;
import com.example.cristian.findgreenplaces.SugerirCambioAtractivoTuristico;
import com.example.cristian.findgreenplaces.VisualizarAtractivoTuristico;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.AtractivoTuristicoMeGusta;
import Clases.CalificacionPromedio;
import Clases.Categoria;
import Clases.Comentario;
import Clases.ConocesEsteLugar;
import Clases.Contribucion;
import Clases.IdUsuario;
import Clases.Imagen;
import Clases.Referencias;
import Clases.Usuario;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VisualizarAtractivoTuristicoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VisualizarAtractivoTuristicoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisualizarAtractivoTuristicoFragment extends Fragment implements View.OnClickListener, LocationListener {
    private static final int CONTRIBUCION = 0;
    ArrayList<Categoria> categorias=new ArrayList<>();
    private TextView textViewTitulo;
    private TextView textViewDescripcionAT;
    private TextView textViewTips;
    private TextView textViewHorario;
    private TextView textViewTelefono;
    private TextView textViewpagina;
    private TextView textViewRedes;
    private LinearLayout linearLayoutHorario;
    private LinearLayout linearLayoutTelefono;
    private LinearLayout linearLayoutPagina;
    private LinearLayout linearLayoutRedes;

    private TextView textViewSugerirCambio;
    private TextView textViewAñadirInformacionAdicional;

    private TextView textViewNombre;
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

    String nivel="";

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
    LinearLayout linearLayoutLike;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout linearLayoutLoConoces;
    TextView textViewSi;
    TextView textViewNo;
    TextView textViewLoConoces;

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
        final TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Información");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        linearLayoutAñadirInformacionAdicional=view.findViewById(R.id.linearLatyoutInformacionAdicional);
        textViewVisualizaciones=view.findViewById(R.id.textViewVisualizacion);
        textViewAñadirInformacionAdicional=view.findViewById(R.id.textViewAñadirInformacionAdicional);

        textViewHorario=view.findViewById(R.id.textViewHorario);
        textViewTelefono=view.findViewById(R.id.textViewTelefono);
        textViewpagina=view.findViewById(R.id.textViewPagina);
        textViewRedes=view.findViewById(R.id.textViewRedes);
        linearLayoutHorario=view.findViewById(R.id.linearLatyoutHorario);
        linearLayoutTelefono=view.findViewById(R.id.linearLatyoutTelefono);
        linearLayoutPagina=view.findViewById(R.id.linearLatyoutPagina);
        linearLayoutRedes=view.findViewById(R.id.linearLatyoutRedes);

        textViewVerMasComentarios=view.findViewById(R.id.textViewVerMasComentarios);
        textViewTips=view.findViewById(R.id.textViewTipsAT);
        textViewTips.setText(atractivoTuristico.getTipsDeViaje());
        textViewNombre=view.findViewById(R.id.textViewTituloAT2);
        //textViewNombre.setText(atractivoTuristico.getNombre());
        linearLayoutComentario=view.findViewById(R.id.comentario);
        lista=view.findViewById(R.id.ma_lv_lista);
        model=new ArrayList<>();
        botonCalificar=view.findViewById(R.id.botonCalificar);

        linearLayoutLoConoces=view.findViewById(R.id.linearLayoutLoConoces);
        textViewSi=view.findViewById(R.id.textViewSi);
        textViewNo=view.findViewById(R.id.textViewNo);
        textViewLoConoces=view.findViewById(R.id.textViewPregunta);


        //Consulta para saber si respondio la encuesta "Conoces este Lugar"
        final DatabaseReference databaseReference=mDatabase.child(Referencias.CONOCESESTELUGAR).child(atractivoTuristico.getId()).
                child(IdUsuario.getIdUsuario());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ConocesEsteLugar conocesEsteLugar;
                if(dataSnapshot.getValue()==null){
                    //final ConocesEsteLugar conocesEsteLugar=dataSnapshot.getValue(ConocesEsteLugar.class);
                    conocesEsteLugar=new ConocesEsteLugar(IdUsuario.getIdUsuario(),atractivoTuristico.getId(),IdUsuario.getIdUsuario(),"","false");
                    databaseReference.setValue(conocesEsteLugar);
                }else {
                        conocesEsteLugar = dataSnapshot.getValue(ConocesEsteLugar.class);
                        if (conocesEsteLugar.isContestado().equals("true")) {
                            linearLayoutLoConoces.setVisibility(LinearLayout.GONE);
                        }
                }
                textViewSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        conocesEsteLugar.setRespuesta(Referencias.SI);
                        conocesEsteLugar.setContestado("true");
                        databaseReference.setValue(conocesEsteLugar);
                        textViewLoConoces.setText("Gracias por su Respuesta.");
                        textViewSi.setVisibility(TextView.GONE);
                        textViewNo.setVisibility(TextView.GONE);
                    }
                });
                textViewNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        conocesEsteLugar.setRespuesta(Referencias.NO);
                        conocesEsteLugar.setContestado("false");
                        databaseReference.setValue(conocesEsteLugar);
                        textViewLoConoces.setText("Gracias por su Respuesta.");
                        textViewSi.setVisibility(TextView.GONE);
                        textViewNo.setVisibility(TextView.GONE);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        int contadorVisualizaciones=Integer.valueOf(atractivoTuristico.getContadorVisualizaciones())+1;
        atractivoTuristico.setContadorVisualizaciones(String.valueOf(contadorVisualizaciones));
        textViewVisualizaciones.setText(String.valueOf(contadorVisualizaciones));
        mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).child(Referencias.CONTADORVISUALIZACIONES).setValue(String.valueOf(contadorVisualizaciones));

        linearLayoutLike=view.findViewById(R.id.linearLayoutLike);
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
        textViewTitulo = (TextView) view.findViewById(R.id.textViewTituloAT2);
        textViewTitulo.setText(atractivoTuristico.getNombre());
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

        mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                atractivoTuristico = dataSnapshot.getValue(AtractivoTuristico.class);
                textViewDescripcionAT.setText(atractivoTuristico.getDescripcion());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child(Referencias.CATEGORIAATRACTIVOTURISTICO).child(atractivoTuristico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tagGroup.removeAll();

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Categoria categoria=dataSnapshot1.getValue(Categoria.class);
                    Tag tag=new Tag(categoria.getEtiqueta());
                    tag.layoutColor = getResources().getColor(R.color.colorPrimary);
                    tagGroup.addTag(tag);
                    categorias.add(categoria);
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
                intent.putExtra("categorias", categorias);
                //startActivity(intent);
                startActivityForResult(intent,CONTRIBUCION);
            }
        });
        //consulta para saber nivel de usuario
        mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario()).child(Referencias.NIVEL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nivel=dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        int i=0;
        if(!atractivoTuristico.getTelefono().equals("")){
            i++;
            //creaInformacionAdicional("Teléfono",atractivoTuristico.getTelefono());
            linearLayoutTelefono.setVisibility(view.VISIBLE);
            textViewTelefono.setText(atractivoTuristico.getTelefono());
        }
        if(!atractivoTuristico.getHorarioDeAtencion().equals("")){
            i++;
            //creaInformacionAdicional("Horario de Atención",atractivoTuristico.getHorarioDeAtencion());
            linearLayoutHorario.setVisibility(view.VISIBLE);
            textViewHorario.setText(atractivoTuristico.getHorarioDeAtencion());
        }
        if(!atractivoTuristico.getPaginaWeb().equals("")){
            i++;
            //creaInformacionAdicional("Página Web",atractivoTuristico.getPaginaWeb());
            linearLayoutPagina.setVisibility(view.VISIBLE);
            textViewpagina.setText(atractivoTuristico.getPaginaWeb());
        }
        if(!atractivoTuristico.getRedesSociales().equals("")){
            i++;
            //creaInformacionAdicional("Redes Sociales",atractivoTuristico.getRedesSociales());
            linearLayoutRedes.setVisibility(view.VISIBLE);
            textViewRedes.setText(atractivoTuristico.getRedesSociales());
        }
        if (i==4){
            textViewAñadirInformacionAdicional.setVisibility(view.GONE);
        }
        textViewAñadirInformacionAdicional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(getActivity(),"Debe registrarse para contribuir en atractivo turistico!",Toast.LENGTH_SHORT).show();
                }else {
                    if(nivel.equals("1") ){
                        if(atractivoTuristico.getIdUsuario().equals(IdUsuario.idUsuario)){
                            Intent intent = new Intent(VisualizarAtractivoTuristicoFragment.this.getActivity(), InformacionAdicionalAT.class);
                            intent.putExtra("imagenes", imagenes);
                            intent.putExtra("atractivoTuristico", atractivoTuristico);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getActivity(),"Debe ser nivel 2 para añadir información adicional de otro usuario",Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(nivel.equals("2")){
                        Intent intent = new Intent(VisualizarAtractivoTuristicoFragment.this.getActivity(), InformacionAdicionalAT.class);
                        intent.putExtra("imagenes", imagenes);
                        intent.putExtra("atractivoTuristico", atractivoTuristico);
                        startActivity(intent);
                    }

                }
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

        ImageView imageViewReportar=view.findViewById(R.id.imageViewReportar);
        imageViewReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VisualizarAtractivoTuristicoFragment.this.getActivity(),DialogoReportarAtractivoTuristico.class);
                intent.putExtra("atractivoTuristico",atractivoTuristico);
                startActivity(intent);
            }
        });


        TextView textViewContadorLike=view.findViewById(R.id.contadorLikes);
        textViewContadorLike.setText(atractivoTuristico.getContadorMeGusta());
        setImageView(imageViewLike, textViewContadorLike);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camara:
                //PickImageDialog.build(new PickSetup()).show((FragmentActivity) FotosATFragment.this.getActivity());
                PickImageDialog.build(new PickSetup().setSystemDialog(false))
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                Uri uri=r.getUri();
                                Intent intent = new Intent(VisualizarAtractivoTuristicoFragment.this.getActivity(), SubirFoto.class);
                                intent.putExtra("atractivoTuristico", atractivoTuristico);
                                intent.putExtra("imagen",uri.toString());
                                Log.v("descargar",uri.toString());
                                startActivity(intent);

                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show((FragmentActivity) VisualizarAtractivoTuristicoFragment.this.getActivity());
                return false;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.camara, menu);
        return;
    }

    public void setImageView(final ImageView imageViewLike, final TextView textViewContadorLike){
        linearLayoutLike.setOnClickListener(new View.OnClickListener() {
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
                    //disminuye en 1 los puntos de un usuario cada vez que quitan "me gusta"
                    mDatabase.child(Referencias.CONTRIBUCIONESPORAT).child(atractivoTuristico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<String> idUsuarios=new ArrayList<>();
                                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    String idUsuario=dataSnapshot1.getKey();
                                    idUsuarios.add(idUsuario);
                                    final DatabaseReference databaseReference=mDatabase.child(Referencias.USUARIO).child(idUsuario);
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
                                }


                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


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

                    //aumenta en 1 los puntos de un usuario con cada "me gusta"
                    mDatabase.child(Referencias.CONTRIBUCIONESPORAT).child(atractivoTuristico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> idUsuarios=new ArrayList<>();
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                String idUsuario=dataSnapshot1.getKey();
                                idUsuarios.add(idUsuario);
                                final DatabaseReference databaseReference=mDatabase.child(Referencias.USUARIO).child(idUsuario);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        databaseReference.setValue(usuario);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTRIBUCION && resultCode == RESULT_OK) {
           atractivoTuristico = (AtractivoTuristico) data.getSerializableExtra("atractivoTuristico");
            ArrayList<Categoria>categoriasNuevas= (ArrayList<Categoria>) data.getSerializableExtra("categorias");
            ArrayList<Contribucion>contribuciones = (ArrayList<Contribucion>) data.getSerializableExtra("contribuciones");

            for (Contribucion contribucion:contribuciones) {
                if(contribucion.getContribucionNombre().equalsIgnoreCase(Referencias.NOMBRE)){
                    textViewTitulo.setText(atractivoTuristico.getNombre());
                }
                if (contribucion.getContribucionNombre().equalsIgnoreCase(Referencias.DESCRIPCION)){
                    textViewDescripcionAT.setText(atractivoTuristico.getDescripcion());
                }
                if (contribucion.getContribucionNombre().equalsIgnoreCase(Referencias.TIPSDEVIAJE)){
                    textViewTips.setText(atractivoTuristico.getTipsDeViaje());
                }
                if (contribucion.getContribucionNombre().equalsIgnoreCase(Referencias.HORARIODEATENCION)){
                    textViewHorario.setText(atractivoTuristico.getHorarioDeAtencion());
                }
                if (contribucion.getContribucionNombre().equalsIgnoreCase(Referencias.TELEFONO)){
                    textViewTelefono.setText(atractivoTuristico.getTelefono());
                }
                if (contribucion.getContribucionNombre().equalsIgnoreCase(Referencias.PAGINAWEB)){
                    textViewpagina.setText(atractivoTuristico.getPaginaWeb());
                }
                if (contribucion.getContribucionNombre().equalsIgnoreCase(Referencias.REDESSOCIALES)){
                    textViewRedes.setText(atractivoTuristico.getRedesSociales());
                }

            }

            if(categoriasNuevas!=null){
                tagGroup.removeAll();
                for (Categoria categoria : categoriasNuevas) {
                    Tag tag=new Tag(categoria.getEtiqueta());
                    tag.layoutColor = getResources().getColor(R.color.colorPrimary);
                    tagGroup.addTag(tag);
                    //categorias.add(categoria);
                }
            }

        }
    }

}
