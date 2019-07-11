package com.example.pokemonappdocw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.ContentHandler;
import java.util.List;

public class myPokemonRVAdapter extends RecyclerView.Adapter<PokemonViewHolder> {

    Context context;
    private List<Pokemon> pokemonList;

    public myPokemonRVAdapter(List<Pokemon> pokemonList, Context context) {
        this.pokemonList = pokemonList;
        this.context = context;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokemon_item_format, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {

        Pokemon pokemon = pokemonList.get(position);
        holder.pokemonName.setText(pokemon.getPokemonName());
        Picasso.get().load(pokemon.getImageUrl()).into(holder.pokemonImage);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
}
