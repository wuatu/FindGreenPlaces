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
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
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
import com.facebook.login.LoginManager;
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
import Clases.IdUsuario;
import Clases.Referencias;


public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener, Serializable {
    Address addres = null;
    Address currentAddres;
    ArrayList<AtractivoTuristico> atractivosTuristicosTemp;
    String[] regions;
    ImageView imageViewIr;
    boolean buscarPorCategoria = false;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    SearchView buscarEditText;
    ArrayList<String> keysAtractivosTuristicos;
    ArrayList<AtractivoTuristico> atractivoTuristicos;
    AtractivoTuristico atractivoTuristico;
    private static String PREFS_KEY = "mispreferencias";
    private static String SESIONINICIADA = "estado.sesion";
    private static String IDUSUARIO = "mispreferencias2";
    private final int REQUEST_ACCESS_FINE = 0;
    Spinner spinner;
    LinearLayout linearLayoutFocus;
    NavigationView navigationView;
    View hView;
    ImageView imageViewFotoPerfil;
    String busqueda;
    int bandera = 0;
    TagView tagGroup;
    int contadorCategorias = 0;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_maps);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE);

        }
        atractivosTuristicosTemp= new ArrayList<>();
        linearLayoutFocus = findViewById(R.id.layoutfocus);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.opciones, R.layout.layout_item_spinner);
        spinner.setAdapter(adapter1);

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

        if(isOnlineNet()){
            LinearLayout linearLayoutDialogo=findViewById(R.id.layoutDialogo2);
            linearLayoutDialogo.setVisibility(View.GONE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        final float density = getResources().getDisplayMetrics().density;
        final Drawable drawable = getResources().getDrawable(R.drawable.lupa);
        final int width = Math.round(25 * density);
        final int height = Math.round(25 * density);
        drawable.setBounds(0, 0, width, height);

        regions = getResources().getStringArray(R.array.region_array);

        fab.setOnClickListener(new View.OnClickListener() {
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
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        //Log.v("taza2", IdUsuario.getNombreUsuario());

        if (IdUsuario.getIdUsuario() == null) {
            ejecutarLoginActivity();
        }
        // Arreglo con las regiones



        buscarEditText = findViewById(R.id.autocomplete_region);

        buscarEditText.setFocusable(false);
        buscarEditText.setIconified(true);
        buscarEditText.setQueryHint("Buscar");

        buscarEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                busqueda=limpiarAcentos(query);
                Log.v("busqueda",busqueda);
                buscar();
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });
        buscarEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {


                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    return true;
                } else {
                    return false;
                }
            }
        });
        tagGroup = (TagView)findViewById(R.id.tag_group);

        // Le pasamos las regiones al adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, regions);
        // finalmente le asignamos el adaptador a nuestro elemento
        //buscarEditText.setAdapter(adapter);
        atractivoTuristicos = new ArrayList();
        //buscarEditText=findViewById(R.id.editTextBuscar);
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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    tagGroup.removeAll();
                }else {
                    Tag tag = new Tag(spinner.getSelectedItem().toString() + " " + "X");
                    tag.layoutColor = getResources().getColor(R.color.colorPrimary);
                    tagGroup.removeAll();
                    tagGroup.addTag(tag);
                    contadorCategorias++;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //set click listener
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int i) {
                tagGroup.remove(i);
                contadorCategorias--;
                spinner.setSelection(0);

                repintarMapaConArrayList();

            }
        });
        //set delete listener
        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
            }
        });

        imageViewIr=findViewById(R.id.imageViewIr);
        imageViewIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+atractivoTuristico.getLatitud()+","+atractivoTuristico.getLongitud()+"&travelmode=driving"));
                startActivity(intent);
            }
        });
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
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    public void buscar() {
        Log.v("busqueda2",busqueda);
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("sin filtros")) {
            //mMap.clear();
            mostrarAtractivoTuristicoPorCiudadOComuna();
        }
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("categoria")) {
            mMap.clear();
            getkeyAtractivoTuristico(busqueda);
            buscarAtractivoTuristicoPorCategoria();
            if (keysAtractivosTuristicos!=null) {
                if(keysAtractivosTuristicos.size()<=0) {
                    Toast.makeText(MenuPrincipal.this, "No existen categorias", Toast.LENGTH_SHORT).show();
                }
            }
            keysAtractivosTuristicos.clear();
        }
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




    public void repintarMapaConArrayList(){
        if(atractivoTuristicos.size()>0) {
            mMap.clear();
            for (AtractivoTuristico atractivoTuristico: atractivoTuristicos) {
                double latitud, longitud;
                latitud = atractivoTuristico.getLatitud();
                longitud = atractivoTuristico.getLongitud();
                LatLng sydney = new LatLng(latitud, longitud);
                mMap.addMarker(new MarkerOptions().
                        position(sydney).
                        icon(bitmapDescriptorFromVector(MenuPrincipal.this, R.drawable.marcador, atractivoTuristico.getCalificacion(),0)).
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
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    AtractivoTuristico atractivoTuristico = dataSnapshot1.getValue(AtractivoTuristico.class);
                    String limpiaCiudad=limpiarAcentos(atractivoTuristico.getCiudad());
                    String limpiaRegion=limpiarAcentos(atractivoTuristico.getComuna());
                    //Log.v("limon",atractivoTuristico.getCalificacion());
                    if(atractivoTuristico.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                        if (limpiaCiudad.equalsIgnoreCase(busqueda) || limpiaRegion.equalsIgnoreCase(busqueda)) {
                            repintarMapaConFiltroDeBusqueda(atractivoTuristico,0,R.drawable.marcador);
                            atractivoTuristicos.add(atractivoTuristico);
                            atractivosTuristicosTemp.add(atractivoTuristico);
                        }
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
                //Address address=ubicacionAddressMoveCamera();
                //if(address!=null) {
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
                    } else {
                        Toast.makeText(MenuPrincipal.this, "No se encuentan datos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (adress != null) {
                    /*if ((!limpiarAcentos(adress.get(0).getAdminArea()).equalsIgnoreCase(limpiarAcentos(currentAddres.getAdminArea())))
                            || isRegion() || atractivoTuristicos.size()<=0 ||
                    (!limpiarAcentos(adress.get(0).getLocality()).equalsIgnoreCase(limpiarAcentos(currentAddres.getLocality())))) {*/
                        Log.v("busqueda4", busqueda);
                        atractivoTuristicos.clear();
                        atractivosTuristicosTemp.clear();
                        mMap.clear();
                        currentAddres = adress.get(0);
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            AtractivoTuristico atractivoTuristico = dataSnapshot1.getValue(AtractivoTuristico.class);
                            String limpiaCiudad = limpiarAcentos(atractivoTuristico.getCiudad());
                            String limpiaRegion = limpiarAcentos(atractivoTuristico.getComuna());
                            Log.v("busqueda5", limpiaRegion);


                            /*String[] palabras = busqueda.split("\\W+");
                            for (String palabra : palabras) {
                                if (limpiaRegion.contains(palabra)) {

                                    Log.v("busqueda6",limpiaRegion+"asasas");
                                }
                            }*/

                           /* if(limpiaRegion.indexOf(busqueda)!=-1){
                                Log.v("busqueda6",limpiaRegion+"asasas");
                            }*/
                            if (atractivoTuristico.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {

                                if (limpiaCiudad.equalsIgnoreCase(busqueda) || limpiaRegion.equalsIgnoreCase(busqueda) || limpiaRegion.indexOf(busqueda) != -1) {
                                    repintarMapaConFiltroDeBusqueda(atractivoTuristico, 0, R.drawable.marcador);
                                    atractivosTuristicosTemp.add(atractivoTuristico);
                                    atractivoTuristicos.add(atractivoTuristico);
                                }
                            }
                        }
                    /*} else {
                        Log.v("busqueda6", busqueda);
                        //mMap.clear();
                        atractivosTuristicosTemp.clear();
                        for (AtractivoTuristico atractivoTuristico : atractivoTuristicos) {
                            if (atractivoTuristico.getCiudad().equalsIgnoreCase(busqueda) || atractivoTuristico.getComuna().equalsIgnoreCase(busqueda)) {
                                repintarMapaConFiltroDeBusqueda(atractivoTuristico, 0, R.drawable.marcador);
                                atractivosTuristicosTemp.add(atractivoTuristico);
                            }
                        }
                    }*/
                }
            }
            //}


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


    //Obtiene todas las llaves de los atractivos turisticos que coinciden con el criterio buscado
    public void getkeyAtractivoTuristico(final String texto) {
        mDatabase.child("categoriaAtractivoTuristico").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        String categoria=dataSnapshot2.getKey();
                        Log.v("quee", categoria);
                        Log.v("texto", texto);
                        if (texto.equalsIgnoreCase(categoria)) {
                            keysAtractivosTuristicos.add(dataSnapshot1.getKey());
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    //busca los atractivos turisticos por llave
    public void buscarAtractivoTuristicoPorCategoria() {
        mDatabase.child("atractivoturistico").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String keyAtractivoTuristico : keysAtractivosTuristicos) {
                    Query q = mDatabase.child("atractivoTuristico").child(keyAtractivoTuristico);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AtractivoTuristico atractivoTuristico = dataSnapshot.getValue(AtractivoTuristico.class);
                                repintarMapaConFiltroDeBusqueda(atractivoTuristico,0,R.drawable.marcador);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
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
                LinearLayout linearLayoutDialogo=findViewById(R.id.layoutDialogo);
                linearLayoutDialogo.setVisibility(View.GONE);

                LinearLayout linearLayoutDialogo2=findViewById(R.id.layoutDialogo2);
                linearLayoutDialogo2.setVisibility(View.GONE);

                imageViewIr.setVisibility(View.VISIBLE);
                atractivoTuristico = buscarAtractivoTuristicoEnArrayListPorNombre(marker);
                Intent intent = new Intent(MenuPrincipal.this, DialogoVisualizarAtractivoTuristico.class);
                //Log.v("seee", atractivoTuristico.getNombre());
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
                                    mostrarAtractivoTuristicoPorRegionInicio();
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
                LinearLayout linearLayoutDialogo=findViewById(R.id.layoutDialogo);
                linearLayoutDialogo.setVisibility(View.GONE);
                LinearLayout linearLayoutDialogo2=findViewById(R.id.layoutDialogo2);
                linearLayoutDialogo2.setVisibility(View.GONE);
                imageViewIr.setVisibility(View.GONE);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        buscarEditText.setQuery("", false);
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


    @SuppressWarnings("StatementWithEmptyBody")
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
        LoginManager.getInstance().logOut();
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
}
