package com.example.cristian.findgreenplaces;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.chipview.ChipView;
import com.allyants.chipview.SimpleChipAdapter;
import com.bumptech.glide.Glide;
import com.cunoraz.tagview.Constants;
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

import java.util.ArrayList;

import Clases.AtractivoTuristico;
import Clases.Categoria;

public class AgregarAtractivoTuristico extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,LocationListener,GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    DatabaseReference mDatabase;
    StorageReference mStorageReference;
    FirebaseDatabase database;
    Button addCategoria;
    TextView textViewCategoria;
    LinearLayout contenedorAddAT;
    LinearLayout contenedorCategoria;
    //LinearLayout contenedorTresCategorias;
    int i=0;
    int contadorCategorias=0;
    TextView textViewNombre;
    TextView textViewCiudad;
    TextView textViewComuna;
    TextView textViewDescripcion;
    MarkerOptions marker;
    Button buttonAceptar;
    Button buttonCancelar;
    TextView tituloCategoria;
    ArrayList<Tag> sCategorias;
    TagView tagGroup;
    ArrayList<Categoria> categorias;
    Button botonSubirImagen;
    private static final int GALLERY_INTENT=1;
    private ImageView imageView;
    private ProgressDialog progressDialog;
    ArrayList imagenes;
    private static final int INTENT_EXTRA_IMAGES=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_atractivo_turistico);
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
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //startActivityForResult(intent,GALLERY_INTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Selecciona Fotos"), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
            progressDialog.setTitle("Subiendo Foto");
            progressDialog.setMessage("Subiendo Foto a Base de Datos!");
            progressDialog.setCancelable(false);
            progressDialog.show();
            //imagenes = data.getParcelableArrayListExtra("adsdsds");

            Uri uri=data.getData();
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
                        String downloadURL = downloadUri.toString();
                        Log.v("descargar",downloadURL);
                        Glide.with(AgregarAtractivoTuristico.this)
                                .load(downloadURL)
                                .fitCenter()
                                .centerCrop()
                                .into(imageView);
                        Toast.makeText(AgregarAtractivoTuristico.this,"La foto se subio exitosamente!",Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }

    public void agregarCategorias(){
        addCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewCategoria=(TextView) findViewById(R.id.editTextCategoria);
                if(!textViewCategoria.getText().toString().isEmpty())
                {
                    tituloCategoria.setVisibility(View.VISIBLE);
                    String sCategoria=textViewCategoria.getText().toString();
                    Categoria categoria=new Categoria(sCategoria);
                    categorias.add(categoria);
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
                    mMap.addMarker(new MarkerOptions().position(sydney).title(atractivoTuristico.getNombre()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    String nombre=textViewNombre.getText().toString();
                    String ciudad=textViewCiudad.getText().toString();
                    String comuna=textViewComuna.getText().toString();
                    String descripcion=textViewDescripcion.getText().toString();
                    Double latitud=marker.getPosition().latitude;
                    Double longitud=marker.getPosition().longitude;
                    AtractivoTuristico atractivoTuristico=new AtractivoTuristico(nombre,ciudad,comuna,descripcion,latitud,longitud);
                    mDatabase.child("atractivoTuristico").push().setValue(atractivoTuristico, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            String key = databaseReference.getKey();
                            Log.v("keyyy",key);
                            for (Categoria categoria: categorias){
                                mDatabase.child("categoria").child(key).push().setValue(categoria);
                            }


                        }
                    });




                }
            }
        });
    }

    public void cancelarAgregarAtractivoTuristico(){
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(AgregarAtractivoTuristico.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(AgregarAtractivoTuristico.this);
                }
                builder.setTitle("Delete entry")
                        .setMessage("Seguro Quieres Cancelar?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(AgregarAtractivoTuristico.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
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

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                mostrarAtractivoTuristico();
                mMap.addMarker(marker.position(latLng));
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
        mostrarAtractivoTuristico();
        agregarAtractivoTuristico();
        cancelarAgregarAtractivoTuristico();
        agregarCategorias();
        subirImagen();
    }

}
