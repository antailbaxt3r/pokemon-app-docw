package com.example.pokemonappdocw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    public void onBindViewHolder(@NonNull final PartyInItemStorageViewHolder holder, int position) {

        final Pokemon pokemon = pokemonList.get(position);
        holder.pokemonName.setText(pokemon.getPokemonName());
        Picasso.get().load(pokemon.getImageURL()).into(holder.pokemonImage);

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                int hpLeft, hpTotal, level;
                float hpSlope;
                float hpPercentage;

                level = Integer.parseInt(dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("pokemonList").child(pokemon.getPokemonName()).child("level").getValue().toString()) ;
                hpSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemon.getPokemonName()).child("hpSlope").getValue().toString());
                hpLeft = Integer.parseInt(dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("pokemonList").child(pokemon.getPokemonName()).child("hp").getValue().toString()) + (int)(level * hpSlope);
                hpTotal = (int) (Integer.parseInt(dataSnapshot.child("allPokemon").child(pokemon.getPokemonName()).child("hp").getValue().toString()) + (int) (level * hpSlope));

                hpPercentage = (float) ((float) hpLeft / (float) hpTotal) * 100;

                hpPercentage = (float) (Math.round(hpPercentage * 10)/10);

                holder.hpPercentage.setText(hpPercentage + "%");
                holder.pokeCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int hpLeft, hpTotal, level;
                        float hpSlope;
                        float hpPercentage;

                        level = Integer.parseInt(dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("pokemonList").child(pokemon.getPokemonName()).child("level").getValue().toString()) ;
                        hpSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemon.getPokemonName()).child("hpSlope").getValue().toString());
                        hpLeft = Integer.parseInt(dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("pokemonList").child(pokemon.getPokemonName()).child("hp").getValue().toString()) + (int)(level * hpSlope);
                        hpTotal = (int) (Integer.parseInt(dataSnapshot.child("allPokemon").child(pokemon.getPokemonName()).child("hp").getValue().toString()) + (int) (level * hpSlope));

                        hpPercentage = (float) ((float) hpLeft / (float) hpTotal) * 100;
                        System.out.println("\nleft : " + hpLeft + "\ntotal : " + hpTotal + "\npercentage : " + hpPercentage);

                        int hpLeftData = Integer.parseInt(dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("pokemonList").child(pokemon.getPokemonName()).child("hp").getValue().toString());
                        int hpTotalData = Integer.parseInt(dataSnapshot.child("allPokemon")
                                .child(pokemon.getPokemonName()).child("hp").getValue().toString());

                        System.out.println("\nleft data : " + hpLeftData + "\ntotal data : " + hpTotalData + "\npercentage data : " + hpPercentage);
                        if (hpLeftData != hpTotalData){

                            int count = Integer.parseInt(dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("itemList").child("potions").getValue().toString());
                            if (count != 0) {
                                if (hpTotalData - hpLeftData > 5) {
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("pokemonList").child(pokemon.getPokemonName()).child("hp").setValue(hpLeftData + 5);
                                }else{
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("pokemonList").child(pokemon.getPokemonName()).child("hp").setValue(hpTotalData);
                                }
                                count -= 1;
                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("itemList").child("potions").setValue(count);

                                Toast.makeText(context, "Health was increased!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "You don't have any more potions :(", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(context, "This Pokemon is already at maximum health", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
}