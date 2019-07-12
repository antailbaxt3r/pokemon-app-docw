package com.example.pokemonappdocw;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PokedexFragment extends Fragment {

    private DatabaseReference pokedexReference;
    private List<Pokemon> pokemonList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PokedexRVAdapter adapter = new PokedexRVAdapter(getContext(), pokemonList);

    private ShimmerFrameLayout shimmerFrameLayout;

    public PokedexFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);
        shimmerFrameLayout = view.findViewById(R.id.shimmerContainer_pokedex);
        shimmerFrameLayout.startShimmer();

        recyclerView = (RecyclerView) view.findViewById(R.id.pokedex_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        pokedexReference = FirebaseDatabase.getInstance().getReference().child("allPokemon");
        pokemonList.clear();


        pokedexReference.orderByChild("number").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot shot : dataSnapshot.getChildren()){
                    pokedexReference.orderByChild("number");
                    Pokemon pokemon = pokemon = shot.getValue(Pokemon.class);
                    pokemonList.add(pokemon);


                }

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
