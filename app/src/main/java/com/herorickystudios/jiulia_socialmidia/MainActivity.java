package com.herorickystudios.jiulia_socialmidia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Programado por HeroRicky Games

public class MainActivity extends AppCompatActivity {

    EditText nome, email, phone, pass;
    public DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("usuario");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        phone = findViewById(R.id.editPhone);
        pass = findViewById(R.id.editSenha);
    }

    public void cadastrarUsuario(View v) {
        String emails = email.getText().toString();
        String senha = pass.getText().toString();
        String telefone = phone.getText().toString();
        String name = nome.getText().toString();

        if (emails.isEmpty() || senha.isEmpty() || telefone.isEmpty() || name.isEmpty()) {
            Snackbar snackbar = Snackbar.make(v, "Preencha todos os campos!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emails, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

                        String getUID = usuarioLogado.getUid();

                        //Documentos
                        referencia.child(getUID).child("nome").setValue(name);
                        referencia.child(getUID).child("telefone").setValue(telefone);

                        Snackbar snackbar = Snackbar.make(v, "Cadastro Realizado com sucesso!", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    } else {
                        String erro;
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            erro = "Digite uma senha com no minimo 6 caracteres!";
                        } catch (FirebaseAuthUserCollisionException e) {
                            erro = "Essa conta já existe!";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erro = "Verifique se seu email está digitado corretamente!";

                        } catch (Exception e) {
                            erro = "Erro ao cadastrar usuário!";
                        }

                        //Snackbar com erros
                        Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuariologado = FirebaseAuth.getInstance().getCurrentUser();

        if(usuariologado != null){
            abrirhome();
        }
    }

    public void loginacbtn(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void abrirhome(){
        Intent intent = new Intent(this, Home_Show.class);
        startActivity(intent);
    }

}