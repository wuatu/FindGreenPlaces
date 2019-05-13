package com.example.cristian.findgreenplaces;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.graphics.Color;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;

        import Clases.AtractivoTuristico;
        import Clases.Contribucion;
        import Clases.IdUsuario;
        import Clases.Imagen;
        import Clases.Referencias;
        import Clases.Usuario;

public class SetTelefonoAT extends AppCompatActivity {
    Button buttonAceptar;
    Button buttonCancelar;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    AtractivoTuristico atractivoTuristico;
    ArrayList<Imagen> imagenes;
    EditText editTextTelefono;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_telefono_at);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Editar Telefono");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        buttonAceptar=findViewById(R.id.buttonAceptar);
        buttonCancelar=findViewById(R.id.buttonCancelar);
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));
        editTextTelefono=findViewById(R.id.editTextTelefono);

        if(!atractivoTuristico.getTelefono().equals("")){
            editTextTelefono.setText(atractivoTuristico.getTelefono());
        }
        aceptar();
        cancelar();
    }

    public void aceptar(){
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextTelefono.getText().equals("")){
                    atractivoTuristico.setTelefono(editTextTelefono.getText().toString());
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                }
                new AlertDialog.Builder(SetTelefonoAT.this)
                        .setTitle("Información")
                        .setMessage("Seguro Quieres Enviar Estos Datos?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                /*Intent intent = new Intent(SetHorarioDeAtencion.this, VisualizarAtractivoTuristico.class);
                                intent.putExtra("item_photo", item_photo);
                                intent.putExtra("atractivoTuristico", atractivoTuristico);
                                startActivity(intent);*/
                                Toast.makeText(SetTelefonoAT.this,"Datos enviados correctamente!",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
    }

    public void cancelar(){
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new AlertDialog.Builder(SetTelefonoAT.this)
                       .setTitle("Información")
                        .setMessage("Seguro que quieres cancelar?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                atractivoTuristico.setDescripcion(editTextTelefono.getText().toString());

                                mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                                //añade contribucion (a tabla "contribucionPorAt") por atractivo turistio
                                DatabaseReference databaseReference=mDatabase.child(Referencias.CONTRIBUCIONESPORAT).
                                        child(atractivoTuristico.getId()).
                                        child(IdUsuario.getIdUsuario()).push();
                                String key=databaseReference.getKey();
                                Contribucion contribucion=new Contribucion(key,atractivoTuristico.getId(),IdUsuario.getIdUsuario(),Referencias.TELEFONO,editTextTelefono.getText().toString());
                                databaseReference.setValue(contribucion);

                                //añade contribucion (a tabla "contribucionPorUsuario") por usuario
                                mDatabase.child(Referencias.CONTRIBUCIONESPORUSUARIO).
                                        child(IdUsuario.getIdUsuario()).
                                        child(atractivoTuristico.getId()).
                                        child(key).
                                        setValue(contribucion);

                                //subir puntos
                                final DatabaseReference databaseReference1=mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario());
                                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        usuario=dataSnapshot.getValue(Usuario.class);
                                        int puntos=Integer.valueOf(usuario.getPuntos())+2;
                                        if(puntos>=100){
                                            int nivel= Integer.valueOf(usuario.getNivel());
                                            if(nivel==1){
                                                usuario.setNivel("2");
                                            }
                                            if(nivel==2){
                                                usuario.setNivel("3");
                                            }
                                        }else{
                                            usuario.setPuntos(String.valueOf(puntos));

                                        }
                                        int contribuciones=Integer.valueOf(usuario.getContribuciones())+1;
                                        usuario.setContribuciones(String.valueOf(contribuciones));
                                        databaseReference1.setValue(usuario);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(SetTelefonoAT.this,"Datos enviados correctamente",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
