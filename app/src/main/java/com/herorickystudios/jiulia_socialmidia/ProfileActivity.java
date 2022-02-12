package com.herorickystudios.jiulia_socialmidia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ProfileActivity extends AppCompatActivity {

    TextView textNomepf, textTelefonepf, nascimentopf;

    ImageButton imageButton6, imageCapa;

    StorageReference storageReference;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance(FirebaseApp.getInstance(), "gs://jiulia-56db4.appspot.com");

    StorageReference storageRef = firebaseStorage.getReference();

    // Get a non-default Storage bucket
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://my-custom-bucket");

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
                imageButton6 = findViewById(R.id.imageButton6);
                imageCapa = findViewById(R.id.imageCapa);

                Log.i("FIREBASE", snapshot.getValue().toString());


                FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

                String user = usuarioLogado.getUid();

                String name = snapshot.child(user).child("nome").getValue().toString();
                String telefone = snapshot.child(user).child("telefone").getValue().toString();
                String data = snapshot.child(user).child("data de nascimento").getValue().toString();


                textNomepf.setText("Nome: " + name);
                textTelefonepf.setText("Telefone: " + telefone);
                nascimentopf.setText("Data de Nascimento: " + data);



                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("image/jpg")
                        .build();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void selecionarFoto(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selecionar foto"), 1);
    }

    public void selecionarCapa(View view){
    Intent intent1 = new Intent();
    intent1.setType("image/*");
    intent1.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent1, "Selecionar capa"),3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(requestCode);

        if(data == null){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }

        if(requestCode == 3){
            Toast.makeText(this, "Capa inicializada", Toast.LENGTH_SHORT).show();
            Uri uri1 = data.getData();

            imageCapa.setImageURI(uri1);

            enviarImagem();

            FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

            String user = usuarioLogado.getUid();


            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();

            // Upload the file and metadata
            UploadTask uploadTask = storageRef.child(user).child("images/capa.jpg").putFile(uri1, metadata);
        }

        if (requestCode == 1){
            Uri uri = data.getData();

            imageButton6.setImageURI(uri);


            enviarImagem();

            FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

            String user = usuarioLogado.getUid();


            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();

// Upload the file and metadata
            UploadTask uploadTask = storageRef.child(user).child("images/profile.jpg").putFile(uri, metadata);

        }


        String date = data.toString();





    }

    private void enviarImagem() {

        Bitmap bitmap = ((BitmapDrawable)imageButton6.getDrawable()).getBitmap();
        ByteArrayOutputStream ByteArrayOutStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutStream);
        byte[] imagem = ByteArrayOutStream.toByteArray();



        StorageReference imagesRef = storageRef.child("images");


        UploadTask uploadTask = storageRef.putBytes(imagem);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(ProfileActivity.this, "Upload feito com sucesso!", Toast.LENGTH_LONG).show();

            }
        });
    }
}