package com.example.cristian.findgreenplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import java.util.ArrayList;
import java.util.List;

import Clases.Models.AtractivoTuristico;
import Clases.Models.Categoria;
import Clases.Utils.IdUsuario;

public class Buscar_Maps extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener, Serializable {
    Address addres = null;
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
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_maps);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE);

        }
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        //banner de publicidad
/*        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        busqueda= ((String) getIntent().getStringExtra("busqueda"));
        linearLayoutFocus = findViewById(R.id.layoutfocus);
        //spinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.opciones, android.R.layout.simple_spinner_item);
        //spinner.setAdapter(adapter1);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("AhoraTurismo");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        buscarEditText = findViewById(R.id.autocomplete_region);
        buscarEditText.setQueryHint("Buscar");
        final float density = getResources().getDisplayMetrics().density;
        final Drawable drawable = getResources().getDrawable(R.drawable.lupa);
        final int width = Math.round(25 * density);
        final int height = Math.round(25 * density);
        drawable.setBounds(0, 0, width, height);
        //searchViewBuscar.setCompoundDrawables(drawable,null,null,null);
        //drawable.setBounds(0,0,50,50);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")) {
                    Toast.makeText(Buscar_Maps.this, "Debe registrarse para agregar atractivo turistico!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(Buscar_Maps.this, AgregarAtractivoTuristico.class);
                    intent.putExtra("busqueda", busqueda);
                    startActivity(intent);
                    //setContentView(R.layout.activity_dialogo_visualizar_atractivo_turistico);
                }
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
                Intent intent = new Intent(Buscar_Maps.this, FotoPerfil.class);
                startActivity(intent);
            }
        });

        /*TextView textViewNombre=navigationView.findViewById(R.id.textViewNombre);
        TextView textViewCorreo=navigationView.findViewById(R.id.textViewCorreo);
        textViewNombre.setText("as");
        textViewCorreo.setText(IdUsuario.getCorreo());*/

        navigationView.setNavigationItemSelectedListener(this);
        Log.v("taza2", IdUsuario.getNombreUsuario());

        if (IdUsuario.getIdUsuario() == null) {
            ejecutarLoginActivity();
        }
        /*searchViewBuscar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    buscaAtractivosTuristicosConOsinFiltro();
                    searchViewBuscar.clearFocus();
                    linearLayoutFocus.requestFocus();
                    //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    dismissKeyboard(searchViewBuscar,MenuPrincipal.this);
                    Toast.makeText(MenuPrincipal.this,"Ciudad encontrada!",Toast.LENGTH_SHORT).show();
                    //searchViewBuscar.setCursorVisible(false);
                    searchViewBuscar.setFocusable(false);
                    searchViewBuscar.setFocusableInTouchMode(true);
                    return true;
                }
                return false;
            }

        });*/

        buscarEditText.setIconified(false);
        //searchViewBuscar.setFocusable(true);
        buscarEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                buscarEditText.setQuery("", false);
                buscarEditText.setIconified(true);
                buscar();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });

        // Arreglo con las regiones
        String[] regions = getResources().getStringArray(R.array.region_array);
        // Le pasamos las regiones al adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, regions);
        // finalmente le asignamos el adaptador a nuestro elemento

        String[] columnNames = {"_id","text"};
        MatrixCursor cursor = new MatrixCursor(columnNames);
        String[] array = getResources().getStringArray(R.array.region_array); //if strings are in resources
        String[] temp = new String[2];
        int id = 0;
        for(String item : array){

            temp[0] = Integer.toString(id++);
            temp[1] = item;
            cursor.addRow(temp);

        }

        String[] from = {"text"};
        int[] to = {android.R.id.text1};
        CursorAdapter  busStopCursorAdapter = new SimpleCursorAdapter(Buscar_Maps.this, android.R.layout.simple_list_item_1, cursor, from, to);
        buscarEditText.setSuggestionsAdapter(busStopCursorAdapter);


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

    }

    public static void dismissKeyboard(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void logout() {
        guardarValorBoolean(Buscar_Maps.this, SESIONINICIADA, false);
        guardarValorString(Buscar_Maps.this, IDUSUARIO, "");
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

    public void buscar() {
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("por ciudad")) {
            Geocoder geo = new Geocoder(Buscar_Maps.this);
            int maxResultados = 1;
            List<Address> adress = null;
            try {
                adress = geo.getFromLocationName(buscarEditText.getQuery().toString(), maxResultados);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (adress != null) {
                if (adress.size() > 0) {
                    LatLng latLng = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
                    addres = adress.get(0);
                } else {
                    Toast.makeText(Buscar_Maps.this, "No se encuenta ciudad!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("categoria/etiqueta")) {
            mMap.clear();
            String textoBusqueda = buscarEditText.getQuery().toString();
            getkeyAtractivoTuristico(textoBusqueda);
            buscarAtractivoTuristicoPorCategoria();
            if (buscarPorCategoria) {
                Toast.makeText(Buscar_Maps.this, "No existen categorias asociadas!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /*public Address buscarPorNombreCiudad(){
        searchViewBuscar.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            LatLng latLng=null;
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Geocoder geo = new Geocoder(MenuPrincipal.this);
                int maxResultados = 1;
                List<Address> adress = null;
                try {
                    adress = geo.getFromLocationName(searchViewBuscar.getText().toString(), maxResultados);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(adress!=null) {
                    latLng = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
                    addres = adress.get(0);
                    return true;
                }
                return false;
            }
        });
        return addres;
    }*/
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
                    Log.v("quee", q.getPath().toString());
                    Log.v("quee", q.getRef().toString());

                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AtractivoTuristico atractivoTuristico = dataSnapshot.getValue(AtractivoTuristico.class);
                            repintarMapaConFiltroDeBusqueda(atractivoTuristico);
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


    public void mostrarAtractivoTuristicoPorCiudadOComuna() {
        Query q = mDatabase.child("atractivoTuristico");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("busqueda",busqueda.toString());
                Log.v("busqueda",dataSnapshot.toString());
                Geocoder geo = new Geocoder(Buscar_Maps.this);
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
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
                        addres = adress.get(0);

                    } else {
                        Toast.makeText(Buscar_Maps.this, "No se encuenta ciudad!", Toast.LENGTH_SHORT).show();
                    }
                }
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    AtractivoTuristico atractivoTuristico = dataSnapshot1.getValue(AtractivoTuristico.class);
                    if(atractivoTuristico.getCiudad().equalsIgnoreCase(busqueda) || atractivoTuristico.getComuna().equalsIgnoreCase(busqueda)) {
                        double latitud, longitud;
                        latitud = atractivoTuristico.getLatitud();
                        longitud = atractivoTuristico.getLongitud();
                        LatLng sydney = new LatLng(latitud, longitud);
                        Marker marker;
                        marker = mMap.addMarker(new MarkerOptions().position(sydney).title(atractivoTuristico.getNombre()));
                        marker.showInfoWindow();
                        atractivoTuristicos.add(atractivoTuristico);
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                AtractivoTuristico atractivoTuristico;
                atractivoTuristico = buscarAtractivoTuristicoEnArrayListPorNombre(marker);
                Intent intent = new Intent(Buscar_Maps.this, DialogoVisualizarAtractivoTuristico.class);
                Log.v("seee", atractivoTuristico.getNombre());
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);
                return false;
            }
        });


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng latLng;
                            latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
                            Log.v("quepasa", "siiii");
                        } else {
                            Log.v("quepasa", "noooo");
                        }
                    }
                });
        mostrarAtractivoTuristicoPorCiudadOComuna();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
            Intent intent=new Intent(Buscar_Maps.this,PerfilUsuario.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent=new Intent(Buscar_Maps.this,VisualizarContribucionAtractivoTuristico.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent=new Intent(Buscar_Maps.this, GalleryFotosUsuarioContribuidas.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
