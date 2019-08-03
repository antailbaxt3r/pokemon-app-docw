package com.example.pokemonappdocw;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;


public class QRScannerFragment extends Fragment {

    FirebaseAuth firebaseAuth;

    private IntentIntegrator qrScan;

    public QRScannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qrscanner, container, false);

        FrameLayout scanButton = view.findViewById(R.id.scanButton);
        qrScan = new IntentIntegrator(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.setOrientationLocked(false);
                IntentIntegrator.forSupportFragment(QRScannerFragment.this).initiateScan();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Could not scan", Toast.LENGTH_SHORT).show();
            } else {

                try {

                    JSONObject obj = new JSONObject(result.getContents());

                    String type;
                    type = obj.getString("type");

                    switch(type){

                        case("POKEMON"):
                            scannedPokemon(obj);
                            break;
                        case ("ITEM"):
                            break;
                        case ("RESULTS"):
                            break;
                        default:
                            Toast.makeText(getActivity(), "Not a correct scan", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void scannedPokemon(final JSONObject pokemonDetails){

        final Pokemon scanned = new Pokemon();
        try {

            scanned.setPokemonName(pokemonDetails.getString("name"));
            scanned.setLegendary(pokemonDetails.getString("legendary"));
            scanned.setImageURL(pokemonDetails.getString("imageURL"));
            scanned.setType1(pokemonDetails.getString("type1"));
            scanned.setType2(pokemonDetails.getString("type2"));
            scanned.setDescription(pokemonDetails.getString("description"));
            scanned.setMove1(pokemonDetails.getString("move1"));
            scanned.setMove2(pokemonDetails.getString("move2"));

            scanned.setAttack(pokemonDetails.getInt("attack"));
            scanned.setDefense(pokemonDetails.getInt("defense"));
            scanned.setGeneration(pokemonDetails.getInt("generation"));
            scanned.setHp(pokemonDetails.getInt("hp"));
            scanned.setNumber(pokemonDetails.getInt("number"));
            scanned.setSpecialAttack(pokemonDetails.getInt("specialAttack"));
            scanned.setSpecialDefence(pokemonDetails.getInt("specialDefence"));
            scanned.setSpecialAttack(pokemonDetails.getInt("speed"));
            scanned.setTotal(pokemonDetails.getInt("total"));
            scanned.setLevel(pokemonDetails.getInt("level"));

            final DatabaseReference myPokemonReference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(firebaseAuth.getCurrentUser().getUid()).child("pokemonList");


            myPokemonReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    try {
                        if(dataSnapshot.hasChild(pokemonDetails.getString("name"))) {
                            Toast.makeText(getContext(), "You have already caught a " + pokemonDetails.getString("name") + "!", Toast.LENGTH_SHORT).show();
                        }else{
                            myPokemonReference.child(pokemonDetails.getString("name")).setValue(scanned);
                            Toast.makeText(getContext(), pokemonDetails.getString("name") + " was added to your Pokemon List!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (JSONException e){
            e.printStackTrace();
        }

    }
}
