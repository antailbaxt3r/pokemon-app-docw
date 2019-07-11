package com.example.pokemonappdocw;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PokedexFragment extends Fragment {

    private DatabaseReference pokedexReference = FirebaseDatabase.getInstance().getReference().child("allPokemon");
    private List<Pokemon> pokemonList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public PokedexFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.pokedex_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pokedexReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot shot : dataSnapshot.getChildren()){

                    Pokemon pokemon = new Pokemon();
                    pokemon = shot.getValue(Pokemon.class);
                    pokemonList.add(pokemon);
                }
                adapter.notifyDataSetChanged();
                adapter = new PokedexRVAdapter(getContext(), pokemonList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
