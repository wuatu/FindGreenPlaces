package Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cristian.findgreenplaces.AgregarAtractivoTuristico;
import com.example.cristian.findgreenplaces.MenuPrincipal;
import com.example.cristian.findgreenplaces.R;
import com.example.cristian.findgreenplaces.SpaceGalleryActivity;
import com.example.cristian.findgreenplaces.SpacePhotoActivity;
import com.example.cristian.findgreenplaces.SubirFoto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.Comentario;
import Clases.Imagen;
import Clases.Referencias;
import Clases.SpacePhoto;
import Clases.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FotosATFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FotosATFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FotosATFragment extends android.app.Fragment {
    private static final int MY_PERMISSIONS_REQUEST_CAMARA = 1;
    ArrayList<Imagen> imagenes;
    AtractivoTuristico atractivoTuristico;
    private ListView lista;
    private Adapter adapter;
    ArrayList<Comentario> model;
    View view;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FotosATFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FotosATFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FotosATFragment newInstance(String param1, String param2) {
        FotosATFragment fragment = new FotosATFragment();
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
        Log.v("imagenesctm",imagenes.get(0).getUrl());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_fotos_at, container, false);

        Toolbar toolbar=view.findViewById(R.id.toolbar_camera);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Fotos");
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        SpacePhoto[] getSpacePhotos=new SpacePhoto[imagenes.size()];

        int i=0;
        for (Imagen imagen:imagenes){
            if(imagen.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                SpacePhoto a = new SpacePhoto(imagen.getUrl(), imagen.getId().toString());
                getSpacePhotos[i] = a;
                i++;
            }
        }
        FotosATFragment.ImageGalleryAdapter adapter = new FotosATFragment.ImageGalleryAdapter(getContext(), getSpacePhotos);
        recyclerView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.camara, menu);
        return;
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
                                Intent intent = new Intent(FotosATFragment.this.getActivity(), SubirFoto.class);
                                intent.putExtra("atractivoTuristico", atractivoTuristico.getId());
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
                        }).show((FragmentActivity) FotosATFragment.this.getActivity());
                return false;
            case android.R.id.home:
                (getActivity()).onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMARA : {
                // Si la petición es cancelada, el array resultante estará vacío.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // El permiso ha sido concedido.
                    abrirCamara ();
                } else {
                    // Permiso denegado, deshabilita la funcionalidad que depende de este permiso.
                }
                return;
            }
            // otros bloques de 'case' para controlar otros permisos de la aplicación
        }
    }

    public void abrirCamara (){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,0);
    }

    public class ImageGalleryAdapter extends RecyclerView.Adapter<FotosATFragment.ImageGalleryAdapter.MyViewHolder>  {

        @Override
        public FotosATFragment.ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the layout
            View photoView = inflater.inflate(R.layout.item_photo, parent, false);

            FotosATFragment.ImageGalleryAdapter.MyViewHolder viewHolder = new FotosATFragment.ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(FotosATFragment.ImageGalleryAdapter.MyViewHolder holder, int position) {

            SpacePhoto spacePhoto = mSpacePhotos[position];
            ImageView imageView = holder.mPhotoImageView;

            Glide.with(mContext)
                    .load(spacePhoto.getUrl())
                    .placeholder(R.drawable.cargando)
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (mSpacePhotos.length);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    SpacePhoto spacePhoto = mSpacePhotos[position];

                    Intent intent = new Intent(mContext, SpacePhotoActivity.class);
                    intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
                    intent.putExtra("atractivoTuristico", atractivoTuristico);
                    intent.putExtra("imagen", imagenes.get(position));
                    startActivity(intent);
                }
            }
        }

        private SpacePhoto[] mSpacePhotos;
        private Context mContext;

        public ImageGalleryAdapter(Context context, SpacePhoto[] spacePhotos) {
            mContext = context;
            mSpacePhotos = spacePhotos;
        }
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
