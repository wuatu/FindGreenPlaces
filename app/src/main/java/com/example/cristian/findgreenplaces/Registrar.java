package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Clases.Utils.Referencias;
import Clases.Models.Usuario;

public class Registrar extends AppCompatActivity {
    Button botonRegistrar;
    Button botonCancelar;
    TextView textViewNombre;
    TextView textViewApellido;
    TextView textViewEmail;
    EditText textViewPassword;
    TextView getTextViewPassword2;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Registrar");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        botonCancelar=findViewById(R.id.cancelar);
        botonRegistrar=findViewById(R.id.aceptar);
        textViewNombre=findViewById(R.id.nombre);
        textViewApellido=findViewById(R.id.apellido);
        textViewEmail=findViewById(R.id.email);
        textViewPassword=findViewById(R.id.password);
        getTextViewPassword2=findViewById(R.id.password2);
        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference();
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(Registrar.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(Registrar.this);
                }
                builder.setTitle("Información")
                        .setMessage("Seguro Quieres Cancelar?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ejecutarLoginActivity();
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
    private void registrarUsuario() {
        View focusView = null;
        boolean cancel = false;
        final String nombre =textViewNombre.getText().toString();
        final String apellido = textViewApellido.getText().toString();
        final String email = textViewEmail.getText().toString();
        String password = textViewPassword.getText().toString();
        String password2 = getTextViewPassword2.getText().toString();
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            textViewPassword.setError(getString(R.string.error_invalid_password));
            focusView = textViewPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            textViewEmail.setError(getString(R.string.error_field_required));
            focusView = textViewEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            textViewEmail.setError(getString(R.string.error_invalid_email));
            focusView = textViewEmail;
            cancel = true;
        }
        if(TextUtils.isEmpty(nombre)){
            textViewNombre.setError("Ingrese nombre");
            focusView=textViewNombre;
            cancel=true;
        }
        if(TextUtils.isEmpty(apellido)){
            textViewApellido.setError("Ingrese apellido");
            focusView=textViewApellido;
            cancel=true;
        }
        if(TextUtils.isEmpty(password2)){
            getTextViewPassword2.setError("Ingrese password");
            focusView=getTextViewPassword2;
            cancel=true;
        }else if(!passwordIguales(password,password2)){
            getTextViewPassword2.setError("Contraseña no coincide");
            focusView=getTextViewPassword2;
            cancel=true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String userId=task.getResult().getUser().getUid();
                        Usuario usuario=new Usuario(userId,nombre,apellido,email,"","0","0","0",Referencias.PRINCIPIANTE);
                        mDatabase.child("usuario").child(userId).setValue(usuario);
                        Toast.makeText(Registrar.this,"Usuario creado con exito!",Toast.LENGTH_SHORT).show();
                        ejecutarLoginActivity();
                    }else{
                        Toast.makeText(Registrar.this,"Error al crear usuario!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public static boolean isNumeric(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }

    private void ejecutarLoginActivity() {
        Intent intent=new Intent(this,Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private boolean passwordIguales(String ps1,String ps2) {
        if(ps1.equals(ps2)){
            return true;
        }
        return false;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
