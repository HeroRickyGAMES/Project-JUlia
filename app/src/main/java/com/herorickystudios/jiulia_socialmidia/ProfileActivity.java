package com.herorickystudios.jiulia_socialmidia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView textNomepf, textTelefonepf, nascimentopf;

    public DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("usuario");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        referencia.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                textNomepf = findViewById(R.id.textNomepf);
                textTelefonepf = findViewById(R.id.textTelefonepf);
                nascimentopf = findViewById(R.id.nascimentopf);

                Log.i("FIREBASE", snapshot.getValue().toString());


                FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

                String user = usuarioLogado.getUid();

                String name = snapshot.child(user).child("nome").getValue().toString();
                String telefone = snapshot.child(user).child("telefone").getValue().toString();
                String data = snapshot.child(user).child("data de nascimento").getValue().toString();


                textNomepf.setText("Nome: " + name);
                textTelefonepf.setText("Telefone: " + telefone);
                nascimentopf.setText("Data de Nascimento: " + data);



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}