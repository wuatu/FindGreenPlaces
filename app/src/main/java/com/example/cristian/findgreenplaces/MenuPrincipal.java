package com.example.cristian.findgreenplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Spinner;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import Clases.AtractivoTuristico;
import Clases.Categoria;
import Clases.IdUsuario;


public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,LocationListener,GoogleApiClient.OnConnectionFailedListener, Serializable {
    Address addres=null;
    boolean buscarPorCategoria=false;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    AutoCompleteTextView buscarEditText;
    ArrayList<String> keysAtractivosTuristicos;
    ArrayList<AtractivoTuristico> atractivoTuristicos;
    AtractivoTuristico atractivoTuristico;
    private static String PREFS_KEY = "mispreferencias";
    private static String SESIONINICIADA = "estado.sesion";
    private static String IDUSUARIO = "mispreferencias2";
    private final int REQUEST_ACCESS_FINE=0;
    Spinner spinner;
    LinearLayout linearLayoutFocus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ACCESS_FINE);

        }
        linearLayoutFocus=findViewById(R.id.layoutfocus);
        spinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.opciones,android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        buscarEditText = (AutoCompleteTextView) findViewById(R.id.autocomplete_region);
        final float density=getResources().getDisplayMetrics().density;
        final Drawable drawable= getResources().getDrawable(R.drawable.lupa);
        final int width=Math.round(25*density);
        final int height=Math.round(25*density);
        drawable.setBounds(0,0,width,height);
        buscarEditText.setCompoundDrawables(drawable,null,null,null);
        //drawable.setBounds(0,0,50,50);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IdUsuario.getIdUsuario().equalsIgnoreCase("invitado")){
                    Toast.makeText(MenuPrincipal.this,"Debe registrarse para agregar atractivo turistico!",Toast.LENGTH_SHORT).show();
                }else {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                    Intent intent = new Intent(MenuPrincipal.this, AgregarAtractivoTuristico.class);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.textViewNombre);
        nav_user.setText(IdUsuario.getNombreUsuario()+" "+IdUsuario.getApellidoUsuario());
        TextView correo=hView.findViewById(R.id.textViewCorreo);
        correo.setText(IdUsuario.getCorreo());
        ImageView imageViewFotoPerfil=hView.findViewById(R.id.imageViewFotoPerfil);
        if(IdUsuario.getUrl().equals("")) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.com_facebook_profile_picture_blank_square)
                    .fitCenter()
                    .into(imageViewFotoPerfil);
        }else{
            Glide.with(getApplicationContext())
                    .load(IdUsuario.getUrl())
                    .fitCenter()
                    .into(imageViewFotoPerfil);

        }
        imageViewFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuPrincipal.this,FotoPerfil.class);
                startActivity(intent);

            }
        });

        /*TextView textViewNombre=navigationView.findViewById(R.id.textViewNombre);
        TextView textViewCorreo=navigationView.findViewById(R.id.textViewCorreo);
        textViewNombre.setText("as");
        textViewCorreo.setText(IdUsuario.getCorreo());*/

        navigationView.setNavigationItemSelectedListener(this);
        Log.v("taza2",IdUsuario.getNombreUsuario());

        if (IdUsuario.getIdUsuario() ==null){
            ejecutarLoginActivity();
        }
        /*buscarEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    buscar();
                    buscarEditText.clearFocus();
                    linearLayoutFocus.requestFocus();
                    //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    dismissKeyboard(buscarEditText,MenuPrincipal.this);
                    Toast.makeText(MenuPrincipal.this,"Ciudad encontrada!",Toast.LENGTH_SHORT).show();
                    //buscarEditText.setCursorVisible(false);
                    buscarEditText.setFocusable(false);
                    buscarEditText.setFocusableInTouchMode(true);
                    return true;
                }
                return false;
            }

        });*/
        buscarEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    buscarEditText.setFocusable(false);
                    buscarEditText.setFocusableInTouchMode(true);
                    buscar();
                    return true;
                } else {
                    return false;
                }
            }
        });
        // Arreglo con las regiones
        String[] regions = getResources().getStringArray(R.array.region_array);
        // Le pasamos las regiones al adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, regions);
        // finalmente le asignamos el adaptador a nuestro elemento
        buscarEditText.setAdapter(adapter);
        atractivoTuristicos=new ArrayList();
        //buscarEditText=findViewById(R.id.editTextBuscar);
        keysAtractivosTuristicos =new ArrayList();
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }
    public static void dismissKeyboard(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void logout() {
        guardarValorBoolean(MenuPrincipal.this,SESIONINICIADA,false);
        guardarValorString(MenuPrincipal.this,IDUSUARIO,"");
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
        return  preferences.getString(keyPref, "");
    }
    public static boolean leerValorBoolean(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return  preferences.getBoolean(keyPref, false);
    }


    private void ejecutarLoginActivity() {
        Intent intent=new Intent(this,Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void buscar(){
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("por ciudad")){
            Geocoder geo = new Geocoder(MenuPrincipal.this);
            int maxResultados = 1;
            List<Address> adress = null;
            try {
                adress = geo.getFromLocationName(buscarEditText.getText().toString(), maxResultados);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(adress!=null) {
                if(adress.size()>0){
                    LatLng latLng = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
                    addres = adress.get(0);
                }
                else{
                    Toast.makeText(MenuPrincipal.this,"No se encuenta ciudad!",Toast.LENGTH_SHORT).show();
                }
            }

        }
        if(spinner.getSelectedItem().toString().equalsIgnoreCase("categoria/etiqueta")){
            mMap.clear();
            String textoBusqueda=buscarEditText.getText().toString();
            getkeyAtractivoTuristico(textoBusqueda);
            buscarAtractivoTuristicoPorCategoria();
            if(!buscarPorCategoria){
                Toast.makeText(MenuPrincipal.this,"No existen categorias asociadas!",Toast.LENGTH_SHORT).show();
            }
        }

    }
    /*public Address buscarPorNombreCiudad(){
        buscarEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            LatLng latLng=null;
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Geocoder geo = new Geocoder(MenuPrincipal.this);
                int maxResultados = 1;
                List<Address> adress = null;
                try {
                    adress = geo.getFromLocationName(buscarEditText.getText().toString(), maxResultados);
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
    public void getkeyAtractivoTuristico(final String texto){
        mDatabase.child("categoriaAtractivoTuristico").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        Log.v("quee",dataSnapshot2.getKey());
                        Log.v("quee",dataSnapshot2.getValue().toString());
                        Categoria categoria=dataSnapshot2.getValue(Categoria.class);
                        Log.v("quee",categoria.getEtiqueta());
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
    public void buscarAtractivoTuristicoPorCategoria(){
        mDatabase.child("atractivoturistico").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(String keyAtractivoTuristico:keysAtractivosTuristicos){
                    Query q=mDatabase.child("atractivoTuristico").child(keyAtractivoTuristico);
                    Log.v("quee",q.getPath().toString());
                    Log.v("quee",q.getRef().toString());

                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AtractivoTuristico atractivoTuristico=dataSnapshot.getValue(AtractivoTuristico.class);
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
    public void repintarMapaConFiltroDeBusqueda(AtractivoTuristico atractivoTuristico){
        double latitud,longitud;
        latitud=atractivoTuristico.getLatitud();
        longitud=atractivoTuristico.getLongitud();
        LatLng sydney = new LatLng(latitud,longitud);
        mMap.addMarker(new MarkerOptions().position(sydney).title(atractivoTuristico.getNombre()));
        if(atractivoTuristico!=null){
            buscarPorCategoria=true;
        }
    }



    public void mostrarAtractivoTuristico(){
        Query q=mDatabase.child("atractivoTuristico");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    AtractivoTuristico atractivoTuristico =dataSnapshot1.getValue(AtractivoTuristico.class);
                    double latitud,longitud;
                    latitud=atractivoTuristico.getLatitud();
                    longitud=atractivoTuristico.getLongitud();
                    LatLng sydney = new LatLng(latitud,longitud);
                    Marker marker;
                    marker=mMap.addMarker(new MarkerOptions().position(sydney).title(atractivoTuristico.getNombre()));
                    marker.showInfoWindow();

                    atractivoTuristicos.add(atractivoTuristico);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    public AtractivoTuristico buscarAtractivoTuristicoEnArrayListPorNombre(Marker marker){
        for (AtractivoTuristico atractivoTuristicos: atractivoTuristicos){
            if(marker.getTitle().equalsIgnoreCase(atractivoTuristicos.getNombre())){
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
                atractivoTuristico =buscarAtractivoTuristicoEnArrayListPorNombre(marker);
                Intent intent = new Intent(MenuPrincipal.this, DialogoVisualizarAtractivoTuristico.class);
                Log.v("seee",atractivoTuristico.getNombre());
                intent.putExtra("atractivoTuristico", atractivoTuristico);
                startActivity(intent);
                return false;
            }
        });


        mostrarAtractivoTuristico();
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng latLng;
                            latLng=new LatLng(location.getLatitude(),location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14.0f));
                            Log.v("quepasa","siiii");
                        }
                        else{
                            Log.v("quepasa","noooo");
                        }
                    }
                });

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

    @Override
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

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
