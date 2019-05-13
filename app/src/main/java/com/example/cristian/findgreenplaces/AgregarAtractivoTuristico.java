package com.example.cristian.findgreenplaces;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Clases.AtractivoTuristico;
import Clases.Categoria;
import Clases.CurrentDate;
import Clases.IdUsuario;
import Clases.Imagen;
import Clases.Referencias;

public class AgregarAtractivoTuristico extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,LocationListener,GoogleApiClient.OnConnectionFailedListener,Serializable {
    Address addres = null;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    StorageReference mStorageReference;
    Button addCategoria;
    AutoCompleteTextView textViewCategoria;
    LinearLayout contenedorAddAT;
    LinearLayout contenedorCategoria;
    Marker currentMarker;
    //LinearLayout contenedorTresCategorias;
    int i=0;
    int contadorCategorias=0;
    TextView textViewNombre;
    TextView textViewCiudad;
    TextView textViewComuna;
    TextView textViewDescripcion;
    TextView textViewDireccion;
    MarkerOptions marker;
    Button buttonAceptar;
    Button buttonCancelar;
    TextView tituloCategoria;
    ArrayList<Tag> sCategorias;
    TagView tagGroup;
    ArrayList<Categoria> categorias;
    ImageButton botonSubirImagen;
    private static final int GALLERY_INTENT=1;
    private ImageView imageView;
    private ProgressDialog progressDialog;
    ArrayList imagenes;
    private static final int INTENT_EXTRA_IMAGES=1;
    String urlImagen;
    String keyAtractivoTuristico;
    private final int REQUEST_ACCESS_READ_EXTERNAL_STORAGE=0;
    private final int REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE=0;
    String busqueda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_agregar_atractivo_turistico);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_ACCESS_READ_EXTERNAL_STORAGE);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE);

        }

        // Referencia al elemento en la vista
        textViewCategoria = (AutoCompleteTextView) findViewById(R.id.autocomplete_region);

        busqueda= ((String) getIntent().getStringExtra("busqueda"));


        tituloCategoria=findViewById(R.id.categoriasTitulo);
        imagenes=new ArrayList();
        imageView=findViewById(R.id.mostrarImagen);
        progressDialog=new ProgressDialog(AgregarAtractivoTuristico.this);
        botonSubirImagen=findViewById(R.id.buttonSubirImagen);
        mStorageReference=FirebaseStorage.getInstance().getReference();
        marker=new MarkerOptions();
        tagGroup = (TagView)findViewById(R.id.tag_group);
        sCategorias =new ArrayList();
        categorias=new ArrayList<>();
        buttonAceptar=(Button)findViewById(R.id.buttonAceptar);
        buttonCancelar=(Button)findViewById(R.id.buttonCancelar);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();

        mDatabase.child(Referencias.KEYSATRACTIVOTURISTICO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> keys=new ArrayList<>();
                Log.v("casi",dataSnapshot.getKey().toString());
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String key= dataSnapshot1.getKey();
                    keys.add(key);
                }
                // Arreglo con las regiones
                String[] regions=new String[keys.size()];
                int i=0;
                for(String string : keys){
                    regions[i]=string;
                    i++;
                }
                // Le pasamos las regiones al adaptador
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarAtractivoTuristico.this, android.R.layout.simple_list_item_1, regions);
                // finalmente le asignamos el adaptador a nuestro elemento
                textViewCategoria.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        contenedorAddAT=(LinearLayout) findViewById(R.id.contenedorAddAtractivoT);
        contenedorCategoria=(LinearLayout) findViewById(R.id.contenedorCategorias);
        addCategoria= (Button) findViewById(R.id.buttonaddCategoria);


    }

    public void subirImagen(){

        botonSubirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageView.setImageResource();
                imageView.setVisibility(View.VISIBLE);
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //startActivityForResult(intent,GALLERY_INTENT);
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);U//para subir multiples item_photo
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent,"Selecciona Fotos"), 1);
                //intent.putParcelableArrayListExtra("item_photo",item_photo);
                startActivityForResult(intent,INTENT_EXTRA_IMAGES);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
            progressDialog.setTitle("Subiendo Foto");
            progressDialog.setMessage("Subiendo Foto a Base de Datos");
            progressDialog.setCancelable(true);
            progressDialog.show();

            Toolbar toolbar=findViewById(R.id.toolbar_camera);
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
            TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
            textView.setText("Agregar Nuevo Atractivo Turístico");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            //item_photo = data.getParcelableArrayListExtra("item_photo");
            //Uri[] uri=new Uri[item_photo.size()];
            Uri uri=data.getData();
            //for (int i =0 ; i < item_photo.size(); i++) {
            final StorageReference direccion=mStorageReference.child("fotos").child(uri.getLastPathSegment());
            Task<Uri> urlTask = direccion.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return direccion.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Uri downloadUri = task.getResult();
                        urlImagen = downloadUri.toString();
                        Log.v("descargar",urlImagen);
                        Glide.with(AgregarAtractivoTuristico.this)
                                .load(urlImagen)
                                .fitCenter()
                                .centerCrop()
                                .into(botonSubirImagen);
                        Toast.makeText(AgregarAtractivoTuristico.this,"La foto se subio exitosamente!",Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
        //}
    }

    public void agregarCategorias(){
        addCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textViewCategoria= (AutoCompleteTextView) findViewById(R.id.autocomplete_region);

                if(!textViewCategoria.getText().toString().isEmpty())
                {
                    tituloCategoria.setVisibility(View.VISIBLE);
                    String sX="   x";
                    String sCategoria=textViewCategoria.getText().toString();
                    Categoria categoria=new Categoria(sCategoria);
                    categorias.add(categoria);
                    sCategoria=sCategoria.concat(sX);
                    Tag tag=new Tag(sCategoria);
                    tagGroup.addTag(tag);
                    contadorCategorias++;
                    textViewCategoria.setText("");

                }
                else {
                    Snackbar.make(v, "Debe Agregar al Menos una Categoria", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        //set click listener
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int i) {
                tagGroup.remove(i);
                contadorCategorias--;
                categorias.remove(i);
            }
        });
        //set delete listener
        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
            }
        });



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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
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

    public void agregarAtractivoTuristico(){
        textViewNombre=(TextView)findViewById(R.id.editTextNombre);
        textViewCiudad=(TextView)findViewById(R.id.editTextCiudad);
        textViewComuna=(TextView)findViewById(R.id.editTextComuna);
        textViewDescripcion=(TextView)findViewById(R.id.editTextDescripcion);
        textViewDireccion=(TextView)findViewById(R.id.editTextDireccion);
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewNombre.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Debe Agregar Nombre!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if(textViewCiudad.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Debe Agregar Ciudad!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if(textViewComuna.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Debe Agregar Comuna!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if(textViewDescripcion.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Debe Agregar Descripcion!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if(contadorCategorias==0) {
                    Snackbar.make(v, "Debe Agregar al Menos una Categoria!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if(marker.getPosition()==null){
                    Snackbar.make(v, "Debe Agregar al Menos un Marcador de Ubicacion!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    //agrega las categorias de ciudad y region
                    if(!textViewCiudad.getText().toString().isEmpty())
                    {
                        tituloCategoria.setVisibility(View.VISIBLE);
                        String sX="   x";
                        String sCategoria=textViewCiudad.getText().toString();
                        Categoria categoria=new Categoria(sCategoria);
                        categorias.add(categoria);
                        sCategoria=sCategoria.concat(sX);
                        Tag tag=new Tag(sCategoria);
                        tagGroup.addTag(tag);
                        contadorCategorias++;
                        textViewCategoria.setText("");

                    }
                    //agrega las categorias de ciudad y region
                    if(!textViewComuna.getText().toString().isEmpty())
                    {
                        tituloCategoria.setVisibility(View.VISIBLE);
                        String sX="   x";
                        String sCategoria=textViewComuna.getText().toString();
                        Categoria categoria=new Categoria(sCategoria);
                        categorias.add(categoria);
                        sCategoria=sCategoria.concat(sX);
                        Tag tag=new Tag(sCategoria);
                        tagGroup.addTag(tag);
                        contadorCategorias++;
                        textViewCategoria.setText("");

                    }

                    new AlertDialog.Builder(AgregarAtractivoTuristico.this)
                            .setTitle("Nuevo Atractivo Turístico")
                            .setMessage("Esta seguro que quiere añadir un nuevo atractivo turístico?")
                            //.setIcon(R.drawable.aporte)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                    String nombre=textViewNombre.getText().toString();
                    String ciudad=textViewCiudad.getText().toString();
                    String comuna=textViewComuna.getText().toString();
                    String descripcion=textViewDescripcion.getText().toString();
                    Double latitud=marker.getPosition().latitude;
                    Double longitud=marker.getPosition().longitude;


                    DatabaseReference databaseReference= mDatabase.child("atractivoTuristico").push();
                    keyAtractivoTuristico =databaseReference.getKey();
                    AtractivoTuristico atractivoTuristico=new AtractivoTuristico(keyAtractivoTuristico,IdUsuario.getIdUsuario(),nombre,ciudad,comuna,descripcion,latitud,longitud,"0","0");
                    databaseReference.setValue(atractivoTuristico, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            for (Categoria categoria: categorias){
                                /*DatabaseReference dbrcategoria= mDatabase.child("keysAtractivosTuristicos").push();
                                String keyCategoria=dbrcategoria.getKey();
                                dbrcategoria.setValue(categoria);
                                categoria.setId(keyCategoria);
                                mDatabase.child("categoriaAtractivoTuristico").child(keyAtractivoTuristico).child(keyCategoria).setValue(categoria);*/
                                DatabaseReference dbrcategoria= mDatabase.child(Referencias.KEYSATRACTIVOTURISTICO).child(categoria.getEtiqueta()).push();
                                String keyCategoria2=dbrcategoria.getKey();
                                dbrcategoria.setValue(categoria);
                                categoria.setId(keyCategoria2);
                                mDatabase.child(Referencias.CATEGORIAATRACTIVOTURISTICO).child(keyAtractivoTuristico).child(keyCategoria2).setValue(categoria);
                            }
                            DatabaseReference databaseReference2=mDatabase.child(Referencias.IMAGENES).child(keyAtractivoTuristico).push();
                            String key =databaseReference2.getKey();
                            Date c = Calendar.getInstance().getTime();
                            String fecha=CurrentDate.CurrentDate(c);
                            Imagen imagen=new Imagen(key,urlImagen,fecha,IdUsuario.getIdUsuario(),IdUsuario.getNombreUsuario()+" "+IdUsuario.getApellidoUsuario(),"0","0");
                            databaseReference2.setValue(imagen);
                        }
                    });
                    mDatabase.child(Referencias.CONTRIBUCIONES).child(IdUsuario.getIdUsuario()).child(Referencias.ATRACTIVOTURISTICO).child(keyAtractivoTuristico).setValue(atractivoTuristico);
                    //mDatabase.child(Referencias.)

                                    Intent intent = new Intent(AgregarAtractivoTuristico.this, MenuPrincipal.class);
                                    startActivity(intent);
                                    Toast.makeText(AgregarAtractivoTuristico.this,"El atractivo turístico se subio exitosamente!",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();


                    /*new AlertDialog.Builder(AgregarAtractivoTuristico.this)
                            .setTitle("Nuevo Atractivo Turístico")
                            .setMessage("Esta seguro que quiere añadir un nuevo atractivo turístico?")
                            //.setIcon(R.drawable.aporte)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();*/




                    /*AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(AgregarAtractivoTuristico.this, android.R.style.Theme_Material_Dialog);
                    } else {
                        builder = new AlertDialog.Builder(AgregarAtractivoTuristico.this);
                    }
                    builder.setTitle("Nuevo Atractivo Agregado")
                            .setMessage("Atractivo Turistico Añadido con Exito!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(AgregarAtractivoTuristico.this, MenuPrincipal.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();*/
                }
            }
        });
    }

    public void cancelarAgregarAtractivoTuristico(){
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AgregarAtractivoTuristico.this)
                            .setTitle("Información")
                            .setMessage("Seguro Quieres Cancelar?")
                            //.setIcon(R.drawable.aporte)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(AgregarAtractivoTuristico.this, MenuPrincipal.class);
                                    startActivity(intent);

                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
            }
        });
    }

    public void getStringAddress(Double lat,Double lng){
        String direccion="";
        String ciudad="";
        String region="";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder=new Geocoder(this,Locale.getDefault());
        try {
            addresses=geocoder.getFromLocation(lat,lng,1);
            //addres=addresses.get(0).getAddressLine(0);
            ciudad=addresses.get(0).getLocality();//curico
            //city=addresses.get(0).getCountryName();//Chile
            region=addresses.get(0).getAdminArea();//Region
            direccion=addresses.get(0).getThoroughfare()+" "+addresses.get(0).getFeatureName();//Region
            textViewCiudad.setText(ciudad);
            textViewComuna.setText(region);
            textViewDireccion.setText(direccion);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mGoogleApiClient.connect();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center=mMap.getCameraPosition().target;

                if(currentMarker==null){
                    currentMarker=mMap.addMarker(marker.position(center).title("Nueva posición"));
                    currentMarker.setVisible(false);
                }
                else{

                    currentMarker.remove();
                    currentMarker=mMap.addMarker(marker.position(center).title("Nueva posición"));
                    currentMarker.setVisible(false);
                    LatLng latLng=currentMarker.getPosition();
                    getStringAddress(latLng.latitude,latLng.longitude);

                }



            }
        });



        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
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
                        }
                    }
                });

        busquedaPorCiudadORegion();
        agregarAtractivoTuristico();
        cancelarAgregarAtractivoTuristico();
        agregarCategorias();
        subirImagen();
    }

    public void busquedaPorCiudadORegion(){
        Geocoder geo = new Geocoder(AgregarAtractivoTuristico.this);
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
                Toast.makeText(AgregarAtractivoTuristico.this, "No se encuenta ciudad!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

}
