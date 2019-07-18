package com.example.pokemonappdocw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

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
        return viewHolder;
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


    public Filter getFilter(){
        return pokemonFilter;
    }

    private Filter pokemonFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Pokemon> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(pokemonListFiltered);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Pokemon item : pokemonListFiltered){

                    if(item.getPokemonName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            pokemonList.clear();
            pokemonList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
