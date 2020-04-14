package com.example.cristian.findgreenplaces;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import Clases.AtractivoTuristico;
import Clases.CustomAdapter;
import Clases.IdUsuario;
import Clases.Referencias;


public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener, Serializable {
    public Address addres = null;
    LinearLayout linearLayoutDialogo;
    LinearLayout linearLayoutDialogo2;
    LinearLayout linearLayoutDialogo3;
    public Address currentAddres;
    public ArrayList<AtractivoTuristico> atractivosTuristicosTemp;
    public String[] regions;
    public String[] city;
    public boolean isCiudadORegion=false;
    public int spinerPosition=0;
    public Tag tagBusqueda=new Tag("");
    public ArrayList<Tag> tags=new ArrayList<>();
    public ImageView imageViewIr;
    public GoogleMap mMap;
    public GoogleApiClient mGoogleApiClient;
    public FusedLocationProviderClient mFusedLocationClient;
    public DatabaseReference mDatabase;
    public FirebaseDatabase database;
    public SearchView searchViewBuscar;
    public ArrayList<String> keysAtractivosTuristicos;
    public ArrayList<AtractivoTuristico> atractivoTuristicos;
    public AtractivoTuristico atractivoTuristico;
    public static String PREFS_KEY = "mispreferencias";
    public static String SESIONINICIADA = "estado.sesion";
    public static String IDUSUARIO = "mispreferencias2";
    public final int REQUEST_ACCESS_FINE = 0;
    private static final int FOTOPERFIL = 1;
    public Spinner spinner;
    public LinearLayout linearLayoutFocus;
    public NavigationView navigationView;
    public View hView;
    public ImageView imageViewFotoPerfil;
    public String busqueda;
    public int bandera = 0;
    public TagView tagGroup;

    public View view;
    public String busquedaCategoria;

    public boolean isFiltroOtros=false;
    public boolean isCiudadOComuna=false;

    public boolean isSinFiltro=false;
    public int posicionCiudadRegionCategoria=0;
    public int posicionFiltroOtros=0;

    public String busquedaCiudad;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_maps);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE);

        }
        imageViewIr=findViewById(R.id.imageViewIr);
        atractivosTuristicosTemp= new ArrayList<>();
        linearLayoutFocus = findViewById(R.id.layoutfocus);
        spinner = findViewById(R.id.spinner);
        /*ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.opciones, R.layout.layout_item_spinner);
        spinner.setAdapter(adapter1);*/
        String list[]=new String[5];
        list[0]="Sin Filtros";
        list[1]="Mejor Evaluado";
        list[2]="Mas Me Gusta";
        list[3]="Mas Visto";
        list[4]="Default";

        int hidingItemIndex = 4;

        CustomAdapter dataAdapter = new CustomAdapter(MenuPrincipal.this, R.layout.layout_item_spinner, list, hidingItemIndex);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView) toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("AhoraTurismo");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LocationManager locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertNoGps();
        }

        if(!isOnlineNet()){

            muestraMensajeSinConexion();
            quitaMensajes2();
        }

        FloatingActionButton floatingButtonAgregarAtractivoTuristico = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton floatingButtonMiUbicacion = (FloatingActionButton) findViewById(R.id.fab2);
        final float density = getResources().getDisplayMetrics().density;
        final Drawable drawable = getResources().getDrawable(R.drawable.lupa);
        final int width = Math.round(25 * density);
        final int height = Math.round(25 * density);
        drawable.setBounds(0, 0, width, height);

        regions = getResources().getStringArray(R.array.region_array);
        city = getResources().getStringArray(R.array.ciudad_array);

        floatingButtonAgregarAtractivoTuristico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")) {
                    Toast.makeText(MenuPrincipal.this, "Debe registrarse para agregar atractivo turistico!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(MenuPrincipal.this, AgregarAtractivoTuristico.class);
                    intent.putExtra("atractivosTuristicos", atractivoTuristicos);
                    startActivity(intent);
                    //setContentView(R.layout.activity_dialogo_visualizar_atractivo_turistico);
                }
                quitaMensajes();
            }
        });

        floatingButtonMiUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitaMensajes();
                if (ActivityCompat.checkSelfPermission(MenuPrincipal.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MenuPrincipal.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MenuPrincipal.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    LatLng latLng;
                                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
                                    Geocoder geo = new Geocoder(MenuPrincipal.this);
                                    List<Address> adress = null;
                                }
                            }
                        });

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.textViewNombre);
        nav_user.setText(IdUsuario.getNombreUsuario() + " " + IdUsuario.getApellidoUsuario());
        TextView correo = hView.findViewById(R.id.textViewCorreo);
        correo.setText(IdUsuario.getCorreo());
        imageViewFotoPerfil = hView.findViewById(R.id.imageViewFotoPerfil);
        if (IdUsuario.getUrl().equals("")) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.com_facebook_profile_picture_blank_square)
                    .fitCenter()
                    .into(imageViewFotoPerfil);
        } else {
            Glide.with(getApplicationContext())
                    .load(IdUsuario.getUrl())
                    .fitCenter()
                    .into(imageViewFotoPerfil);

        }
        imageViewFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, FotoPerfil.class);
                startActivityForResult(intent,FOTOPERFIL);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        //Log.v("taza2", IdUsuario.getNombreUsuario());

        if (IdUsuario.getIdUsuario() == null) {
            ejecutarLoginActivity();
        }
        // Arreglo con las regiones




        // Le pasamos las regiones al adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, regions);
        // finalmente le asignamos el adaptador a nuestro elemento
        //searchViewBuscar.setAdapter(adapter);
        atractivoTuristicos = new ArrayList();
        //searchViewBuscar=findViewById(R.id.editTextBuscar);
        keysAtractivosTuristicos = new ArrayList();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();




        imageViewIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(atractivoTuristico!=null) {
                    if (!atractivoTuristico.getLatitud().equals("")) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + atractivoTuristico.getLatitud() + "," + atractivoTuristico.getLongitud() + "&travelmode=driving"));
                        startActivity(intent);
                    }
                }
            }
        });

        tagGroup = (TagView)findViewById(R.id.tag_group);
        searchViewBuscar = findViewById(R.id.autocomplete_region);
        searchViewBuscar.setFocusable(false);
        searchViewBuscar.setIconified(true);
        searchViewBuscar.setQueryHint("Buscar");

        searchViewBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                quitaMensajes();

                busqueda=limpiarAcentos(query);
                busquedaCategoria=busqueda;
                //le saca el espacio al final de la busqueda
                if(busqueda.substring((busqueda.length()-1),busqueda.length()).equalsIgnoreCase(" ")){
                    busqueda=busqueda.substring(0,busqueda.length()-1);
                }

                    isCiudadORegion=true;

                    if (tags.size() > 0) {
                        int i = 0;
                        int j = 0;
                        boolean isBusqueda = false;
                        for (Tag tag : tags) {
                            if (tag.text.equalsIgnoreCase("Mejor Evaluado X") ||
                                    tag.text.equalsIgnoreCase("mas me gusta X") ||
                                    tag.text.equalsIgnoreCase("mas visto X")){

                            }else{
                                isBusqueda = true;
                                j = i;
                            }
                            i++;
                        }
                        if (isBusqueda) {
                            Tag tag = new Tag(query + " " + "X");
                            tag.layoutColor = getResources().getColor(R.color.colorPrimary);
                            tagGroup.getTags().set(j,tag);
                            tags.set(j,tag);
                            Tag tagAux=new Tag("");
                            tagGroup.addTag(tagAux);
                            tagGroup.remove(tagGroup.getTags().size()-1);
                            tag.layoutColor = getResources().getColor(R.color.colorPrimary);
                            posicionCiudadRegionCategoria=j;

                        } else {
                            Tag tag = new Tag(query + " " + "X");
                            tag.layoutColor = getResources().getColor(R.color.colorPrimary);
                            tagGroup.addTag(tag);
                            tags.add(tag);
                            posicionCiudadRegionCategoria=tags.size()-1;
                        }

                    } else {
                        tagGroup.removeAll();
                        tags.clear();
                        Tag tag = new Tag(query + " " + "X");
                        tag.layoutColor = getResources().getColor(R.color.colorPrimary);
                        tagGroup.addTag(tag);
                        tags.add(tag);
                        posicionCiudadRegionCategoria=0;
                    }

                mostrarAtractivoTuristicoPorCiudadOComuna();

                    return true;

            }


            @Override
            public boolean onQueryTextChange(String newText) { return true; }

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int posicionItemSeleccionado, long id) {
                if(posicionItemSeleccionado==0){
                    ArrayList<Tag> auxTags=new ArrayList<>();
                    for (Tag tag : tagGroup.getTags()) {
                        if (tag.text.equalsIgnoreCase("Mejor Evaluado X") || tag.text.equalsIgnoreCase("mas me gusta X") || tag.text.equalsIgnoreCase("mas visto X") || tag.text.equalsIgnoreCase("categoria X")) {
                            Tag tag2=tag;
                            auxTags.add(tag2);
                            identificaYEliminaFiltrosDeBusqueda(tag);
                            Log.v("filtro",tag.text);
                        }

                        //buscaAtractivosTuristicosConOsinFiltro();
                    }
                    int i=1;
                    for (Tag tag:auxTags){
                        tagGroup.getTags().remove(tag);
                        i++;
                    }
                    Tag tagAux=new Tag("");
                    tagGroup.addTag(tagAux);
                    tagGroup.remove(tagGroup.getTags().size()-1);
                    Log.v("filtrotagsize",String.valueOf(tagGroup.getTags().size()));
                    auxTags=new ArrayList<>();
                    for (Tag tag : tags) {
                        if (tag.text.equalsIgnoreCase("Mejor Evaluado X") || tag.text.equalsIgnoreCase("mas me gusta X") || tag.text.equalsIgnoreCase("mas visto X") || tag.text.equalsIgnoreCase("categoria X")) {
                            Tag tag2=tag;
                            auxTags.add(tag2);
                            Log.v("filtro2",tag.text);
                        }
                        //buscaAtractivosTuristicosConOsinFiltro();
                    }
                    for (Tag tag:auxTags){
                        tags.remove(tag);

                    }
                    Log.v("filtroTags",String.valueOf(tags.size()));

                    if(tags.size()>0){
                        isFiltroOtros=false;

                        isCiudadOComuna=true;
                        isSinFiltro=true;
                        repintarMapaConArrayListTemporal(14);
                    }
                    else{
                        isFiltroOtros=false;
                        isCiudadOComuna=false;
                        isSinFiltro=true;
                        busquedaCiudad=null;
                        repintarMapaConArrayList(14);
                    }
                }

                else if (posicionItemSeleccionado == 4) {
                    //quitaMensajes();
                    View v = null;
                    TextView tv = new TextView(getApplicationContext());
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else{
                    quitaMensajes();
                    //agrega palabra o una categoria cuando no hay tag dentro
                    if(tags.size()==0){
                        Tag tag = new Tag(spinner.getSelectedItem().toString() + " " + "X");
                        tag.layoutColor = getResources().getColor(R.color.colorPrimary);
                        tagGroup.addTag(tag);
                        tags.add(tag);
                        identificaYAgregaFiltrosDeBusqueda(tag);
                        buscaAtractivosTuristicosConOsinFiltro();
                    }
                    //agrega palabra o una categoria cuando hay un tag dentro
                    else {
                         if (posicionItemSeleccionado != 0 ) {
                            int i=0;
                            Tag tag3=null;
                            for (Tag tag : tags) {
                                if (tag.text.equalsIgnoreCase("Mejor Evaluado X") || tag.text.equalsIgnoreCase("mas me gusta X") || tag.text.equalsIgnoreCase("mas visto X")) {
                                    posicionFiltroOtros=i;
                                    tag3=tag;
                                }
                                i++;
                            }
                            if(tag3!=null) {
                                Tag tag2 = new Tag(spinner.getSelectedItem().toString() + " " + "X");
                                tag2.layoutColor = getResources().getColor(R.color.colorPrimary);
                                tagGroup.getTags().remove(posicionFiltroOtros);
                                tagGroup.getTags().add(posicionFiltroOtros,tag2);
                                Tag tag=new Tag("");
                                tagGroup.addTag(tag);
                                tagGroup.remove(tagGroup.getTags().size()-1);
                                tags.set(posicionFiltroOtros,tag2);
                                //identificaYAgregaFiltrosDeBusqueda(tag2);
                                buscaAtractivosTuristicosConOsinFiltro();

                            }
                            else {
                                Tag tag2 = new Tag(spinner.getSelectedItem().toString() + " " + "X");
                                tag2.layoutColor = getResources().getColor(R.color.colorPrimary);
                                tagGroup.addTag(tag2);
                                tags.add(tag2);
                                identificaYAgregaFiltrosDeBusqueda(tag2);
                                buscaAtractivosTuristicosConOsinFiltro();
                            }




                        }
                    }
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //Elimina tag cuando se presiona sobre el
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int i) {
                identificaYEliminaFiltrosDeBusqueda(tag);
                tagGroup.remove(i);
                tags.remove(i);
                boolean isFiltro=false;
                if (tag.text.equalsIgnoreCase("Mejor Evaluado X") || tag.text.equalsIgnoreCase("mas me gusta X") || tag.text.equalsIgnoreCase("mas visto X")) {
                    isFiltro=true;
                    repintarMapaConArrayListTemporal(1);
                }
                else {
                    repintarMapaConArrayList(1f);
                    for (AtractivoTuristico atractivoTuristico : atractivoTuristicos) {
                        atractivosTuristicosTemp.add(atractivoTuristico);
                    }
                    /*atractivosTuristicosTemp.clear();
                    if (tags.size() == 1) {
                        if (isFiltro) {

                            for (AtractivoTuristico atractivoTuristico : atractivoTuristicos) {
                                atractivosTuristicosTemp.add(atractivoTuristico);
                            }

                            mostrarAtractivoTuristicoPorCiudadOComuna();
                            spinner.setSelection(4);
                        } else {
                            repintarMapaConArrayList(1f);
                            for (AtractivoTuristico atractivoTuristico : atractivoTuristicos) {
                                atractivosTuristicosTemp.add(atractivoTuristico);
                            }
                            //Log.v("tagctm",String.valueOf(tags.size()));
                            for (Tag tag2 : tags) {
                                if (tag2.text.equalsIgnoreCase("Mejor Evaluado X")) {
                                    spinner.setSelection(1);
                                    buscaAtractivosTuristicosConOsinFiltro();
                                } else if (tag2.text.equalsIgnoreCase("mas me gusta X")) {
                                    spinner.setSelection(2);
                                    buscaAtractivosTuristicosConOsinFiltro();
                                } else if (tag2.text.equalsIgnoreCase("mas visto X")) {
                                    spinner.setSelection(3);
                                    buscaAtractivosTuristicosConOsinFiltro();
                                } else {
                                    spinner.setSelection(4);
                                }
                            }
                        }
                    }*/
                }

                if (tags.size() == 0) {
                    spinner.setSelection(4);
                    for (AtractivoTuristico atractivoTuristico : atractivoTuristicos) {
                        atractivosTuristicosTemp.add(atractivoTuristico);
                    }
                //repintarMapaConArrayListTemporal(14);
                    repintarMapaConArrayList(14);
                }
            }
        });
        //set delete listener
        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
            }
        });

        mostrarAtractivoTuristicoPorRegionInicio();

    }

    private void identificaYAgregaFiltrosDeBusqueda(Tag tag) {
        if (tag.text.equalsIgnoreCase("Mejor Evaluado X") ||
                tag.text.equalsIgnoreCase("mas me gusta X") ||
                tag.text.equalsIgnoreCase("mas visto X")) {
            isFiltroOtros=true;
        }
        else{
            isCiudadOComuna=true;
        }
    }

    private void identificaYEliminaFiltrosDeBusqueda(Tag tag) {
        if (tag.text.equalsIgnoreCase("Mejor Evaluado X") ||
                tag.text.equalsIgnoreCase("mas me gusta X") ||
                tag.text.equalsIgnoreCase("mas visto X")) {
            isFiltroOtros=false;
        }
        else{
            isCiudadOComuna=false;
        }
    }

    private void quitaMensajes() {
        linearLayoutDialogo=findViewById(R.id.layoutDialogo);
        linearLayoutDialogo.setVisibility(View.GONE);
        linearLayoutDialogo2=findViewById(R.id.layoutDialogo2);
        linearLayoutDialogo2.setVisibility(View.GONE);
        linearLayoutDialogo3=findViewById(R.id.layoutDialogo3);
        linearLayoutDialogo3.setVisibility(View.GONE);

        imageViewIr.setVisibility(View.GONE);
    }
    private void quitaMensajes2() {
        LinearLayout linearLayoutDialogo=findViewById(R.id.layoutDialogo);
        linearLayoutDialogo.setVisibility(View.GONE);
        LinearLayout linearLayoutDialogo3=findViewById(R.id.layoutDialogo3);
        linearLayoutDialogo3.setVisibility(View.GONE);

        imageViewIr.setVisibility(View.GONE);
    }
    private void muestraMensajeSinConexion() {
        LinearLayout linearLayoutDialogo=findViewById(R.id.layoutDialogo2);
        linearLayoutDialogo.setVisibility(View.VISIBLE);
    }


    public boolean isNombre(){
        for(AtractivoTuristico atractivoTuristico:atractivoTuristicos){
            if(atractivoTuristico.getNombre().toUpperCase().indexOf(busqueda.toUpperCase())==0){
                return true;
            }
        }
        return false;
    }

    public void AlertNoGps(){
        new AlertDialog.Builder(MenuPrincipal.this)
                .setTitle("Activar GPS")
                .setMessage("El sistema de GPS está desactivado, ¿Desea Activarlo?")
                //.setIcon(R.drawable.aporte)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    public void buscaAtractivosTuristicosConOsinFiltro() {
        /*Log.v("busqueda2",busqueda);
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("sin filtros")) {
            //mMap.clear();
            mostrarAtractivoTuristicoPorCiudadOComuna();
        }*/
        /*if (spinner.getSelectedItem().toString().equalsIgnoreCase("categoria")) {
            mMap.clear();
            if(busqueda!=null) {
                if(busquedaCategoria!=null) {
                    getkeyAtractivoTuristico(busquedaCategoria);
                    AtractivoTuristicoPorCategoria();
                }
            }
        }*/
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("mas me gusta")) {
            getMasMeGustaAtractivoTuristico();
        }
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("mejor evaluado")) {

            getMejorCalificadosAtractivoTuristico();
        }
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("mas visto")) {
            //mMap.clear();
            getMasVistosAtractivoTuristico();
        }
    }





    public void getMasMeGustaAtractivoTuristico(){
        AtractivoTuristico[] lista=new AtractivoTuristico[atractivosTuristicosTemp.size()];
        int k=0;
        for(AtractivoTuristico atractivoTuristico: atractivosTuristicosTemp){
            lista[k]=atractivoTuristico;
            k++;
        }
        AtractivoTuristico temp;
        for(int i=0; i<lista.length;i++){
            for(int j=0; j<lista.length-1;j++){
                if(Integer.valueOf(lista[j].getContadorMeGusta())<Integer.valueOf(lista[j+1].getContadorMeGusta())){
                    temp=lista[j];
                    lista[j]=lista[j+1];
                    lista[j+1]=temp;
                }
            }
        }
        mMap.clear();
        for(int l=0;l<lista.length;l++){
            if(l==0) {repintarMapaConFiltroDeBusqueda2(lista[l], 0,R.drawable.marcador1);}
            if(l==1) {repintarMapaConFiltroDeBusqueda2(lista[l], 0,R.drawable.marcador2);}
            if(l==2) {repintarMapaConFiltroDeBusqueda2(lista[l], 0,R.drawable.marcador3);}
            if(l==3) {repintarMapaConFiltroDeBusqueda2(lista[l], 0,R.drawable.marcador4);}
            if(l==4) {repintarMapaConFiltroDeBusqueda2(lista[l], 0,R.drawable.marcador5);}
            if(l==5) {repintarMapaConFiltroDeBusqueda2(lista[l], 0,R.drawable.marcador6);}
            if(l==6) {repintarMapaConFiltroDeBusqueda2(lista[l], 0,R.drawable.marcador7);}
            if(l==7) {repintarMapaConFiltroDeBusqueda2(lista[l], 0,R.drawable.marcador8);}
            if(l==8) {repintarMapaConFiltroDeBusqueda2(lista[l], 0,R.drawable.marcador9);}

            if (l>=9){
                return;
            }
            Log.v("atractivo",lista[l].getNombre());
        }
    }

    public void getMejorCalificadosAtractivoTuristico(){
        AtractivoTuristico[] lista=new AtractivoTuristico[atractivosTuristicosTemp.size()];
        int k=0;
        for(AtractivoTuristico atractivoTuristico: atractivosTuristicosTemp){
            lista[k]=atractivoTuristico;
            k++;
        }
        AtractivoTuristico temp;
        for(int i=0; i<lista.length;i++){
            for(int j=0; j<lista.length-1;j++){
                Double dobleI=Double.valueOf(lista[j].getCalificacion())*10;
                Double dobleJ=Double.valueOf(lista[j+1].getCalificacion())*10;
                int integerI=dobleI.intValue();
                int integerJ=dobleJ.intValue();
                if(integerI<integerJ){
                    temp=lista[j];
                    lista[j]=lista[j+1];
                    lista[j+1]=temp;
                }
            }
        }
        mMap.clear();
        for(int l=0;l<lista.length;l++){
            if(l==0) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador1);}
            if(l==1) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador2);}
            if(l==2) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador3);}
            if(l==3) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador4);}
            if(l==4) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador5);}
            if(l==5) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador6);}
            if(l==6) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador7);}
            if(l==7) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador8);}
            if(l==8) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador9);}
            if (l>=9){
                return;
            }
        }
    }

    public void getMasVistosAtractivoTuristico(){
        AtractivoTuristico[] lista=new AtractivoTuristico[atractivosTuristicosTemp.size()];
        int k=0;
        for(AtractivoTuristico atractivoTuristico: atractivosTuristicosTemp){
            lista[k]=atractivoTuristico;
            k++;
        }
        AtractivoTuristico temp;
        for(int i=0; i<lista.length;i++){
            for(int j=0; j<lista.length-1;j++){
                if(Integer.valueOf(lista[j].getContadorVisualizaciones())<Integer.valueOf(lista[j+1].getContadorVisualizaciones())){
                    temp=lista[j];
                    lista[j]=lista[j+1];
                    lista[j+1]=temp;
                }
            }
        }
        mMap.clear();
        for(int l=0;l<lista.length;l++){
            if(l==0) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador1);}
            if(l==1) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador2);}
            if(l==2) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador3);}
            if(l==3) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador4);}
            if(l==4) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador5);}
            if(l==5) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador6);}
            if(l==6) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador7);}
            if(l==7) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador8);}
            if(l==8) {repintarMapaConFiltroDeBusqueda2(lista[l], l,R.drawable.marcador9);}
            if (l>=9){
                return;
            }
        }
    }




    public void repintarMapaConArrayList(float zoom){
        if(atractivoTuristicos.size()>0) {
            mMap.clear();
            for (AtractivoTuristico atractivoTuristico: atractivoTuristicos) {
                double latitud, longitud;
                latitud = atractivoTuristico.getLatitud();
                longitud = atractivoTuristico.getLongitud();
                LatLng sydney = new LatLng(latitud, longitud);
                Double dCalificacion = Double.valueOf(atractivoTuristico.getCalificacion());
                int iCalificacion=dCalificacion.intValue();
                /*if(iCalificacion>=4) {
                    mMap.addMarker(new MarkerOptions().
                            position(sydney).
                            icon(bitmapDescriptorFromVector(MenuPrincipal.this, R.drawable.marcador, atractivoTuristico.getCalificacion(), 0)).
                            title(atractivoTuristico.getNombre()));
                }
                if(iCalificacion==3 && zoom >=13f) {
                    mMap.addMarker(new MarkerOptions().
                            position(sydney).
                            icon(bitmapDescriptorFromVector(MenuPrincipal.this, R.drawable.marcador, atractivoTuristico.getCalificacion(), 0)).
                            title(atractivoTuristico.getNombre()));
                }
                if(iCalificacion==2 && zoom >=14f) {
                    mMap.addMarker(new MarkerOptions().
                            position(sydney).
                            icon(bitmapDescriptorFromVector(MenuPrincipal.this, R.drawable.marcador, atractivoTuristico.getCalificacion(), 0)).
                            title(atractivoTuristico.getNombre()));
                }
                if(iCalificacion<=1 && zoom >=15f) {
                    mMap.addMarker(new MarkerOptions().
                            position(sydney).
                            icon(bitmapDescriptorFromVector(MenuPrincipal.this, R.drawable.marcador, atractivoTuristico.getCalificacion(), 0)).
                            title(atractivoTuristico.getNombre()));
                }*/
                mMap.addMarker(new MarkerOptions().
                        position(sydney).
                        icon(bitmapDescriptorFromVector(MenuPrincipal.this, R.drawable.marcador, atractivoTuristico.getCalificacion(), 0)).
                        title(atractivoTuristico.getNombre()));
            }
        }
    }

    public void repintarMapaConArrayListTemporal(float zoom){
        if(atractivoTuristicos.size()>0) {
            mMap.clear();
            for (AtractivoTuristico atractivoTuristico: atractivosTuristicosTemp) {
                double latitud, longitud;
                latitud = atractivoTuristico.getLatitud();
                longitud = atractivoTuristico.getLongitud();
                LatLng sydney = new LatLng(latitud, longitud);
                Double dCalificacion = Double.valueOf(atractivoTuristico.getCalificacion());
                int iCalificacion=dCalificacion.intValue();
                mMap.addMarker(new MarkerOptions().
                        position(sydney).
                        icon(bitmapDescriptorFromVector(MenuPrincipal.this, R.drawable.marcador, atractivoTuristico.getCalificacion(), 0)).
                        title(atractivoTuristico.getNombre()));
            }
        }
    }

    //busca por region inicial la camara se actualiza en la ubicacion de la persona
    public void mostrarAtractivoTuristicoPorRegionInicio() {
        Query q = mDatabase.child("atractivoTuristico");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                atractivoTuristicos.clear();
                mMap.clear();
                atractivosTuristicosTemp.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    AtractivoTuristico atractivoTuristico = dataSnapshot1.getValue(AtractivoTuristico.class);
                    String limpiaCiudad=limpiarAcentos(atractivoTuristico.getCiudad());
                    String limpiaRegion=limpiarAcentos(atractivoTuristico.getComuna());
                    //Log.v("limon",atractivoTuristico.getCalificacion());
                    if(atractivoTuristico.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                        //if (limpiaCiudad.equalsIgnoreCase(busqueda) || limpiaRegion.equalsIgnoreCase(busqueda)) {
                            repintarMapaConFiltroDeBusqueda(atractivoTuristico,0,R.drawable.marcador);
                            atractivoTuristicos.add(atractivoTuristico);
                            atractivosTuristicosTemp.add(atractivoTuristico);
                        //}
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void mostrarAtractivoTuristicoPorCiudadOComuna() {
        Log.v("busqueda3",busqueda);
        Query q = mDatabase.child("atractivoTuristico");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Geocoder geo = new Geocoder(MenuPrincipal.this);
                int maxResultados = 1;
                List<Address> adress = null;
                try {
                    adress = geo.getFromLocationName(busqueda, maxResultados);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (adress != null) {
                    if (adress.size() > 0) {
                        LatLng latLng = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
                        currentAddres=adress.get(0);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
                        buscarAtractivoTuristicoPorCategoria(busqueda);
                        return;
                    }
                }
                buscarAtractivoTuristicoPorCategoria(busqueda);
                if(atractivosTuristicosTemp.size()==0){
                    mMap.clear();
                    Toast.makeText(MenuPrincipal.this, "No se encuentran resultados de búsqueda", Toast.LENGTH_SHORT).show();
                }
                return;
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void restauraMapaInicialSinFiltro(){
        mMap.clear();
        for(AtractivoTuristico atractivoTuristico: atractivoTuristicos){
            repintarMapaConFiltroDeBusqueda(atractivoTuristico,0,R.drawable.marcador);
        }
    }

    public boolean isCiudadORegion(){
        for(int i=0;i<regions.length;i++){
            if(regions[i].toUpperCase().indexOf(busqueda.toUpperCase())==0){
                return true;

            }
        }
        for(int i=0;i<city.length;i++){
            if(city[i].toUpperCase().indexOf(busqueda.toUpperCase())==0){
                return true;

            }
            Log.v("cityy",city[i].toString());
            Log.v("cityy2",busqueda);
        }
        return false;
    }

    //Obtiene todas las llaves de los atractivos turisticos que coinciden con el criterio buscado
    public void getkeyAtractivoTuristico(final String busqueda) {
        mDatabase.child("categoriaAtractivoTuristico").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        String categoria=dataSnapshot2.getKey();
                        boolean keyEncontrada=false;
                        if(busqueda.equalsIgnoreCase(categoria)){
                            keysAtractivosTuristicos.add(dataSnapshot1.getKey());
                            break;
                        }
                        if (busqueda.contains(" ")) {
                            String[] textoIngresado = busqueda.split(" ");
                            if(categoria.contains(" ")){
                                String[] categorias = categoria.split(" ");
                                for (int i=0; i<textoIngresado.length;i++) {
                                    for(int j=0;j<categorias.length;j++) {
                                        if (textoIngresado[i].toUpperCase().equalsIgnoreCase(categorias[j].toUpperCase())) {
                                            keysAtractivosTuristicos.add(dataSnapshot1.getKey());
                                            keyEncontrada=true;
                                            break;
                                        }
                                        if(keyEncontrada){
                                            break;
                                        }
                                    }
                                }
                            }
                            else{
                                String[] parts = busqueda.split(" ");
                                for (int i=0; i<parts.length;i++) {
                                    if (parts[i].toUpperCase().equalsIgnoreCase(categoria.toUpperCase())) {
                                        keysAtractivosTuristicos.add(dataSnapshot1.getKey());
                                        break;
                                    }
                                }
                            }
                        }
                        else{
                            if (busqueda.toUpperCase().equalsIgnoreCase(categoria.toUpperCase())) {
                                keysAtractivosTuristicos.add(dataSnapshot1.getKey());
                                break;
                            }
                        }

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    //busca los atractivos turisticos por llave
    public void buscarAtractivoTuristicoPorCategoria(String busqueda) {
        busqueda=busqueda.concat(" ");
        getkeyAtractivoTuristico(busqueda);
        mDatabase.child("atractivoturistico").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear();
                atractivosTuristicosTemp.clear();
                for (String keyAtractivoTuristico : keysAtractivosTuristicos) {
                    Query q = mDatabase.child("atractivoTuristico").child(keyAtractivoTuristico);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AtractivoTuristico atractivoTuristico = dataSnapshot.getValue(AtractivoTuristico.class);
                                if(atractivoTuristico.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                                    repintarMapaConFiltroDeBusqueda(atractivoTuristico, 0, R.drawable.marcador);
                                    atractivosTuristicosTemp.add(atractivoTuristico);
                                }
                                buscaAtractivosTuristicosConOsinFiltro();

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
                keysAtractivosTuristicos.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Le paso atractivos turisticos y su peso
    //lo ideal es usarlo con un for
    //no me mueve la camara
    //no cambia nada del arraylist de atractivos
    public void repintarMapaConFiltroDeBusqueda(AtractivoTuristico atractivoTuristico,int tamañoMarcador, int drawableMarcador) {
        double latitud, longitud;
        latitud = atractivoTuristico.getLatitud();
        longitud = atractivoTuristico.getLongitud();
        LatLng sydney = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(sydney).
                icon(bitmapDescriptorFromVector(MenuPrincipal.this, drawableMarcador, atractivoTuristico.getCalificacion(),tamañoMarcador)).
                title(atractivoTuristico.getNombre()));
    }

    public void repintarMapaConFiltroDeBusqueda2(AtractivoTuristico atractivoTuristico,int tamañoMarcador, int drawableMarcador) {
        double latitud, longitud;
        latitud = atractivoTuristico.getLatitud();
        longitud = atractivoTuristico.getLongitud();
        LatLng sydney = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(sydney).
                icon(bitmapDescriptorFromVector2(MenuPrincipal.this, drawableMarcador,tamañoMarcador)).
                title(atractivoTuristico.getNombre()));
    }

    public boolean isRegion(){
        boolean b = false;
        for(int i=0;i<regions.length;i++){
            if(regions[i].equalsIgnoreCase(busqueda) ){
                return b=true;
            }
        }
        return b;
    }

    public boolean isCiudad(){
        boolean b = false;
        for(int i=0;i<regions.length;i++){
            if(regions[i].equalsIgnoreCase(busqueda)){
                return b=true;
            }
        }
        return b;
    }


    public Address ubicacionAddressMoveCamera(){
        Geocoder geo = new Geocoder(MenuPrincipal.this);
        int maxResultados = 1;
        List<Address> adress = null;
        try {
            adress = geo.getFromLocationName(busqueda, maxResultados);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (adress != null) {
            if (adress.size() > 0) {
                LatLng latLng = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
                Toast.makeText(MenuPrincipal.this, "ctmm", Toast.LENGTH_SHORT).show();
                return addres = adress.get(0);
            } else {
                Toast.makeText(MenuPrincipal.this, "No se encuentan datos", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        return null;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId, String scalificacion, double alto) {
        double calificacion=Double.valueOf(scalificacion);
        alto=60+alto;
        if(calificacion==1){alto=alto*1.2;}
        if(calificacion==2){alto=alto*1.4;}
        if(calificacion==3){alto=alto*1.6;}
        if(calificacion==4){alto=alto*1.8;}
        if(calificacion==5){alto=alto*2;
        }
        int anch= (int) Math.ceil(alto);
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, anch, anch);
        Bitmap bitmap = Bitmap.createBitmap(anch, anch, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor bitmapDescriptorFromVector2(Context context, int vectorResId, double alto) {
        alto=150;
        int altoo= (int) Math.ceil(alto);
        int ancho=100;
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, ancho, altoo);
        Bitmap bitmap = Bitmap.createBitmap(ancho, altoo, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public AtractivoTuristico buscarAtractivoTuristicoEnArrayListPorNombre(Marker marker) {
        for (AtractivoTuristico atractivoTuristicos : atractivoTuristicos) {
            if (marker.getTitle().equalsIgnoreCase(atractivoTuristicos.getNombre())) {
                return atractivoTuristicos;
            }
        }
        return null;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mGoogleApiClient.connect();
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                quitaMensajes();
                imageViewIr.setVisibility(View.VISIBLE);
                atractivoTuristico = buscarAtractivoTuristicoEnArrayListPorNombre(marker);
                Intent intent = new Intent(MenuPrincipal.this, DialogoVisualizarAtractivoTuristico.class);
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);

                return false;
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng latLng;
                            latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
                            Geocoder geo = new Geocoder(MenuPrincipal.this);
                            List<Address> adress = null;

                            try {
                                adress = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(adress!=null){
                                if(adress.size()>0){
                                    currentAddres=adress.get(0);
                                    busqueda=limpiarAcentos(adress.get(0).getAdminArea());
                                    //mostrarAtractivoTuristicoPorRegionInicio();
                                }
                            }

                            Log.v("quepasa", "siiii");
                        } else {
                            Log.v("quepasa", "noooo");
                        }
                    }
                });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                quitaMensajes();
            }
        });





        /*mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LinearLayout linearLayoutDialogo=findViewById(R.id.layoutDialogo);
                linearLayoutDialogo.setVisibility(View.GONE);
                LinearLayout linearLayoutDialogo2=findViewById(R.id.layoutDialogo2);
                linearLayoutDialogo2.setVisibility(View.GONE);

                float mZoom = mMap.getCameraPosition().zoom;
                if(spinner.getSelectedItemPosition()==0) {
                    repintarMapaConArrayList(mZoom);
                }

            }
        });*/


    }


    @Override
    protected void onResume() {
        super.onResume();
        //mostrarAtractivoTuristicoPorRegionInicio();
        searchViewBuscar.setQuery("", false);
        //mMap.requestFocus();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent=new Intent(MenuPrincipal.this,PerfilUsuario.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent=new Intent(MenuPrincipal.this,VisualizarContribucionAtractivoTuristico.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent=new Intent(MenuPrincipal.this,SpaceGalleryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent=new Intent(MenuPrincipal.this,Informacion.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            logout();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        guardarValorBoolean(MenuPrincipal.this, SESIONINICIADA, false);
        guardarValorString(MenuPrincipal.this, IDUSUARIO, "");
        ejecutarLoginActivity();

    }

    public static void guardarValorBoolean(Context context, String keyPref, boolean valor) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putBoolean(keyPref, valor);
        editor.commit();
    }

    public static void guardarValorString(Context context, String keyPref, String valor) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(keyPref, valor);
        editor.commit();
    }

    public static String leerValorString(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return preferences.getString(keyPref, "");
    }

    public static boolean leerValorBoolean(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return preferences.getBoolean(keyPref, false);
    }


    private void ejecutarLoginActivity() {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public static String limpiarAcentos(String cadena) {
        String limpio =null;
        if (cadena !=null) {
            String valor = cadena;
            valor = valor.toUpperCase();
            // Normalizar texto para eliminar acentos, dieresis, cedillas y tildes
            limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
            // Quitar caracteres no ASCII excepto la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
            limpio = limpio.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores
            limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC);

            limpio=limpio.toLowerCase();

            limpio = limpio.substring(0, 1).toUpperCase() + limpio.substring(1);
        }
        return limpio;
    }


    @Override
    public void onLocationChanged(Location location) {
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public Address getAddres() {
        return addres;
    }

    public void setAddres(Address addres) {
        this.addres = addres;
    }

    public Address getCurrentAddres() {
        return currentAddres;
    }

    public void setCurrentAddres(Address currentAddres) {
        this.currentAddres = currentAddres;
    }

    public ArrayList<AtractivoTuristico> getAtractivosTuristicosTemp() {
        return atractivosTuristicosTemp;
    }

    public void setAtractivosTuristicosTemp(ArrayList<AtractivoTuristico> atractivosTuristicosTemp) {
        this.atractivosTuristicosTemp = atractivosTuristicosTemp;
    }

    public String[] getRegions() {
        return regions;
    }

    public void setRegions(String[] regions) {
        this.regions = regions;
    }

    public String[] getCity() {
        return city;
    }

    public void setCity(String[] city) {
        this.city = city;
    }

    public void setCiudadORegion(boolean ciudadORegion) {
        isCiudadORegion = ciudadORegion;
    }

    public int getSpinerPosition() {
        return spinerPosition;
    }

    public void setSpinerPosition(int spinerPosition) {
        this.spinerPosition = spinerPosition;
    }

    public Tag getTagBusqueda() {
        return tagBusqueda;
    }

    public void setTagBusqueda(Tag tagBusqueda) {
        this.tagBusqueda = tagBusqueda;
    }


    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ImageView getImageViewIr() {
        return imageViewIr;
    }

    public void setImageViewIr(ImageView imageViewIr) {
        this.imageViewIr = imageViewIr;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    public FusedLocationProviderClient getmFusedLocationClient() {
        return mFusedLocationClient;
    }

    public void setmFusedLocationClient(FusedLocationProviderClient mFusedLocationClient) {
        this.mFusedLocationClient = mFusedLocationClient;
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public SearchView getSearchViewBuscar() {
        return searchViewBuscar;
    }

    public void setSearchViewBuscar(SearchView searchViewBuscar) {
        this.searchViewBuscar = searchViewBuscar;
    }

    public ArrayList<String> getKeysAtractivosTuristicos() {
        return keysAtractivosTuristicos;
    }

    public void setKeysAtractivosTuristicos(ArrayList<String> keysAtractivosTuristicos) {
        this.keysAtractivosTuristicos = keysAtractivosTuristicos;
    }

    public ArrayList<AtractivoTuristico> getAtractivoTuristicos() {
        return atractivoTuristicos;
    }

    public void setAtractivoTuristicos(ArrayList<AtractivoTuristico> atractivoTuristicos) {
        this.atractivoTuristicos = atractivoTuristicos;
    }

    public AtractivoTuristico getAtractivoTuristico() {
        return atractivoTuristico;
    }

    public void setAtractivoTuristico(AtractivoTuristico atractivoTuristico) {
        this.atractivoTuristico = atractivoTuristico;
    }

    public static String getPrefsKey() {
        return PREFS_KEY;
    }

    public static void setPrefsKey(String prefsKey) {
        PREFS_KEY = prefsKey;
    }

    public static String getSESIONINICIADA() {
        return SESIONINICIADA;
    }

    public static void setSESIONINICIADA(String SESIONINICIADA) {
        MenuPrincipal.SESIONINICIADA = SESIONINICIADA;
    }

    public static String getIDUSUARIO() {
        return IDUSUARIO;
    }

    public static void setIDUSUARIO(String IDUSUARIO) {
        MenuPrincipal.IDUSUARIO = IDUSUARIO;
    }

    public int getREQUEST_ACCESS_FINE() {
        return REQUEST_ACCESS_FINE;
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;
    }

    public LinearLayout getLinearLayoutFocus() {
        return linearLayoutFocus;
    }

    public void setLinearLayoutFocus(LinearLayout linearLayoutFocus) {
        this.linearLayoutFocus = linearLayoutFocus;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public void setNavigationView(NavigationView navigationView) {
        this.navigationView = navigationView;
    }

    public View gethView() {
        return hView;
    }

    public void sethView(View hView) {
        this.hView = hView;
    }

    public ImageView getImageViewFotoPerfil() {
        return imageViewFotoPerfil;
    }

    public void setImageViewFotoPerfil(ImageView imageViewFotoPerfil) {
        this.imageViewFotoPerfil = imageViewFotoPerfil;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public int getBandera() {
        return bandera;
    }

    public void setBandera(int bandera) {
        this.bandera = bandera;
    }

    public TagView getTagGroup() {
        return tagGroup;
    }

    public void setTagGroup(TagView tagGroup) {
        this.tagGroup = tagGroup;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getBusquedaCategoria() {
        return busquedaCategoria;
    }

    public void setBusquedaCategoria(String busquedaCategoria) {
        this.busquedaCategoria = busquedaCategoria;
    }

    public String getBusquedaCiudad() {
        return busquedaCiudad;
    }

    public void setBusquedaCiudad(String busquedaCiudad) {
        this.busquedaCiudad = busquedaCiudad;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FOTOPERFIL && resultCode == RESULT_OK) {
            String imagen = data.getStringExtra("imagen");
            Glide.with(getApplicationContext())
                    .load(imagen)
                    .fitCenter()
                    .into(imageViewFotoPerfil);
        }
    }
}
