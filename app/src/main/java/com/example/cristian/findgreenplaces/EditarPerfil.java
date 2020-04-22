package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Clases.IdUsuario;
import Clases.Referencias;
import Clases.Usuario;

public class EditarPerfil extends AppCompatActivity {
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    Button botonRegistrar;
    Button botonCancelar;
    TextView textViewNombre;
    TextView textViewApellido;
    TextView textViewEmail;
    TextView textViewDia;
    TextView textViewMes;
    TextView textViewAño;
    EditText textViewPassword;
    EditText textViewPasswordActual;
    TextView getTextViewPassword2;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    ImageView imageViewFotoPerfil;
    Usuario usuario;
    private static String PREFS_KEY = "mispreferencias";
    private static String SESIONINICIADA = "estado.sesion";
    private static String IDUSUARIO = "mispreferencias2";
    private static String NOMBRE = "nombre";
    private static String APELLIDO = "apellido";
    private static String URL = "url";
    private static String CORREO = "correo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Editar Perfil");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    botonCancelar=findViewById(R.id.cancelar);
    botonRegistrar=findViewById(R.id.aceptar);
    textViewNombre=findViewById(R.id.nombre);
    textViewApellido=findViewById(R.id.apellido);
    textViewEmail=findViewById(R.id.email);
    textViewDia=findViewById(R.id.dia);
    textViewMes=findViewById(R.id.mes);
    textViewAño=findViewById(R.id.año);
    textViewPasswordActual=findViewById(R.id.passwordActual);

    textViewNombre.setText(IdUsuario.getNombreUsuario());
    textViewApellido.setText(IdUsuario.getApellidoUsuario());
    textViewEmail.setText(IdUsuario.getCorreo());

    database=FirebaseDatabase.getInstance();
    mDatabase=database.getReference();

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user=firebaseAuth.getCurrentUser();
            }
        };

        imageViewFotoPerfil=findViewById(R.id.imageViewFotoPerfil1);
        if(IdUsuario.getUrl().equals("")) {
            Glide.with(EditarPerfil.this)
                    .load(R.drawable.com_facebook_profile_picture_blank_square)
                    .fitCenter()
                    .transform(new PerfilUsuario.CircleTransform(EditarPerfil.this))
                    .into(imageViewFotoPerfil);
        }else{
            Glide.with(EditarPerfil.this)
                    .load(IdUsuario.getUrl())
                    .transform(new PerfilUsuario.CircleTransform(EditarPerfil.this))
                    .fitCenter()
                    .into(imageViewFotoPerfil);
            Log.v("aburri",IdUsuario.getUrl());

        }

        imageViewFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPerfil.this, FotoPerfil.class);
                startActivity(intent);
            }
        });
    mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario()).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            usuario=dataSnapshot.getValue(Usuario.class);
            textViewDia.setText(String.valueOf(usuario.getDia()));
            textViewMes.setText(String.valueOf(usuario.getMes()));
            textViewAño.setText(String.valueOf(usuario.getAño()));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


    textViewPassword=findViewById(R.id.password);
    getTextViewPassword2=findViewById(R.id.password2);

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registrarUsuario();
        }
    });
        botonCancelar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.
            Builder(EditarPerfil.this).setTitle("Información")
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
        final String dia = textViewDia.getText().toString();
        final String mes = textViewMes.getText().toString();
        final String año = textViewAño.getText().toString();
        final String password = textViewPassword.getText().toString();
        String password2 = getTextViewPassword2.getText().toString();
        final boolean[] isPassChanged = {false};
        final boolean[] isPasscorrect = {false};
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
        if(TextUtils.isEmpty(dia)){
            textViewDia.setError("Ingrese dia");
            focusView=textViewDia;
            cancel=true;
        }else if(!isNumeric(dia)){
            textViewDia.setError("Ingrese valores numericos");
            focusView=textViewDia;
            cancel=true;
        }
        if(TextUtils.isEmpty(mes) ){
            textViewMes.setError("Ingrese mes");
            focusView=textViewMes;
            cancel=true;
        }else if(!isNumeric(mes)){
            textViewMes.setError("Ingrese valores numericos");
            focusView=textViewMes;
            cancel=true;
        }
        if(TextUtils.isEmpty(año)){
            textViewAño.setError("Ingrese año");
            focusView=textViewAño;
            cancel=true;
        }else if(!isNumeric(año)){
            textViewAño.setError("Ingrese valores numericos");
            cancel=true;

        }
        if(!TextUtils.isEmpty(textViewPasswordActual.getText())){
            String password3=textViewPasswordActual.getText().toString();
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password3).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            isPasscorrect[0]=true;
                        }
                        //textViewPasswordActual.setError("Password no coincide");


                    }
                });
            if(!isPasscorrect[0]) {
                if (TextUtils.isEmpty(password)) {
                    textViewPassword.setError("Ingrese Nueva Contraseña");
                    focusView = textViewPassword;
                    cancel = true;
                }
                else if (TextUtils.isEmpty(password2)) {
                    getTextViewPassword2.setError("Ingrese Nueva Contraseña");
                    focusView = getTextViewPassword2;
                    cancel = true;
                } else if (!passwordIguales(password, password2)) {
                    getTextViewPassword2.setError("Contraseña no coincide");
                    focusView = getTextViewPassword2;
                    cancel = true;
                    isPassChanged[0] =true;
                }
                isPassChanged[0] =true;
            }
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            new AlertDialog.
                    Builder(EditarPerfil.this).setTitle("Información")
                    .setMessage("Seguro Quieres Enviar estos Datos?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(isPassChanged[0]){
                                mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario()).setValue(usuario);
                                user.updatePassword(password)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    usuario.setNombre(nombre);
                                                    usuario.setApellido(apellido);
                                                    usuario.setEmail(email);
                                                    usuario.setDia(Integer.valueOf(dia));
                                                    usuario.setMes(Integer.valueOf(mes));
                                                    usuario.setAño(Integer.valueOf(año));
                                                    user.updateEmail(email)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                    }
                                                                }
                                                            });

                                                    Toast.makeText(EditarPerfil.this,"Contraseña Actualizada.",Toast.LENGTH_SHORT).show();
                                                    logout();
                                                }
                                                else{
                                                    Toast.makeText(EditarPerfil.this,"Error Contraseña Actual Incorrecta",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else{
                                usuario.setNombre(nombre);
                                usuario.setApellido(apellido);
                                usuario.setEmail(email);
                                usuario.setDia(Integer.valueOf(dia));
                                usuario.setMes(Integer.valueOf(mes));
                                usuario.setAño(Integer.valueOf(año));
                                user.updateEmail(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                }
                                            }
                                        });

                                mDatabase.child(Referencias.USUARIO).child(IdUsuario.getIdUsuario()).setValue(usuario);
                                guardarValorString(EditarPerfil.this,NOMBRE,usuario.getNombre());
                                guardarValorString(EditarPerfil.this,APELLIDO,usuario.getApellido());
                                guardarValorString(EditarPerfil.this,CORREO,usuario.getEmail());
                                guardarValorString(EditarPerfil.this,URL,IdUsuario.getUrl());
                                Toast.makeText(EditarPerfil.this,"Datos Actualizados Exitosamente.",Toast.LENGTH_SHORT).show();
                                ejecutarLoginActivity();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();

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

    public static void guardarValorString(Context context, String keyPref, String valor) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(keyPref, valor);
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
    }

    private void logout() {
        guardarValorBoolean(EditarPerfil.this, SESIONINICIADA, false);
        guardarValorString(EditarPerfil.this, IDUSUARIO, "");
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
}


