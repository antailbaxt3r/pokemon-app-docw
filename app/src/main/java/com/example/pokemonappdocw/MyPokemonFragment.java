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
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MyPokemonFragment extends Fragment {


    public MyPokemonFragment() {
        // Required empty public constructor
    }


    private List<Pokemon> pokemonList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyPokemonRVAdapter adapter;
    private androidx.appcompat.widget.SearchView searchView;
    private androidx.appcompat.widget.SearchView.OnQueryTextListener searchQuery;

    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout caughtAtleastOne, caughtNone;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_button_toolbar) {
            // Not implemented here
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
        View view = inflater.inflate(R.layout.fragment_my_pokemon, container, false);
        shimmerFrameLayout = view.findViewById(R.id.shimmerContainer_my_pokemon);
        shimmerFrameLayout.startShimmer();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        recyclerView = view.findViewById(R.id.my_pokemon_rv);
        caughtNone = view.findViewById(R.id.caughtNone);
        caughtAtleastOne = view.findViewById(R.id.caughtAtleastOne);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        pokemonList.clear();


        userReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("pokemonList")){

                    if(dataSnapshot.child("pokemonList").getChildrenCount() != 0){
                        caughtNone.setVisibility(View.GONE);
                        caughtAtleastOne.setVisibility(View.VISIBLE);
                        for (DataSnapshot shot : dataSnapshot.child("pokemonList").getChildren()) {
                            Pokemon pokemon = shot.getValue(Pokemon.class);
                            pokemonList.add(pokemon);

                        }

                        adapter = new MyPokemonRVAdapter(pokemonList, getContext());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.INVISIBLE);
                    }else{
                        caughtAtleastOne.setVisibility(View.GONE);
                        caughtNone.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.INVISIBLE);
                    }


                }else{
                    caughtAtleastOne.setVisibility(View.GONE);
                    caughtNone.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
