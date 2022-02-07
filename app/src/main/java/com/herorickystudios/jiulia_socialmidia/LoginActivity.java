package com.herorickystudios.jiulia_socialmidia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText lemail, lsenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lemail = findViewById(R.id.lgEmail);
        lsenha = findViewById(R.id.lgSenha);
    }
}