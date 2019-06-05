package com.example.cristian.findgreenplaces;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import java.util.ArrayList;
import java.util.List;

import Clases.IdUsuario;
import Clases.Referencias;
import Clases.UserLoginTask;
import Clases.Usuario;


public class Login extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    private UserLoginTask mAuthTask = null;

    public UserLoginTask getmAuthTask() {
        return mAuthTask;
    }

    public void setmAuthTask(UserLoginTask mAuthTask) {
        this.mAuthTask = mAuthTask;
    }

    public AutoCompleteTextView getmEmailView() {
        return mEmailView;
    }

    public void setmEmailView(AutoCompleteTextView mEmailView) {
        this.mEmailView = mEmailView;
    }

    public EditText getmPasswordView() {
        return mPasswordView;
    }

    public void setmPasswordView(EditText mPasswordView) {
        this.mPasswordView = mPasswordView;
    }

    public View getmProgressView() {
        return mProgressView;
    }

    public void setmProgressView(View mProgressView) {
        this.mProgressView = mProgressView;
    }

    public View getmLoginFormView() {
        return mLoginFormView;
    }

    public void setmLoginFormView(View mLoginFormView) {
        this.mLoginFormView = mLoginFormView;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void setCallbackManager(CallbackManager callbackManager) {
        this.callbackManager = callbackManager;
    }

    public LoginButton getLoginFacebookButton() {
        return loginFacebookButton;
    }

    public void setLoginFacebookButton(LoginButton loginFacebookButton) {
        this.loginFacebookButton = loginFacebookButton;
    }

    public Button getBotonInvitado() {
        return botonInvitado;
    }

    public void setBotonInvitado(Button botonInvitado) {
        this.botonInvitado = botonInvitado;
    }

    public FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    public void setmAuthListener(FirebaseAuth.AuthStateListener mAuthListener) {
        this.mAuthListener = mAuthListener;
    }

    public TextView getRegistrar() {
        return registrar;
    }

    public void setRegistrar(TextView registrar) {
        this.registrar = registrar;
    }

    public static String getPrefsKey() {
        return PREFS_KEY;
    }

    public static void setPrefsKey(String prefsKey) {
        PREFS_KEY = prefsKey;
    }

    public static String getSESIONINICIADA() {
        return SESIONINICIADA;
    }

    public static void setSESIONINICIADA(String SESIONINICIADA) {
        Login.SESIONINICIADA = SESIONINICIADA;
    }

    public static String getIDUSUARIO() {
        return IDUSUARIO;
    }

    public static void setIDUSUARIO(String IDUSUARIO) {
        Login.IDUSUARIO = IDUSUARIO;
    }

    public static String getNOMBRE() {
        return NOMBRE;
    }

    public static void setNOMBRE(String NOMBRE) {
        Login.NOMBRE = NOMBRE;
    }

    public static String getAPELLIDO() {
        return APELLIDO;
    }

    public static void setAPELLIDO(String APELLIDO) {
        Login.APELLIDO = APELLIDO;
    }

    public static String getURL() {
        return URL;
    }

    public static void setURL(String URL) {
        Login.URL = URL;
    }

    public static String getCORREO() {
        return CORREO;
    }

    public static void setCORREO(String CORREO) {
        Login.CORREO = CORREO;
    }

    public TextView getTextViewOlvidoContrasña() {
        return textViewOlvidoContrasña;
    }

    public void setTextViewOlvidoContrasña(TextView textViewOlvidoContrasña) {
        this.textViewOlvidoContrasña = textViewOlvidoContrasña;
    }

    public boolean isSesionIniciada() {
        return sesionIniciada;
    }

    public void setSesionIniciada(boolean sesionIniciada) {
        this.sesionIniciada = sesionIniciada;
    }

    // UI references.
    public AutoCompleteTextView mEmailView;
    public EditText mPasswordView;
    public View mProgressView;
    public View mLoginFormView;
    public CallbackManager callbackManager;
    public LoginButton loginFacebookButton;
    public Button botonInvitado;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public TextView registrar;
    public static String PREFS_KEY = "mispreferencias";
    public static String SESIONINICIADA = "estado.sesion";
    public static String IDUSUARIO = "mispreferencias2";
    public static String NOMBRE = "nombre";
    public static String APELLIDO = "apellido";
    public static String URL = "url";
    public static String CORREO = "correo";
    public TextView textViewOlvidoContrasña;
    public boolean sesionIniciada=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        if(leerValorBoolean(Login.this,SESIONINICIADA)){
            String key=leerValorString(Login.this,IDUSUARIO);
            String nombre=leerValorString(Login.this,NOMBRE);
            String apellido=leerValorString(Login.this,APELLIDO);
            String correo=leerValorString(Login.this,CORREO);
            String url=leerValorString(Login.this,URL);
            IdUsuario idUsuario=new IdUsuario(key,nombre,apellido,correo,url);
            finish();
            ejecutarMainActivity();
        }

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        botonInvitado=findViewById(R.id.boton_invitado);
        textViewOlvidoContrasña=findViewById(R.id.textViewOlvideContraseña);
        textViewOlvidoContrasña.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,OlvidoContrasena.class);
                startActivity(intent);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        //loginFacebookButton = (LoginButton) findViewById(R.id.login_button);
        ///loginFacebookButton.setReadPermissions("email");
        registrar=findViewById(R.id.textViewRegistro);
        registrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Registrar.class);
                startActivity(intent);

            }
        });

        // Callback registration
        /*loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ejecutarMainActivity();
            }

            @Override
            public void onCancel() {
                Toast.makeText(Login.this,"Se ha cancelado ingreso!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login.this,"Error al iniciar sesion!",Toast.LENGTH_SHORT).show();
            }


        });*/

        //loginFacebookButton.setReadPermissions("email");


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    iniciarSesionUsuario();
                    return true;
                }
                return false;
            }
        });
        botonInvitado.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesionInvitado();
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesionUsuario();
            }
        });




        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
            }
        };

    }

    private void iniciarSesionInvitado() {
        String keyUsuario="invitado";
        IdUsuario idUsuario=new IdUsuario(keyUsuario);
        sesionIniciada=true;
        //guardarValorBoolean(Login.this,SESIONINICIADA,sesionIniciada);
        //guardarValorString(Login.this,IDUSUARIO,keyUsuario);
        ejecutarMainActivity();
    }

    private void ejecutarMainActivity() {
        Intent intent=new Intent(Login.this,MenuPrincipal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        startActivity(intent);
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void iniciarSesionUsuario() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Login.this,"Sesion iniciada con exito!",Toast.LENGTH_SHORT).show();
                        String keyUsuario=task.getResult().getUser().getUid();
                        FirebaseDatabase database;
                        DatabaseReference mDatabase;
                        database=FirebaseDatabase.getInstance();
                        mDatabase=database.getReference();
                        mDatabase.child(Referencias.USUARIO).child(keyUsuario).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                                IdUsuario idUsuario=new IdUsuario(usuario.getId(),usuario.getNombre(),usuario.getApellido(),usuario.getEmail(),usuario.getUrlFotoPerfil());
                                sesionIniciada=true;
                                guardarValorBoolean(Login.this,SESIONINICIADA,sesionIniciada);
                                guardarValorString(Login.this,IDUSUARIO,usuario.getId());
                                guardarValorString(Login.this,NOMBRE,usuario.getNombre());
                                guardarValorString(Login.this,APELLIDO,usuario.getApellido());
                                guardarValorString(Login.this,CORREO,usuario.getEmail());
                                guardarValorString(Login.this,URL,IdUsuario.getUrl());
                                ejecutarMainActivity();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }else{
                        showProgress(false);
                        Toast.makeText(Login.this,"Error, usuario o contraseña incorrecta!",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            //mLoginFormView=findViewById(R.id.progress_bar);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(Login.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }



    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


}

