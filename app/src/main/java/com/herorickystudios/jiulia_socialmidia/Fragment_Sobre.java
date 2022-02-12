package com.herorickystudios.jiulia_socialmidia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Sobre#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Sobre extends Fragment {

    public DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("usuario");
    TextView idade, telefone;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Sobre() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Sobre.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Sobre newInstance(String param1, String param2) {
        Fragment_Sobre fragment = new Fragment_Sobre();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__sobre, container, false);



        referencia.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //View By ID
                idade = view.findViewById(R.id.textIdade);
                telefone = view.findViewById(R.id.textTelefone);

                Log.i("FIREBASE", snapshot.getValue().toString());


                FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

                String user = usuarioLogado.getUid();

                String idadee = snapshot.child(user).child("data de nascimento").getValue().toString();
                String telefonee = snapshot.child(user).child("telefone").getValue().toString();

                idade.setText("Data de Nascimento: " + idadee);
                telefone.setText("telefone: " + telefonee);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "BA NI DO... ~ Edinaldo Pereira", Toast.LENGTH_SHORT).show();
            }
        });


    return view;
    }
}