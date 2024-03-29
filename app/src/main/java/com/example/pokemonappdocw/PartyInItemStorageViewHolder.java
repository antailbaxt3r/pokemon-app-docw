package com.example.pokemonappdocw;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class PartyInItemStorageViewHolder extends RecyclerView.ViewHolder {

    TextView pokemonName, hpPercentage;
    ImageView pokemonImage;
    CardView pokeCard;

    public PartyInItemStorageViewHolder(@NonNull View itemView) {
        super(itemView);

        pokemonName = itemView.findViewById(R.id.pokemon_name);
        hpPercentage = itemView.findViewById(R.id.hp_percentage);
        pokemonImage = itemView.findViewById(R.id.pokemon_image);
        pokeCard = itemView.findViewById(R.id.pokeCard);

    }
}
