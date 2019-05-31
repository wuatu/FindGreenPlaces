package com.example.cristian.findgreenplaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class OlvidoContrasena extends AppCompatActivity {
    Button buttonCancelar;
    Button buttonAceptar;
    View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido_contrasena);

        Toolbar toolbar=findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView)toolbar.findViewById(R.id.textViewToolbar);
        textView.setText("Olvidó Contraseña");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final TextView textViewEmail=findViewById(R.id.email);
        buttonAceptar=findViewById(R.id.aceptar);
        buttonCancelar=findViewById(R.id.cancelar);

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(OlvidoContrasena.this)
                        .setTitle("Información")
                        .setMessage("Seguro Quieres Cancelar?")
                        //.setIcon(R.drawable.aporte)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(OlvidoContrasena.this, MenuPrincipal.class);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancel=false;
                if(textViewEmail.getText().toString().equals("")){
                    textViewEmail.setError(("Ingrese correo"));
                }
                else if (!isEmailValid(textViewEmail.getText().toString())) {
                    textViewEmail.setError(getString(R.string.error_invalid_email));
                    focusView = textViewEmail;
                    cancel = true;
                }
                else if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                }
                else{
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = textViewEmail.getText().toString();

                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(OlvidoContrasena.this, "Email enviado", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                }
            }
        });



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
