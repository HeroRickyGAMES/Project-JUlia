package com.herorickystudios.jiulia_socialmidia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.herorickystudios.jiulia_socialmidia.placeholder.PlaceholderContent.PlaceholderItem;
import com.herorickystudios.jiulia_socialmidia.databinding.FragmentItemBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MypostRecyclerViewAdapter extends RecyclerView.Adapter<MypostRecyclerViewAdapter.ViewHolder> {

    public DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("usuario");

    private final List<PlaceholderItem> mValues;

    public MypostRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();
        String user = usuarioLogado.getUid();

        DatabaseReference referencia2 = FirebaseDatabase.getInstance().getReference("usuario").child(user).child("Posts");

        referencia2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                snapshot.getValue();
                String db = String.valueOf(snapshot.getKey());
                String valor = String.valueOf((db));

                //int post = Integer.parseInt(0 + numberposts);;
                System.out.println(db);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        referencia.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                Log.i("FIREBASE", snapshot.getValue().toString());


                FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

                String user = usuarioLogado.getUid();

                //final String numberposts = snapshot.child(user).child("numerodeposts").getValue().toString();

                //System.out.println(numberposts);

                //final String PostsProfile = snapshot.child(user).child("Posts").child(String.valueOf(position)).getValue().toString();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mIdView;
        public final TextView mContentView;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}