package Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cristian.findgreenplaces.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.CalificacionPromedio;
import Clases.CalificacionUsuarioAtractivoTuristico;
import Clases.Imagen;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Informacion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Informacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Informacion extends Fragment implements View.OnClickListener {
    AtractivoTuristico atractivoTuristico;
    private TextView titulo;
    private TextView descripcion;
    private LinearLayout linearLayoutCategorias;
    private RatingBar ratingBar;
    private ImageView imageView;
    private TextView textViewratingBar;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    ArrayList<Imagen> imagenes;
    View view;
    Button button;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Informacion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Informacion.
     */
    // TODO: Rename and change types and number of parameters
    public static Informacion newInstance(String param1, String param2) {
        Informacion fragment = new Informacion();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_informacion, container, false);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        titulo = (TextView) view.findViewById(R.id.textViewTituloAT2);
        titulo.setText(atractivoTuristico.getNombre());
        descripcion=view.findViewById(R.id.textViewDescripcionAT);
        descripcion.setText(atractivoTuristico.getDescripcion());
        imageView=view.findViewById(R.id.imageViewVAT);
        getImagenesAtractivoTuristico();
        textViewratingBar=view.findViewById(R.id.textViewRatingBar);
        textViewratingBar.setOnClickListener(this);
        ratingBar=view.findViewById(R.id.rating);
        button=view.findViewById(R.id.buttonas);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ratingBar.isIndicator()){

                    float calificacion = ratingBar.getRating();
                    CalificacionUsuarioAtractivoTuristico calificacionUsuarioAtractivoTuristico=new CalificacionUsuarioAtractivoTuristico(calificacion);
                    Query q=mDatabase.child("usuario").child(atractivoTuristico.getId()).push();
                    String calificacionKey=((DatabaseReference) q).getKey();
                   // ((DatabaseReference) q).setValue();
                }


            }
        });
        return view;
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
                Glide.with(Informacion.this)
                        .load(imagenes.get(0).getUrl())
                        .fitCenter()
                        .centerCrop()
                        .into(imageView);

    }
}
