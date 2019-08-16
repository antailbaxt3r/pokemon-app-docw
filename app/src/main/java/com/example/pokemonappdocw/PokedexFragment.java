package com.example.pokemonappdocw;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PokedexFragment extends Fragment {

    private List<Pokemon> pokemonList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PokedexRVAdapter adapter;
    private androidx.appcompat.widget.SearchView searchView;
    private androidx.appcompat.widget.SearchView.OnQueryTextListener searchQuery;

    private ShimmerFrameLayout shimmerFrameLayout;


    public PokedexFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_button_toolbar) {// Not implemented here
            return false;
        }
        searchView.setOnQueryTextListener(searchQuery);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.pokedex_menu, menu);

        MenuItem search = menu.findItem(R.id.search_button_toolbar);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (search != null) {
            searchView = (androidx.appcompat.widget.SearchView) search.getActionView();
        }

        if (searchView != null){
            searchQuery = new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (adapter != null) {
                        adapter.getFilter().filter(s);
                    }

                    return false;
                }
            };
            searchView.setOnQueryTextListener(searchQuery);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);
        shimmerFrameLayout = view.findViewById(R.id.shimmerContainer_pokedex);
        shimmerFrameLayout.startShimmer();

        recyclerView = view.findViewById(R.id.pokedex_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        DatabaseReference pokedexReference = FirebaseDatabase.getInstance().getReference().child("allPokemon");
        pokemonList.clear();


        pokedexReference.orderByChild("number").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    Pokemon pokemon = shot.getValue(Pokemon.class);
//                    String imageURLText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("imageURL").getValue().toString();
//                    String pokemonNameText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("pokemonName").getValue().toString();
//                    String numberText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("number").getValue().toString();
//                    String generationText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("generation").getValue().toString();
//                    String hpText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("hp").getValue().toString();
//                    String type1 = dataSnapshot.child("allPokemon").child(pokemonNameText).child("type1").getValue().toString().toUpperCase();
//                    String type2 = dataSnapshot.child("allPokemon").child(pokemonNameText).child("type2").getValue().toString().toUpperCase();
//                    String attackText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("attack").getValue().toString();
//                    String defenseText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("defense").getValue().toString();
//                    String speedText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("speed").getValue().toString();
//                    String specialAttackText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("specialAttack").getValue().toString();
//                    String specialDefenseText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("specialDefence").getValue().toString();
//                    String descriptionText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("description").getValue().toString();
//                    String move1 = dataSnapshot.child("allPokemon").child(pokemonNameText).child("move1").getValue().toString();
//                    String move2 = dataSnapshot.child("allPokemon").child(pokemonNameText).child("move2").getValue().toString();
//                    float hpSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("hpSlope").getValue().toString());
//                    float attackSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("attackSlope").getValue().toString());
//                    float defenseSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("defenseSlope").getValue().toString());
//                    float spAttackSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("spAttackSlope").getValue().toString());
//                    float spDefenseSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("spDefenseSlope").getValue().toString());
//                    float speedSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("speedSlope").getValue().toString());
//                    float evolvesAtLevel.setText(dataSnapshot.child("allPokemon").child(pokemonNameText).child("evolvesAtLevel").getValue().toString());
//                    float evolvesTo.setText(dataSnapshot.child("allPokemon").child(pokemonNameText).child("evolvesTo").getValue().toString());
                    pokemonList.add(pokemon);
                    System.out.println(pokemon.getPokemonName());

                }

                adapter = new PokedexRVAdapter(getContext(), pokemonList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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