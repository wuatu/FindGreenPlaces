package com.example.cristian.findgreenplaces;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Clases.Models.AtractivoTuristico;
import Clases.Utils.IdUsuario;
import Clases.Models.Imagen;
import Clases.Models.MeGustaImagen;
import Clases.Utils.Referencias;
import Clases.Models.SpacePhoto;
import Clases.Utils.SubirPuntos;
import Clases.Models.Usuario;

/**
 * Created by Chike on 2/12/2017.
 */

public class SpacePhotoActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    FirebaseDatabase database;

    public static final String EXTRA_SPACE_PHOTO = "SpacePhotoActivity.SPACE_PHOTO";

    private ImageView mImageView;
    AtractivoTuristico atractivoTuristico;
    Imagen imagen;

    TextView textViewNombre;
    TextView textViewFecha;
    TextView textViewNLike;
    TextView textViewVisualizaciones;
    ImageView imageViewLike;
    LinearLayout linearLayoutLike;
    ImageView imageViewReportar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        //textView.setText("Foto de Perfil");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_focused));

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();

        imageViewReportar=findViewById(R.id.imageViewReportar);
        textViewNombre=findViewById(R.id.textViewNombre);
        textViewFecha=findViewById(R.id.textViewFecha);
        textViewNLike=findViewById(R.id.textViewNLike);
        textViewVisualizaciones=findViewById(R.id.textViewVisualizacion);
        imageViewLike=findViewById(R.id.imageViewLike);
        linearLayoutLike=findViewById(R.id.linearLayoutLike);

        mImageView = (ImageView) findViewById(R.id.image);
        SpacePhoto spacePhoto = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);

        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagen=((Imagen)getIntent().getSerializableExtra("imagen"));


        mDatabase.child(Referencias.USUARIO).child(imagen.getIdUsuario()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario =dataSnapshot.getValue(Usuario.class);
                textViewNombre.setText(usuario.getNombre()+" "+usuario.getApellido());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        textViewFecha.setText(imagen.getFecha());
        textViewNLike.setText(imagen.getContadorLike());
        textViewVisualizaciones.setText(imagen.getContadorVisualizaciones());
        imageViewLike.setTag(R.drawable.likeoff);

        int contadorVisualizaciones=Integer.valueOf(imagen.getContadorVisualizaciones())+1;
        textViewVisualizaciones.setText(String.valueOf(contadorVisualizaciones));
        imagen.setContadorVisualizaciones(String.valueOf(contadorVisualizaciones));
        mDatabase.child(Referencias.IMAGENES).
                child(imagen.getIdAtractivo()).
                child(imagen.getId()).setValue(imagen);

        //Consulta para saber si la imagen tiene un like del usuario
            mDatabase.child(Referencias.FOTOMEGUSTA).child(IdUsuario.getIdUsuario()).child(imagen.getIdAtractivo()).child(imagen.getId()).child(Referencias.MEGUSTA).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String meGusta=dataSnapshot.getValue(String.class);
                    if(meGusta!=null){
                        if(meGusta.equals(Referencias.MEGUSTA)){
                            imageViewLike.setImageResource(R.drawable.likeon);
                            imageViewLike.setTag(R.drawable.likeon);
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Glide.with(this)
        .load(spacePhoto.getUrl())
        .asBitmap()
        .error(R.drawable.cargando)
        .listener(new RequestListener<String, Bitmap>() {

            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                onPalette(Palette.from(resource).generate());
                mImageView.setImageBitmap(resource);

                return false;
            }

            public void onPalette(Palette palette) {
                if (null != palette) {
                    ViewGroup parent = (ViewGroup) mImageView.getParent().getParent();
                    //parent.setBackgroundColor(palette.getDarkVibrantColor(Color.BLACK));
                }
            }
        })
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .into(mImageView);
        linearLayoutLike.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(Integer.valueOf(imageViewLike.getTag().toString())==R.drawable.likeoff){
            //Consulta para añadir un megusta a la imagen
            DatabaseReference databaseReference=mDatabase.child(Referencias.FOTOMEGUSTA).child(IdUsuario.getIdUsuario()).child(imagen.getIdAtractivo()).child(imagen.getId());
            String key=databaseReference.getKey();
            MeGustaImagen meGustaImagen=new MeGustaImagen(key,IdUsuario.getIdUsuario(),imagen.getIdAtractivo(),imagen.getId(),Referencias.MEGUSTA);
            databaseReference.setValue(meGustaImagen);
            imageViewLike.setImageResource(R.drawable.likeon);
            imageViewLike.setTag(R.drawable.likeon);
            int contadorLike=Integer.valueOf(imagen.getContadorLike())+1;
            textViewNLike.setText(String.valueOf(contadorLike));
            imagen.setContadorLike(String.valueOf(contadorLike));
            mDatabase.child(Referencias.IMAGENES).child(imagen.getIdAtractivo()).child(imagen.getId()).setValue(imagen);
            SubirPuntos.aumentaPuntosOtrosUsuarios(SpacePhotoActivity.this,imagen.getIdUsuario(),1);
        }
        else{
            mDatabase.child(Referencias.FOTOMEGUSTA).child(IdUsuario.getIdUsuario()).child(imagen.getIdAtractivo()).child(imagen.getId()).removeValue();
            imageViewLike.setImageResource(R.drawable.likeoff);
            imageViewLike.setTag(R.drawable.likeoff);
            int contadorLike=Integer.valueOf(imagen.getContadorLike());
            if(contadorLike>0){
                contadorLike=contadorLike-1;
                textViewNLike.setText(String.valueOf(contadorLike));
                imagen.setContadorLike(String.valueOf(contadorLike));
                mDatabase.child(Referencias.IMAGENES).child(imagen.getIdAtractivo()).child(imagen.getId()).setValue(imagen);
            }
            SubirPuntos.disminuyePuntosOtrosUsuarios(SpacePhotoActivity.this,imagen.getIdUsuario(),1);
        }
            }
        });
        imageViewReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SpacePhotoActivity.this,DialogoReportarFoto.class);
                intent.putExtra("atractivoTuristico",atractivoTuristico);
                intent.putExtra("imagen",imagen);
                startActivity(intent);
            }
        });
    }

   /* private SimpleTarget target = new SimpleTarget<Bitmap>() {

        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

           onPalette(Palette.from(bitmap).generate());
           mImageView.setImageBitmap(bitmap);
        }

        public void onPalette(Palette palette) {
            if (null != palette) {
                ViewGroup parent = (ViewGroup) mImageView.getParent().getParent();
                parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
            }
        }
    };*/
   @Override
   public boolean onSupportNavigateUp() {
       onBackPressed();
       return false;
   }
}
