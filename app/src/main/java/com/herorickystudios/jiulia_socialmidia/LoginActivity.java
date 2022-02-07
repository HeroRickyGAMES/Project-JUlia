package com.herorickystudios.jiulia_socialmidia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//Programado por HeroRicky Games

public class LoginActivity extends AppCompatActivity {

    EditText lemail, lsenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lemail = findViewById(R.id.lgEmail);
        lsenha = findViewById(R.id.lgSenha);

    }

    public void autenticarusuario(View view) {

        String email = lemail.getText().toString();
        String senha = lsenha.getText().toString();

        if (email.isEmpty() || senha.isEmpty()) {

            Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_LONG);
            snackbar.show();

        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                abrirhome();
                            }
                        }, 3000);
                    } else {
                        String erro;
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            erro = "Erro ao logar! Verifique seu email ou sua senha se estão corretos! Ou verifique sua conexão com a internet!";
                            Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }
            });
        }
    }

    public void abrirhome(){
        Intent intent = new Intent(this, Home_Show.class);
        startActivity(intent);
    }
}