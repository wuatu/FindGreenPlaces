package com.example.cristian.findgreenplaces;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Clases.Adapter.AdapterSliderVisualizacionDeFotosZoom;
import Clases.Models.AtractivoTuristico;
import Clases.Models.Contribucion;
import Clases.Utils.IdUsuario;
import Clases.Models.Imagen;
import Clases.Models.MeGustaImagen;
import Clases.Utils.Referencias;
import Clases.Utils.SubirPuntos;

public class VisualizacionDeImagen extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    AtractivoTuristico atractivoTuristico;

    TextView textViewNombre;
    TextView textViewFecha;
    TextView textViewNLike;
    TextView textViewVisualizaciones;
    ImageView imageViewLike;
    LinearLayout linearLayoutLike;
    ImageView imageViewReportar;
    ArrayList<Imagen> imagenes;
    ArrayList<MeGustaImagen> meGustaImagens;
    ArrayList<String> imageUrl;
    String imageLike[];
    String urlMayor;
    int mayor=0;
    private static final int IMAGENES=0;
    AdapterSliderVisualizacionDeFotosZoom adapterSliderVisualizacionDeFotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adapter_slider_visualizacion_de_fotos);
        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        //textView.setText("Foto de Perfil");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        database= FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        int position=(getIntent().getIntExtra("position",0));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));
        textViewVisualizaciones=findViewById(R.id.textViewVisualizacion);
        meGustaImagens=((ArrayList<MeGustaImagen>)getIntent().getSerializableExtra("meGustaImagens"));

        atractivoTuristico=((AtractivoTuristico)getIntent().getSerializableExtra("atractivoTuristico"));

        for (Imagen imagen:imagenes){
            int actual=Integer.parseInt(imagen.getContadorLike());
            if (actual>=mayor){
                mayor=actual;
                urlMayor=imagen.getUrl();
            }

        }


        Log.v("posicion",String.valueOf(position));

        //ver las imagenes que tienen like
        //imageUrl =new String[imagenes.size()];
        imageUrl =new ArrayList<>();
        imageLike =new String[imagenes.size()];
        for(int i=0;i<imagenes.size();i++){
            for(MeGustaImagen meGustaImagen: meGustaImagens){
                imageLike[i]="false";
                if(imagenes.get(i).getId().equalsIgnoreCase(meGustaImagen.getId())){
                    imageLike[i]="true";
                    break;
                }
            }
            imageUrl.add(imagenes.get(i).getUrl());
        }

        imageViewReportar=findViewById(R.id.imageViewReportar);
        textViewNombre=findViewById(R.id.textViewNombre);
        textViewFecha=findViewById(R.id.textViewFecha);
        textViewNLike=findViewById(R.id.textViewNLike);

        imageViewLike=findViewById(R.id.imageViewLike);
        linearLayoutLike=findViewById(R.id.linearLayoutLike);
        ViewPager viewPager=findViewById(R.id.view_pager);

        adapterSliderVisualizacionDeFotos=new AdapterSliderVisualizacionDeFotosZoom(this,imageUrl);
        viewPager.setAdapter(adapterSliderVisualizacionDeFotos);
        pintaImagenes(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                pintaImagenes(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(position);
    }


    public void contadorDeVisualizaciones(final int position){
        //contador de visualizaciones
        DatabaseReference databaseReference=mDatabase.child(Referencias.IMAGENES)
                .child(imagenes.get(position)
                .getIdAtractivo())
                .child(imagenes.get(position).getId());
        String key=databaseReference.getKey();
        Imagen imagen=new Imagen(key,
                imagenes.get(position).getUrl(),
                imagenes.get(position).getFecha(),
                imagenes.get(position).getIdAtractivo(),
                imagenes.get(position).getIdUsuario(),
                imagenes.get(position).getNombreUsuario(),
                imagenes.get(position).getContadorLike(),
                imagenes.get(position).getContadorVisualizaciones(),
                imagenes.get(position).getContadorReportes(),
                imagenes.get(position).getVisible());
        int contadorVisualizaciones=Integer.valueOf(imagen.getContadorVisualizaciones())+1;
        imagen.setContadorVisualizaciones(String.valueOf(contadorVisualizaciones));
        //comento la linea 152 y funciona perfe ahi se cae
        databaseReference.setValue(imagen);
        textViewVisualizaciones.setText(String.valueOf(contadorVisualizaciones));
    }

    public void pintaImagenes(final int position){
        contadorDeVisualizaciones(position);
        //textViewVisualizaciones.setText(imagenes.get(position).getContadorVisualizaciones());
        textViewNombre.setText(imagenes.get(position).getNombreUsuario() );
        textViewFecha.setText(imagenes.get(position).getFecha());
        textViewNLike.setText(imagenes.get(position).getContadorLike());
        if(imageLike[position]=="true"){
            imageViewLike.setImageResource(R.drawable.likeon);
            imageViewLike.setTag(R.drawable.likeon);
        } else{
            imageViewLike.setImageResource(R.drawable.likeoff);
            imageViewLike.setTag(R.drawable.likeoff);
        }

        linearLayoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(imageViewLike.getTag().toString())==R.drawable.likeoff){
                    //Consulta para añadir un megusta a la imagen
                    DatabaseReference databaseReference=mDatabase.child(Referencias.FOTOMEGUSTA).child(IdUsuario.getIdUsuario()).child(imagenes.get(position).getIdAtractivo()).child(imagenes.get(position).getId());
                    String key=databaseReference.getKey();
                    MeGustaImagen meGustaImagen=new MeGustaImagen(key,IdUsuario.getIdUsuario(),imagenes.get(position).getIdAtractivo(),imagenes.get(position).getId(),Referencias.MEGUSTA);
                    databaseReference.setValue(meGustaImagen);
                    imageViewLike.setImageResource(R.drawable.likeon);
                    imageViewLike.setTag(R.drawable.likeon);
                    imageLike[position]="true";
                    int contadorLike=Integer.valueOf(imagenes.get(position).getContadorLike())+1;

                    //cambia la foto principal por cantidad de likes
                    if(contadorLike>=mayor){
                        if(atractivoTuristico!=null) {
                            atractivoTuristico.setUrlFoto(urlMayor);
                            mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                        }
                    }
                    textViewNLike.setText(String.valueOf(contadorLike));
                    imagenes.get(position).setContadorLike(String.valueOf(contadorLike));
                    setResult(RESULT_OK, new Intent().putExtra("imagenes", imagenes));
                    SubirPuntos.aumentaPuntosOtrosUsuarios(VisualizacionDeImagen.this,imagenes.get(position).getIdUsuario(),1);
                }
                else{
                    mDatabase.child(Referencias.FOTOMEGUSTA).child(IdUsuario.getIdUsuario()).child(imagenes.get(position).getIdAtractivo()).child(imagenes.get(position).getId()).removeValue();
                    imageViewLike.setImageResource(R.drawable.likeoff);
                    imageViewLike.setTag(R.drawable.likeoff);
                    imageLike[position]="false";
                    int contadorLike=Integer.valueOf(imagenes.get(position).getContadorLike());
                    if(contadorLike>0){
                        contadorLike=contadorLike-1;
                        textViewNLike.setText(String.valueOf(contadorLike));
                        imagenes.get(position).setContadorLike(String.valueOf(contadorLike));
                        setResult(RESULT_OK, new Intent().putExtra("imagenes", imagenes));
                    }
                    SubirPuntos.disminuyePuntosOtrosUsuarios(VisualizacionDeImagen.this,imagenes.get(position).getIdUsuario(),1);
                }
            }
        });
        imageViewReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(atractivoTuristico!=null) {
                    Intent intent = new Intent(VisualizacionDeImagen.this, DialogoReportarFoto.class);
                    intent.putExtra("atractivoTuristico", atractivoTuristico);
                    intent.putExtra("imagen", imagenes.get(position));
                    intent.putExtra("position",position);
                    startActivityForResult(intent,IMAGENES);
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_OK,
                new Intent().putExtra("imagenes", imagenes).
                        putExtra("meGustaImagens",meGustaImagens));
        finish();
        onBackPressed();
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
