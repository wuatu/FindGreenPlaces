package Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.cristian.findgreenplaces.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Clases.Models.AtractivoTuristico;
import Clases.Models.Imagen;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilUsuario extends Fragment {
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    ImageView imageViewFotoPerfil;
    View progressBar;
    ProgressBar progressBar2;
    LinearLayout linearLayoutPerfil;
    Button buttonEditarPerfil;
    LinearLayout linearLayoutMedallas;
    LinearLayout linearLayoutBronce;
    LinearLayout linearLayoutPlata;
    LinearLayout linearLayoutOro;
    ArrayList<Imagen> imagenes;
    private static final int IMAGENES = 1;
    ListView lista;
    Adapter adapter;
    ArrayList<AtractivoTuristico> atractivoTuristicos;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilUsuario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilUsuario.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilUsuario newInstance(String param1, String param2) {
        PerfilUsuario fragment = new PerfilUsuario();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_perfil_usuario, container, false);
        buttonEditarPerfil=view.findViewById(R.id.buttonEditarPerfil);
        linearLayoutPerfil=view.findViewById(R.id.linearLayoutPerfil);
        linearLayoutMedallas=view.findViewById(R.id.LayoutMedallas);
        linearLayoutBronce=view.findViewById(R.id.LayoutBronce);
        linearLayoutPlata=view.findViewById(R.id.LayoutPlata);
        linearLayoutOro=view.findViewById(R.id.LayoutOro);
        imagenes=new ArrayList();
        atractivoTuristicos=new ArrayList();
        return view;
    }
}
