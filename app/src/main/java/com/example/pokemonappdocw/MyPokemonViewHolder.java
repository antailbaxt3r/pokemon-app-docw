package com.example.pokemonappdocw;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MyPokemonViewHolder extends RecyclerView.ViewHolder{

    TextView pokemonName;
    ImageView pokemonImage;
    CardView pokecard;

    public MyPokemonViewHolder(@NonNull final View itemView) {
        super(itemView);

        pokemonName = itemView.findViewById(R.id.pokemon_name);
        pokemonImage = itemView.findViewById(R.id.pokemon_image);
        pokecard = itemView.findViewById(R.id.pokeCard);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MyPokemonOpenDetail.class);
                intent.putExtra("name", pokemonName.getText().toString());
                view.getContext().startActivity(intent);
            }
        });
    }


}
