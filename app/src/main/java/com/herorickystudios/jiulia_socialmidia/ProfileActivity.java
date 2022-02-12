package com.herorickystudios.jiulia_socialmidia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextView textNomepf, textTelefonepf, nascimentopf, textNumeroSeguidores, textNumroAmigos;

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
                textNumeroSeguidores = findViewById(R.id.textNumeroSeguidores);
                textNumroAmigos = findViewById(R.id.textNumroAmigos);

                Log.i("FIREBASE", snapshot.getValue().toString());


                FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

                String user = usuarioLogado.getUid();

                String name = snapshot.child(user).child("nome").getValue().toString();
                String telefone = snapshot.child(user).child("telefone").getValue().toString();
                String data = snapshot.child(user).child("data de nascimento").getValue().toString();
                String amigo = snapshot.child(user).child("amigos").getValue().toString();
                String seguidor = snapshot.child(user).child("seguidores").getValue().toString();


                textNomepf.setText("Nome: " + name);
                textTelefonepf.setText("Telefone: " + telefone);
                nascimentopf.setText("Data de Nascimento: " + data);
                textNumroAmigos.setText(amigo);
                textNumeroSeguidores.setText(seguidor);



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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's a download in progress, save the reference so you can query it later
        if (storageRef != null) {
            outState.putString("reference", storageRef.toString());
        }
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        // If there was a download in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString("reference");
        if (stringRef == null) {
            return;
        }
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

        // Find all DownloadTasks under this StorageReference (in this example, there should be one)
        List<FileDownloadTask> tasks = storageRef.getActiveDownloadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the download
            FileDownloadTask task = tasks.get(0);

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(this, new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot state) {
                    // Success!
                    // ...
                }
            });
        }
    }
}