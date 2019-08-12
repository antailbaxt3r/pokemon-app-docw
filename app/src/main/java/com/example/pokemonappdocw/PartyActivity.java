package com.example.pokemonappdocw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PartyActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    MyPokemonRVAdapter adapter;
    List<Pokemon> pokemonList = new ArrayList<>();

    CardView clearButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        attachID();

        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        pokemonList.clear();
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("party")){

                        for (DataSnapshot shot : dataSnapshot.child("party").getChildren()) {
                            Pokemon pokemon = shot.getValue(Pokemon.class);
                            pokemonList.add(pokemon);
                        }

                        adapter = new MyPokemonRVAdapter(pokemonList, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(PartyActivity.this, "You have still not created a party", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pokemonList.clear();
                adapter = new MyPokemonRVAdapter(pokemonList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Toast.makeText(PartyActivity.this, "Party Cleared", Toast.LENGTH_SHORT).show();
                userReference.child("party").removeValue();
            }
        });

    }

    private void attachID() {

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getDrawable(R.drawable.ic_arrow_back_red_24dp));
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.party_rv);
        clearButton = findViewById(R.id.clear_party);
    }
}