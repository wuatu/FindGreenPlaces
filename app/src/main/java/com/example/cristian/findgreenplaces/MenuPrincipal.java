package com.example.cristian.findgreenplaces;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.facebook.AccessToken;
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
import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Clases.AtractivoTuristico;
import Clases.Categoria;
import Clases.IdUsuario;
import Clases.Referencias;


public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener, Serializable {
    Address addres = null;
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
    String busqueda = "";
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        final float density = getResources().getDisplayMetrics().density;
        final Drawable drawable = getResources().getDrawable(R.drawable.lupa);
        final int width = Math.round(25 * density);
        final int height = Math.round(25 * density);
        drawable.setBounds(0, 0, width, height);

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
        final String[] regions = getResources().getStringArray(R.array.region_array);


        buscarEditText = findViewById(R.id.autocomplete_region);

        buscarEditText.setFocusable(false);
        buscarEditText.setIconified(true);
        buscarEditText.setQueryHint("Buscar");

        buscarEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //mostrarAtractivoTuristicoPorCiudadOComuna(query);
                buscar(query);
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
        int i=0;
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

                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        tagGroup = (TagView)findViewById(R.id.tag_group);
        //set click listener
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int i) {
                tagGroup.remove(i);
                contadorCategorias--;
                spinner.setSelection(0);

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




    public void buscar(String query) {
        final String busqueda = limpiarAcentos(query);
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("sin filtros")) {
            mMap.clear();
            mostrarAtractivoTuristicoPorCiudadOComuna(busqueda);
        }
        if(spinner.getSelectedItem().toString().equalsIgnoreCase("por nombre")){
            mMap.clear();
            mDatabase.child(Referencias.ATRACTIVOTURISTICO).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        AtractivoTuristico atractivoTuristico=dataSnapshot1.getValue(AtractivoTuristico.class);
                        if(atractivoTuristico.getVisible().equalsIgnoreCase(Referencias.VISIBLE)){
                            if(atractivoTuristico.getNombre().equalsIgnoreCase(busqueda)){
                                LatLng latLng;
                                latLng = new LatLng(atractivoTuristico.getLatitud(), atractivoTuristico.getLongitud());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("categoria")) {
            mMap.clear();
            getkeyAtractivoTuristico(busqueda);
            buscarAtractivoTuristicoPorCategoria();
            if (buscarPorCategoria) {
                Toast.makeText(MenuPrincipal.this, "No existen categorias", Toast.LENGTH_SHORT).show();
            }
        }
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("mas me gusta")) {
            mMap.clear();
            getMasMeGustaAtractivoTuristico();
        }
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("mejor evaluado")) {
            mMap.clear();
            getMejorCalificadosAtractivoTuristico();
        }
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("mas visto")) {
            mMap.clear();
            getMasVistosAtractivoTuristico();
        }


    }



    public void getMasMeGustaAtractivoTuristico(){
        int i=0;
        HashMap<Integer,AtractivoTuristico> map=new HashMap<>();
        for(AtractivoTuristico atractivoTuristico: atractivoTuristicos){
            map.put(Integer.valueOf(atractivoTuristico.getContadorMeGusta()),atractivoTuristico);
        }
        List<Integer> megusta = new ArrayList<>(map.keySet());
        Collections.sort(megusta);

        for (int j=atractivoTuristicos.size()-1; j>=0;j--){
            AtractivoTuristico atractivoTuristico=map.get(megusta.get(j));
            repintarMapaConFiltroDeBusqueda(atractivoTuristico);
            i++;
            if(i>10){
                break;
            }
        }

    }

    public void getMejorCalificadosAtractivoTuristico(){
        int i=0;
        HashMap<Integer,AtractivoTuristico> map=new HashMap<>();
        for(AtractivoTuristico atractivoTuristico: atractivoTuristicos){
            map.put(Integer.valueOf(atractivoTuristico.getCalificacion()),atractivoTuristico);
        }
        List<Integer> megusta = new ArrayList<>(map.keySet());
        Collections.sort(megusta);

        for (int j=atractivoTuristicos.size()-1; j>=0;j--){
            AtractivoTuristico atractivoTuristico=map.get(megusta.get(j));
            repintarMapaConFiltroDeBusqueda(atractivoTuristico);
            i++;
            if(i>10){
                break;
            }
        }

    }

    public void getMasVistosAtractivoTuristico(){
        int i=0;
        HashMap<Integer,AtractivoTuristico> map=new HashMap<>();
        for(AtractivoTuristico atractivoTuristico: atractivoTuristicos){
            map.put(Integer.valueOf(atractivoTuristico.getContadorVisualizaciones()),atractivoTuristico);
        }
        List<Integer> megusta = new ArrayList<>(map.keySet());
        Collections.sort(megusta);

        for (int j=atractivoTuristicos.size()-1; j>=0;j--){
            AtractivoTuristico atractivoTuristico=map.get(megusta.get(j));
            repintarMapaConFiltroDeBusqueda(atractivoTuristico);
            i++;
            if(i>10){
                break;
            }
        }

    }

    public static void dismissKeyboard(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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
        }
        return limpio;
    }

    public void mostrarAtractivoTuristicoPorCiudadOComunaInicio(String busqueda) {
        final String ciudadLimpio = limpiarAcentos(busqueda);
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
                        if (limpiaCiudad.equalsIgnoreCase(ciudadLimpio) || limpiaRegion.equalsIgnoreCase(ciudadLimpio)) {
                            double latitud, longitud;
                            latitud = atractivoTuristico.getLatitud();
                            longitud = atractivoTuristico.getLongitud();
                            LatLng sydney = new LatLng(latitud, longitud);
                            Marker marker;
                            marker = mMap.addMarker(new MarkerOptions().
                                    position(sydney).
                                    icon(bitmapDescriptorFromVector(MenuPrincipal.this, R.drawable.marcador, atractivoTuristico.getCalificacion())).
                                    title(atractivoTuristico.getNombre()));
                            //marker.showInfoWindow();
                            atractivoTuristicos.add(atractivoTuristico);
                        }
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId, String scalificacion) {
        double calificacion=Double.valueOf(scalificacion);
        double alto=60;
        if(calificacion==2){alto=alto*1.3;}
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

    public void mostrarAtractivoTuristicoPorCiudadOComuna(String busqueda) {
        final String ciudadLimpio = limpiarAcentos(busqueda);
        Query q = mDatabase.child("atractivoTuristico");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Geocoder geo = new Geocoder(MenuPrincipal.this);
                int maxResultados = 1;
                List<Address> adress = null;

                try {
                    adress = geo.getFromLocationName(ciudadLimpio, maxResultados);

                    Log.v("paco",adress.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (adress != null) {
                    if (adress.size() > 0) {
                        LatLng latLng = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f));
                        addres = adress.get(0);

                    } else {
                        Toast.makeText(MenuPrincipal.this, "No se encuenta ciudad!", Toast.LENGTH_SHORT).show();
                    }
                }
                atractivoTuristicos.clear();
                mMap.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    AtractivoTuristico atractivoTuristico = dataSnapshot1.getValue(AtractivoTuristico.class);
                    String limpiaCiudad=limpiarAcentos(atractivoTuristico.getCiudad());
                    String limpiaRegion=limpiarAcentos(atractivoTuristico.getComuna());
                    if(atractivoTuristico.getVisible().equalsIgnoreCase("visible")) {
                        if (limpiaCiudad.equalsIgnoreCase(ciudadLimpio) || limpiaRegion.equalsIgnoreCase(ciudadLimpio)) {
                            double latitud, longitud;
                            latitud = atractivoTuristico.getLatitud();
                            longitud = atractivoTuristico.getLongitud();
                            LatLng sydney = new LatLng(latitud, longitud);
                            Marker marker;
                            marker = mMap.addMarker(new MarkerOptions().position(sydney).
                                    icon(bitmapDescriptorFromVector(MenuPrincipal.this, R.drawable.marcador, atractivoTuristico.getCalificacion())).
                                    title(atractivoTuristico.getNombre()));
                            //marker.showInfoWindow();
                            atractivoTuristicos.add(atractivoTuristico);
                        }
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getkeyAtractivoTuristico(final String texto) {
        mDatabase.child("categoriaAtractivoTuristico").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        Log.v("quee", dataSnapshot2.getKey());
                        Log.v("quee", dataSnapshot2.getValue().toString());
                        Categoria categoria = dataSnapshot2.getValue(Categoria.class);
                        Log.v("quee", categoria.getEtiqueta());
                        if (texto.equalsIgnoreCase(categoria.getEtiqueta())) {
                            keysAtractivosTuristicos.add(dataSnapshot1.getKey());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void buscarAtractivoTuristicoPorCategoria() {
        mDatabase.child("atractivoturistico").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String keyAtractivoTuristico : keysAtractivosTuristicos) {
                    Query q = mDatabase.child("atractivoTuristico").child(keyAtractivoTuristico);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(atractivoTuristico.getVisible().equalsIgnoreCase(Referencias.VISIBLE)) {
                                AtractivoTuristico atractivoTuristico = dataSnapshot.getValue(AtractivoTuristico.class);
                                repintarMapaConFiltroDeBusqueda(atractivoTuristico);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                keysAtractivosTuristicos.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void repintarMapaConFiltroDeBusqueda(AtractivoTuristico atractivoTuristico) {
        double latitud, longitud;
        latitud = atractivoTuristico.getLatitud();
        longitud = atractivoTuristico.getLongitud();
        LatLng sydney = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(sydney).title(atractivoTuristico.getNombre()));
        if (atractivoTuristico != null) {
            buscarPorCategoria = true;
        }
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
                                    mostrarAtractivoTuristicoPorCiudadOComunaInicio(adress.get(0).getLocality());
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

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

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

    public void actualizaFotoPerfil(){
        Glide.with(getApplicationContext())
                .load(IdUsuario.getUrl())
                .fitCenter()
                .into(imageViewFotoPerfil);
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
