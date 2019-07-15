package com.example.pokemonappdocw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyPokemonRVAdapter extends RecyclerView.Adapter<MyPokemonViewHolder> {

    Context context;
    private List<Pokemon> pokemonList;
    List<Pokemon> pokemonListFiltered;

    public MyPokemonRVAdapter(List<Pokemon> pokemonList, Context context) {
        this.pokemonList = pokemonList;
        this.context = context;
        this.pokemonListFiltered = new ArrayList<>(pokemonList);
    }

    @NonNull
    @Override
    public MyPokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokemon_item_format, parent, false);
        MyPokemonViewHolder viewHolder = new MyPokemonViewHolder(itemView);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPokemonViewHolder holder, int position) {

        Pokemon pokemon = pokemonList.get(position);
        holder.pokemonName.setText(pokemon.getPokemonName());
        Picasso.get().load(pokemon.getImageURL()).into(holder.pokemonImage);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
}
