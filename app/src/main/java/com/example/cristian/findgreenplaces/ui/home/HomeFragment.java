package com.example.cristian.findgreenplaces.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.cristian.findgreenplaces.EditarPerfil;
import com.example.cristian.findgreenplaces.PerfilUsuario;
import com.example.cristian.findgreenplaces.R;
import com.example.cristian.findgreenplaces.VisualizacionDeImagen;
import com.example.cristian.findgreenplaces.VisualizarAtractivoTuristico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Clases.Adapter.AdapterListViewContribucionAtractivoTuristico;
import Clases.Models.AtractivoTuristico;
import Clases.Models.Imagen;
import Clases.Models.MeGustaImagen;
import Clases.Models.Usuario;
import Clases.Utils.GetImagenes;
import Clases.Utils.IdUsuario;
import Clases.Utils.Referencias;
import Clases.Utils.SetListViewHeightBasedOnChildren;
import Clases.Utils.Storage;
import Interfaz.OnGetDataListenerArrayListAtractivoTuristico;
import Interfaz.OnGetDataListenerAtractivoTuristico;
import Interfaz.OnGetDataListenerImagenes;

public class HomeFragment extends Fragment {
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

    Usuario usuario;
    ArrayList<MeGustaImagen> meGustaImagens;


    View root;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_camera);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        final TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Perfil");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);

        buttonEditarPerfil=root.findViewById(R.id.buttonEditarPerfil);
        linearLayoutPerfil=root.findViewById(R.id.linearLayoutPerfil);
        linearLayoutMedallas=root.findViewById(R.id.LayoutMedallas);
        linearLayoutBronce=root.findViewById(R.id.LayoutBronce);
        linearLayoutPlata=root.findViewById(R.id.LayoutPlata);
        linearLayoutOro=root.findViewById(R.id.LayoutOro);
        imagenes=new ArrayList();
        atractivoTuristicos=new ArrayList();

        //barra circular
        progressBar=root.findViewById(R.id.progressBar1);
        showProgress(true);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();

        TextView nombre=root.findViewById(R.id.textViewNombre);
        nombre.setText(IdUsuario.getNombreUsuario()+" "+IdUsuario.getApellidoUsuario());
        TextView correo=root.findViewById(R.id.textViewCorreo);
        correo.setText(IdUsuario.getCorreo());

        boolean isFilePresent = Storage.isFilePresent(getActivity(), "jsonImagenes.json");
        if(isFilePresent) {
            String jsonString = Storage.read(getActivity(), "jsonImagenes.json");
            //do the json parsing here and do the rest of functionality of app
            Type listType = new TypeToken<ArrayList<Imagen>>(){}.getType();
            this.imagenes=new Gson().fromJson(jsonString,listType);
        } else {
            mDatabase.child(Referencias.USUARIOFOTODEPERFIL).
                    child(IdUsuario.getIdUsuario()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Imagen imagen = dataSnapshot1.getValue(Imagen.class);
                        imagenes.add(imagen);
                    }
                    if (imagenes.size() > 0) {
                        Gson gson=new Gson();
                        String jsonImagenes=gson.toJson(imagenes);
                        boolean isFileCreated = Storage.create(getActivity(), "jsonImagenes.json", jsonImagenes);
                        if (isFileCreated) {
                            Log.v("perfil", "creado jsonImagenes");
                            //Usuario usuario2=gson.fromJson(json,Usuario.class);
                            //Log.v("perfil",usuario2.getNombre());
                        } else {
                            //show error or try again.
                            Log.v("perfil", "some error create jsonImagenes");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        progressBar2=root.findViewById(R.id.seekBar);

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase=database.getReference();
        imageViewFotoPerfil=root.findViewById(R.id.imageViewFotoPerfil1);
        if(IdUsuario.getUrl().equals("")) {
            Glide.with(HomeFragment.this.getActivity())
                    .load(R.drawable.com_facebook_profile_picture_blank_square)
                    .fitCenter()
                    .transform(new PerfilUsuario.CircleTransform(HomeFragment.this.getActivity()))
                    .into(imageViewFotoPerfil);
        }else{
            Glide.with(HomeFragment.this.getActivity())
                    .load(IdUsuario.getUrl())
                    .transform(new PerfilUsuario.CircleTransform(HomeFragment.this.getActivity()))
                    .fitCenter()
                    .into(imageViewFotoPerfil);
            Log.v("aburri",IdUsuario.getUrl());

        }
        imageViewFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(Referencias.FOTOMEGUSTA).child(IdUsuario.getIdUsuario()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        meGustaImagens=new ArrayList<>();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            if (dataSnapshot1.exists()) {
                                Log.v("llegue",dataSnapshot1.getKey());
                                MeGustaImagen meGustaImagen = dataSnapshot1.getValue(MeGustaImagen.class);
                                meGustaImagens.add(meGustaImagen);

                            }
                        }
                        Intent intent = new Intent(HomeFragment.this.getActivity(), VisualizacionDeImagen.class);
                        intent.putExtra("imagenes", imagenes);
                        intent.putExtra("meGustaImagens", meGustaImagens);
                        intent.putExtra("position", 0);
                        startActivityForResult(intent,IMAGENES);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        isFilePresent = Storage.isFilePresent(getActivity(), "jsonUsuario.json");
        if(isFilePresent) {
            String jsonString = Storage.read(getActivity(), "jsonUsuario.json");
            Log.v("perfilUsuario",jsonString);
            //do the json parsing here and do the rest of functionality of app
            this.usuario=new Gson().fromJson(jsonString,Usuario.class);
            createViewDatosUsuario();
        } else {
            mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usuario=dataSnapshot.getValue(Usuario.class);
                    if(usuario==null){
                        return;
                    }

                    createViewDatosUsuario();

                    String jsonUsuario=new Gson().toJson(usuario);
                    boolean isFileCreated = Storage.create(getActivity(), "jsonUsuario.json", jsonUsuario);
                    if (isFileCreated) {
                        Log.v("perfil", "creado jsonUsuario");
                        //Usuario usuario2=gson.fromJson(json,Usuario.class);
                        //Log.v("perfil",usuario2.getNombre());
                    } else {
                        //show error or try again.
                        Log.v("perfil", "some error create jsonUsuario");
                    }

                    /*
                    if(nivel.getText().toString().equalsIgnoreCase("1")){
                        linearLayoutBronce.setVisibility(View.VISIBLE);
                        linearLayoutMedallas.setVisibility(View.VISIBLE);
                    }
                    if(nivel.getText().toString().equalsIgnoreCase("2")){
                        linearLayoutBronce.setVisibility(View.VISIBLE);
                        linearLayoutPlata.setVisibility(View.VISIBLE);
                        linearLayoutMedallas.setVisibility(View.VISIBLE);
                    }
                    if(Integer.valueOf(nivel.getText().toString())>=3){
                        linearLayoutPlata.setVisibility(View.VISIBLE);
                        linearLayoutBronce.setVisibility(View.VISIBLE);
                        linearLayoutOro.setVisibility(View.VISIBLE);
                        linearLayoutMedallas.setVisibility(View.VISIBLE);
                    }*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeFragment.this.getActivity(), EditarPerfil.class);
                startActivity(intent);
            }
        });

        query(new OnGetDataListenerAtractivoTuristico() {
            @Override
            public void onSuccess(AtractivoTuristico atractivoTuristico) {
                startVisualizarAtractivoTuristico(atractivoTuristico);
            }
            @Override
            public void onStart() {
            }
            @Override
            public void onFailure() {
            }
        });

        return root;

    }

    private void createViewDatosUsuario() {
        TextView contribuciones=root.findViewById(R.id.textViewNContribuciones);
        contribuciones.setText(usuario.getContribuciones());
        TextView nivel=root.findViewById(R.id.textViewNivel);
        nivel.setText(usuario.getNivel());
        TextView puntos=root.findViewById(R.id.textViewPuntos);
        puntos.setText(usuario.getPuntos());
        TextView nombreNivel=root.findViewById(R.id.textViewNombreNivel);
        nombreNivel.setText(usuario.getNombreNivel());
        progressBar2.setProgress(Integer.valueOf(usuario.getPuntos()));
        progressBar2.setEnabled(false);
        showProgress(false);
        LinearLayout linearLayoutProgressBar=root.findViewById(R.id.linearLayoutProgressBar);
        linearLayoutProgressBar.setVisibility(View.GONE);
        linearLayoutPerfil.setVisibility(View.VISIBLE);
    }

    private void query(final OnGetDataListenerAtractivoTuristico listener) {
        boolean isFilePresent = Storage.isFilePresent(getActivity(), "jsonAtractivoTuristicos.json");
        if(isFilePresent) {
            String jsonString = Storage.read(getActivity(), "jsonAtractivoTuristicos.json");
            Log.v("perfil",jsonString);
            //do the json parsing here and do the rest of functionality of app
            Type listType = new TypeToken<ArrayList<AtractivoTuristico>>(){}.getType();
            this.atractivoTuristicos=new Gson().fromJson(jsonString,listType);
            createListViewAtractivosTuristicos(listener);
        } else {
            listener.onStart();
            mDatabase.child(Referencias.CONTRIBUCIONES).child(IdUsuario.getIdUsuario()).child(Referencias.ATRACTIVOTURISTICO).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //atractivoTuristicos.removeAll(atractivoTuristicos);
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        AtractivoTuristico atractivoTuristico2 = dataSnapshot1.getValue(AtractivoTuristico.class);
                        mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico2.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                AtractivoTuristico atractivoTuristico = dataSnapshot.getValue(AtractivoTuristico.class);
                                if (atractivoTuristico != null) {
                                    if (atractivoTuristico.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                                        atractivoTuristicos.add(atractivoTuristico);
                                    }
                                }
                                if (atractivoTuristicos.size() > 0) {
                                    String jsonAtractivoTuristicos=new Gson().toJson(atractivoTuristicos);
                                    boolean isFileCreated = Storage.create(getActivity(), "jsonAtractivoTuristicos.json", jsonAtractivoTuristicos);
                                    if (isFileCreated) {
                                        Log.v("perfil", "creado jsonAtractivoTuristicos");
                                        //Usuario usuario2=gson.fromJson(json,Usuario.class);
                                        //Log.v("perfil",usuario2.getNombre());
                                    } else {
                                        //show error or try again.
                                        Log.v("perfil", "some error create jsonAtractivoTuristicos");
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    //Log.v("atractivoo",String.valueOf(atractivoTuristicos.size()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    listener.onFailure();
                }
            });

        }
    }

    public void createListViewAtractivosTuristicos(final OnGetDataListenerAtractivoTuristico listener){
        adapter = new AdapterListViewContribucionAtractivoTuristico(HomeFragment.this.getActivity(), atractivoTuristicos);
        lista = (ListView) root.findViewById(R.id.listViewVisualizarAtractivoTuristico);
        lista.setAdapter((ListAdapter) adapter);
        SetListViewHeightBasedOnChildren.setListViewHeightBasedOnChildren(lista);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    AtractivoTuristico atractivoTuristico = (AtractivoTuristico) adapter.getItem(position);
                    listener.onSuccess(atractivoTuristico);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startVisualizarAtractivoTuristico(AtractivoTuristico atractivoTuristico){
        Log.v("atractivoooo",atractivoTuristico.getId());
        GetImagenes getImagenes=new GetImagenes();
        getImagenes.getImagenesAtractivoTuristico(atractivoTuristico, new OnGetDataListenerImagenes() {
            @Override
            public void onSuccess(ArrayList<Imagen> imagenes,AtractivoTuristico atractivoTuristico1) {
                Log.v("atractivoooo",String.valueOf(imagenes.size()));
                Intent intent=new Intent(HomeFragment.this.getContext(), VisualizarAtractivoTuristico.class);
                intent.putExtra("atractivoTuristico", atractivoTuristico1);
                intent.putExtra("imagenes",imagenes);
                startActivity(intent);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            //mLoginFormView=findViewById(R.id.progress_bar);

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
