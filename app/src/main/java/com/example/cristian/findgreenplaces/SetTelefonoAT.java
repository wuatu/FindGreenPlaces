package com.example.cristian.findgreenplaces;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Color;

        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;
        import androidx.appcompat.widget.Toolbar;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.ArrayList;

        import Clases.Models.AtractivoTuristico;
        import Clases.Models.Contribucion;
        import Clases.Utils.IdUsuario;
        import Clases.Models.Imagen;
        import Clases.Utils.Referencias;
        import Clases.Models.Usuario;

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
                atractivoTuristico.setTelefono(editTextTelefono.getText().toString());
                Contribucion contribucion=new Contribucion("",atractivoTuristico.getId(),IdUsuario.getIdUsuario(),Referencias.TELEFONO,editTextTelefono.getText().toString(),Referencias.VISIBLE);

                setResult(RESULT_OK,
                        new Intent().putExtra("nombre", atractivoTuristico.getTelefono())
                                .putExtra("contribucion",contribucion));
                finish();
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
