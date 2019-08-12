package com.example.pokemonappdocw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PartyInItemStorageRVAdapter extends RecyclerView.Adapter<PartyInItemStorageViewHolder> {

    private Context context;
    private List<Pokemon> pokemonList;

    public PartyInItemStorageRVAdapter(Context context, List<Pokemon> pokemonList) {
        this.context = context;
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    public PartyInItemStorageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokemon_in_item_storage_layout, parent, false);
        PartyInItemStorageViewHolder viewHolder = new PartyInItemStorageViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PartyInItemStorageViewHolder holder, int position) {

        final Pokemon pokemon = pokemonList.get(position);
        holder.pokemonName.setText(pokemon.getPokemonName());
        Picasso.get().load(pokemon.getImageURL()).into(holder.pokemonImage);

        int hpLeft, hpTotal;

        hpLeft = pokemon.getHp();

        holder.pokeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                DatabaseReference pokedexPokemonReference = FirebaseDatabase.getInstance().getReference()
                        .child("allPokemon").child(pokemon.getPokemonName());

            }
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
}
