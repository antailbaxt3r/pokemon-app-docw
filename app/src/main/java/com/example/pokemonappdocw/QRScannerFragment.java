package com.example.pokemonappdocw;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
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
    TextView tvSteps;

    private IntentIntegrator qrScan;

    public QRScannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qrscanner, container, false);

        tvSteps = view.findViewById(R.id.tv_steps);
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

        final DatabaseReference myPokemonReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(firebaseAuth.getCurrentUser().getUid()).child("pokemonList");

        final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(firebaseAuth.getCurrentUser().getUid());
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
                            Toast.makeText(getContext(), "You recieved a Potion!", Toast.LENGTH_SHORT).show();
                            scannedPotion();
                            break;
                        case ("RESULTS"):
                            Toast.makeText(getContext(), "Results Scanned!", Toast.LENGTH_SHORT).show();
                            scannedResults(obj);

                            break;
                        case ("POKECENTER"):
                            Toast.makeText(getContext(), "Your Pokemon Have Been Healed!", Toast.LENGTH_SHORT).show();
                            scannedCenter();
                            break;
                        default:
                            Toast.makeText(getActivity(), "Incorrect scan", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void scannedCenter() {

        FirebaseDatabase.getInstance().getReference()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot shot : dataSnapshot.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("party").getChildren()){
                            shot.child("hp").getRef().setValue(Integer.parseInt(dataSnapshot.child("allPokemon").child(shot.child("pokemonName").getValue().toString()).child("hp").getValue().toString()));
                            shot.child("alive").getRef().setValue(true);
                        }
                        for (DataSnapshot shot : dataSnapshot.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("pokemonList").getChildren()){
                            shot.child("hp").getRef().setValue(Integer.parseInt(dataSnapshot.child("allPokemon").child(shot.child("pokemonName").getValue().toString()).child("hp").getValue().toString()));
                            shot.child("alive").getRef().setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void scannedResults(final JSONObject results) {

        try {
            final boolean pokemon1Alive = results.getBoolean("pokemon1Alive");
            final boolean pokemon2Alive = results.getBoolean("pokemon2Alive");
            final boolean pokemon3Alive = results.getBoolean("pokemon3Alive");
            final boolean pokemon4Alive = results.getBoolean("pokemon4Alive");
            final boolean pokemon5Alive = results.getBoolean("pokemon5Alive");
            final boolean pokemon6Alive = results.getBoolean("pokemon6Alive");

            final int pokemon1HP = results.getInt("pokemon1HP");
            final int pokemon2HP = results.getInt("pokemon2HP");
            final int pokemon3HP = results.getInt("pokemon3HP");
            final int pokemon4HP = results.getInt("pokemon4HP");
            final int pokemon5HP = results.getInt("pokemon5HP");
            final int pokemon6HP = results.getInt("pokemon6HP");

            final String gymWonName = results.getString("gymWonName");

            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon1").child("alive").setValue(pokemon1Alive);
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon2").child("alive").setValue(pokemon2Alive);
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon3").child("alive").setValue(pokemon3Alive);
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon4").child("alive").setValue(pokemon4Alive);
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon5").child("alive").setValue(pokemon5Alive);
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon6").child("alive").setValue(pokemon6Alive);

            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon1").child("hp").setValue(pokemon1HP);
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon2").child("hp").setValue(pokemon2HP);
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon3").child("hp").setValue(pokemon3HP);
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon4").child("hp").setValue(pokemon4HP);
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon5").child("hp").setValue(pokemon5HP);
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .child("party").child("pokemon6").child("hp").setValue(pokemon6HP);


            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                                    .child("pokemonList").child(dataSnapshot.child("party").child("pokemon1").child("pokemonName").getValue().toString())
                                    .child("hp").setValue(pokemon1HP);
                            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                                    .child("pokemonList").child(dataSnapshot.child("party").child("pokemon2").child("pokemonName").getValue().toString())
                                    .child("hp").setValue(pokemon2HP);
                            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                                    .child("pokemonList").child(dataSnapshot.child("party").child("pokemon3").child("pokemonName").getValue().toString())
                                    .child("hp").setValue(pokemon3HP);

                            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                                    .child("pokemonList").child(dataSnapshot.child("party").child("pokemon1").child("pokemonName").getValue().toString())
                                    .child("alive").setValue(pokemon1Alive);
                            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                                    .child("pokemonList").child(dataSnapshot.child("party").child("pokemon2").child("pokemonName").getValue().toString())
                                    .child("alive").setValue(pokemon2Alive);
                            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                                    .child("pokemonList").child(dataSnapshot.child("party").child("pokemon3").child("pokemonName").getValue().toString())
                                    .child("alive").setValue(pokemon3Alive);

                            if (!gymWonName.equals("") && !gymWonName.equals(null)){

                                FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                                        .child("gymsWon").child(gymWonName).setValue("WON");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void scannedPotion() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                .child("itemList").child("potions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    int count = Integer.parseInt(dataSnapshot.getValue().toString());

                    if (count < 11) {
                        count++;
                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                                .child("itemList").child("potions").setValue(count);
                    } else {
                        Toast.makeText(getContext(), "You cannot carry more than 10 potions", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                            .child("itemList").child("potions").setValue(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

            scanned.setAlive(true);
            scanned.setSpeed(Integer.parseInt(pokemonDetails.getString("speed")));
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

            final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(firebaseAuth.getCurrentUser().getUid());


            myPokemonReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        if(dataSnapshot.hasChild(pokemonDetails.getString("name"))) {
                            Toast.makeText(getContext(), "You have already caught a " + pokemonDetails.getString("name") + "!", Toast.LENGTH_SHORT).show();
                        }else{
                            myPokemonReference.child(pokemonDetails.getString("name")).setValue(scanned);
                            Toast.makeText(getContext(), pokemonDetails.getString("name") + " was added to your Pokemon List!", Toast.LENGTH_SHORT).show();

                            MediaPlayer mediaPlayer= MediaPlayer.create(getContext(), R.raw.caught_pokemon);
                            mediaPlayer.start();

                            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try {
                                        myPokemonReference.child(pokemonDetails.getString("name")).child("caughtAtStep").setValue(Integer.parseInt(dataSnapshot.child("currentStep").getValue().toString()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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
