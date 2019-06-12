package com.example.cristian.findgreenplaces;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
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

public class SetRedesSocialesAT extends AppCompatActivity {
    Button buttonAceptar;
    Button buttonCancelar;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    AtractivoTuristico atractivoTuristico;
    ArrayList<Imagen> imagenes;
    EditText editTextRedes;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_redes_sociales_at);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Editar Redes Sociales");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        buttonAceptar=findViewById(R.id.buttonAceptar);
        buttonCancelar=findViewById(R.id.buttonCancelar);
        atractivoTuristico= ((AtractivoTuristico) getIntent().getSerializableExtra("atractivoTuristico"));
        imagenes=((ArrayList<Imagen>)getIntent().getSerializableExtra("imagenes"));
        editTextRedes=findViewById(R.id.editTextRedes);
        if(!atractivoTuristico.getTelefono().equals("")){
            editTextRedes.setText(atractivoTuristico.getRedesSociales());
        }
        aceptar();
        cancelar();
    }

    public void aceptar(){
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextRedes.getText().equals("")){
                    atractivoTuristico.setRedesSociales(editTextRedes.getText().toString());
                    mDatabase.child(Referencias.ATRACTIVOTURISTICO).child(atractivoTuristico.getId()).setValue(atractivoTuristico);
                }
                atractivoTuristico.setRedesSociales(editTextRedes.getText().toString());
                Contribucion contribucion=new Contribucion("",atractivoTuristico.getId(),IdUsuario.getIdUsuario(),Referencias.REDESSOCIALES,editTextRedes.getText().toString());

                setResult(RESULT_OK,
                        new Intent().putExtra("nombre", atractivoTuristico.getRedesSociales())
                                .putExtra("contribucion",contribucion));
                finish();
            }
        });
    }

    public void cancelar(){
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SetRedesSocialesAT.this)
                        .setTitle("Información")
                        .setMessage("Seguro que quieres cancelar?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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

