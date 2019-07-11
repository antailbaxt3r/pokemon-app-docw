package com.example.pokemonappdocw;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PokemonViewHolder extends RecyclerView.ViewHolder {

    TextView pokemonName;
    ImageView pokemonImage;
    String imageURL;

    public PokemonViewHolder(@NonNull View itemView) {
        super(itemView);
        pokemonName = itemView.findViewById(R.id.pokemon_name);
        pokemonImage = itemView.findViewById(R.id.pokemon_image);
    }
}
