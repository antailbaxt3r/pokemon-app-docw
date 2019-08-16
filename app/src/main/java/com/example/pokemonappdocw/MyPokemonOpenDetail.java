package com.example.pokemonappdocw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyPokemonOpenDetail extends AppCompatActivity {

    /**
     * Created By Antailbaxt3r
     */

    private SimpleDraweeView image;
    private CardView levelUpButton, evolveButton, addToPartyButton;
    private TextView number, pokemonName, generation, type, attack, defense, hp, specialAttack, specialDefense,
            speed, description, moves, level, currentStepTV, caughtAtStepTV, evolvesTo, evolvesAtLevel;
    private String numberText, pokemonNameText, generationText, typeText;
    private String attackText, defenseText, hpText,
            specialAttackText, specialDefenseText, speedText;
    private String descriptionText, imageURLText, type1, type2,
            numberTextFinal, move1, move2, moveText;
    private String levelText;
    private String currentStep, caughtAtStep;
    private int numberInt;
    private float hpSlope, attackSlope, defenseSlope, spAttackSlope, spDefenseSlope, speedSlope;

    private ValueEventListener pokeListener;

    private static int LEVEL_UP_STEP_DIFFERENCE = 200;
    private ShimmerFrameLayout shimmerFrameLayout;

    private DatabaseReference pokemonReference, userReference;
    private ProgressBar progressBar;

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(pokeListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(pokeListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pokemon_open_detail);
        attachID();

        pokeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                imageURLText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("imageURL").getValue().toString();
                pokemonNameText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("pokemonName").getValue().toString();
                numberText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("number").getValue().toString();
                generationText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("generation").getValue().toString();
                hpText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("hp").getValue().toString();
                type1 = dataSnapshot.child("allPokemon").child(pokemonNameText).child("type1").getValue().toString().toUpperCase();
                type2 = dataSnapshot.child("allPokemon").child(pokemonNameText).child("type2").getValue().toString().toUpperCase();
                attackText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("attack").getValue().toString();
                defenseText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("defense").getValue().toString();
                speedText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("speed").getValue().toString();
                specialAttackText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("specialAttack").getValue().toString();
                specialDefenseText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("specialDefence").getValue().toString();
                descriptionText = dataSnapshot.child("allPokemon").child(pokemonNameText).child("description").getValue().toString();
                move1 = dataSnapshot.child("allPokemon").child(pokemonNameText).child("move1").getValue().toString();
                move2 = dataSnapshot.child("allPokemon").child(pokemonNameText).child("move2").getValue().toString();
                hpSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("hpSlope").getValue().toString());
                attackSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("attackSlope").getValue().toString());
                defenseSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("defenseSlope").getValue().toString());
                spAttackSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("spAttackSlope").getValue().toString());
                spDefenseSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("spDefenseSlope").getValue().toString());
                speedSlope = Float.parseFloat(dataSnapshot.child("allPokemon").child(pokemonNameText).child("speedSlope").getValue().toString());
                evolvesAtLevel.setText(dataSnapshot.child("allPokemon").child(pokemonNameText).child("evolvesAtLevel").getValue().toString());
                evolvesTo.setText(dataSnapshot.child("allPokemon").child(pokemonNameText).child("evolvesTo").getValue().toString());

                Uri uri = Uri.parse(imageURLText);

                if(type2.isEmpty()){
                    typeText = type1;
                }else{
                    typeText = type1 + ", " + type2;
                }

                if(move2.isEmpty()){
                    moveText = move1;
                }else{
                    moveText = move1 + ", " + move2;
                }

                numberInt = Integer.parseInt(numberText);
                if(numberInt < 10){
                    numberTextFinal = "#00" + numberText;
                }else if(numberInt < 100){
                    numberTextFinal = "#0" + numberText;
                }else{
                    numberTextFinal = "#" + numberText;
                }

                caughtAtStep = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("pokemonList").child(pokemonNameText).child("caughtAtStep").getValue().toString();

                levelText = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("pokemonList").child(pokemonNameText).child("level").getValue().toString();

                currentStep = dataSnapshot.child("Users").child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child("currentStep").getValue().toString();



                image.setImageURI(uri);
                image.setBackgroundResource(R.color.white);
                pokemonName.setText(pokemonNameText);
                number.setText(numberTextFinal);
                generation.setText(generationText);
                hp.setText(hpText);
                type.setText(typeText);
                attack.setText(attackText);
                defense.setText(defenseText);
                speed.setText(speedText);
                specialDefense.setText(specialDefenseText);
                specialAttack.setText(specialAttackText);
                description.setText(descriptionText);
                moves.setText(moveText);

                System.out.println(currentStep);
                System.out.println(caughtAtStep);
                level.setText(levelText);
                caughtAtStepTV.setText(caughtAtStep);
                currentStepTV.setText(currentStep);

                int finalHP, finalAttack, finalDefense, finalSpAttack, finalSpDefense, finalSpeed, finalLevel;

                finalLevel = Integer.parseInt(level.getText().toString());

                finalHP = (int) (Integer.parseInt(hp.getText().toString()) + finalLevel * hpSlope);
                finalAttack = (int) (Integer.parseInt(attack.getText().toString()) + finalLevel * attackSlope);
                finalDefense = (int) (Integer.parseInt(defense.getText().toString()) + finalLevel * defenseSlope);
                finalSpAttack = (int) (Integer.parseInt(specialAttack.getText().toString()) + finalLevel * spAttackSlope);
                finalSpDefense = (int) (Integer.parseInt(specialDefense.getText().toString()) + finalLevel * spDefenseSlope);
                finalSpeed = (int) (Integer.parseInt(speed.getText().toString()) + finalLevel * speedSlope);

                hp.setText(String.valueOf(finalHP));
                attack.setText(String.valueOf(finalAttack));
                defense.setText(String.valueOf(finalDefense));
                specialDefense.setText(String.valueOf(finalSpDefense));
                specialAttack.setText(String.valueOf(finalSpAttack));
                speed.setText(String.valueOf(finalSpeed));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        pokemonNameText = getIntent().getStringExtra("name");
        pokemonReference = FirebaseDatabase.getInstance().getReference().child("allPokemon").child(pokemonNameText);
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        shimmerFrameLayout.startShimmer();
        progressBar.setVisibility(View.VISIBLE);

        addToPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPokemonToParty(pokemonName.getText().toString());
            }
        });

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(pokeListener);



        levelUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(currentStepTV.getText().toString() + " and " + caughtAtStepTV.getText().toString() + " and " + level);
                levelUp();
            }
        });

        evolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evolvePokemon();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }, 2000);



    }

    private void evolvePokemon() {
        //evolve pokemon
        int evolvesAtLevelInt = 0;
        final String evolvesToPokemon = evolvesTo.getText().toString();
        if (!evolvesAtLevel.getText().equals("")) {
            evolvesAtLevelInt = Integer.parseInt(evolvesAtLevel.getText().toString());
        }
        int currentLevel = Integer.parseInt(level.getText().toString());


        if(evolvesAtLevelInt == 0){
            Toast.makeText(this, "This Pokemon cannot evolve", Toast.LENGTH_SHORT).show();
        }else {
            if (currentLevel >= evolvesAtLevelInt) {
                if (evolvesAtLevelInt != 0 && !evolvesToPokemon.equals("")) {
                    evolve();
                } else {
                    Toast.makeText(this, "This Pokemon cannot evolve", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Your pokemon is not ready to evolve yet. Walk more to gain experience", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void evolve() {
        final String evolvesToPokemon = evolvesTo.getText().toString();
        int evolvesAtLevelInt = Integer.parseInt(evolvesAtLevel.getText().toString());


        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pokemon pokemon = new Pokemon();
                pokemon = dataSnapshot.child("allPokemon").child(evolvesToPokemon).getValue(Pokemon.class);

                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("pokemonList").child(pokemon.getPokemonName()).setValue(pokemon);

                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("pokemonList").child(evolvesToPokemon).child("level").setValue(Integer.parseInt(level.getText().toString()));

                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("pokemonList").child(evolvesToPokemon).child("caughtAtStep").setValue(Integer.parseInt(caughtAtStepTV.getText().toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        finish();
        Intent evolveIntent = new Intent(this, HomeActivity.class);
        startActivity(evolveIntent);

        Toast.makeText(getApplicationContext(), "Evolved!", Toast.LENGTH_SHORT).show();

        MediaPlayer mediaPlayer= MediaPlayer.create(this,R.raw.evolve);
        mediaPlayer.start();

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("pokemonList").child(pokemonName.getText().toString()).removeValue();

    }

    private void levelUp() {
        //level up pokemon

        int currentLevel = Integer.parseInt(level.getText().toString());
        int caughtAtStepInt = Integer.parseInt(caughtAtStepTV.getText().toString());
        int currentStepInt = Integer.parseInt(currentStepTV.getText().toString());
        int stepDifference = currentStepInt - caughtAtStepInt;

        int finalLevel = 1 + Math.round((float)stepDifference / (float)LEVEL_UP_STEP_DIFFERENCE);

        if (currentLevel == finalLevel){
            Toast.makeText(this, "You need more experience to level up this Pokemon. Walk more to increase experience", Toast.LENGTH_LONG).show();
        }else if (currentLevel == 100){
            Toast.makeText(this, "Your pokemon is already at level 100!", Toast.LENGTH_SHORT).show();
        }else{

            if (finalLevel >= 100){
                userReference.child("pokemonList").child(pokemonName.getText().toString()).child("level")
                        .setValue(100);
                Toast.makeText(this, "Pokemon level increased to level " + 100 + "!", Toast.LENGTH_SHORT).show();
            }else {
                userReference.child("pokemonList").child(pokemonName.getText().toString()).child("level")
                        .setValue(finalLevel);
                Toast.makeText(this, "Pokemon level increased to level " + finalLevel + "!", Toast.LENGTH_SHORT).show();
            }
        }

        startActivity(getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void addPokemonToParty(final String pokemonname) {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            if (dataSnapshot.child("party").getChildrenCount() <= 5){

                if (dataSnapshot.child("party").hasChild("pokemon1")) {
                    if (dataSnapshot.child("party").child("pokemon1").child("pokemonName").getValue().toString().equals(pokemonname)) {
                        Toast.makeText(MyPokemonOpenDetail.this, "This Pokemon is already in your party", Toast.LENGTH_SHORT).show();
                    return;}
                }
                if (dataSnapshot.child("party").hasChild("pokemon2")) {
                    if (dataSnapshot.child("party").child("pokemon2").child("pokemonName").getValue().toString().equals(pokemonname)) {
                        Toast.makeText(MyPokemonOpenDetail.this, "This Pokemon is already in your party", Toast.LENGTH_SHORT).show();
                    return;}
                }
                if (dataSnapshot.child("party").hasChild("pokemon3")) {
                    if (dataSnapshot.child("party").child("pokemon3").child("pokemonName").getValue().toString().equals(pokemonname)) {
                        Toast.makeText(MyPokemonOpenDetail.this, "This Pokemon is already in your party", Toast.LENGTH_SHORT).show();
                    return;}
                }
                if (dataSnapshot.child("party").hasChild("pokemon4")) {
                    if (dataSnapshot.child("party").child("pokemon4").child("pokemonName").getValue().toString().equals(pokemonname)) {
                        Toast.makeText(MyPokemonOpenDetail.this, "This Pokemon is already in your party", Toast.LENGTH_SHORT).show();
                    return;}
                }
                if (dataSnapshot.child("party").hasChild("pokemon5")) {
                    if (dataSnapshot.child("party").child("pokemon5").child("pokemonName").getValue().toString().equals(pokemonname)) {
                        Toast.makeText(MyPokemonOpenDetail.this, "This Pokemon is already in your party", Toast.LENGTH_SHORT).show();
                    return;}
                }
                if (dataSnapshot.child("party").hasChild("pokemon6")) {
                    if (dataSnapshot.child("party").child("pokemon6").child("pokemonName").getValue().toString().equals(pokemonname)) {
                        Toast.makeText(MyPokemonOpenDetail.this, "This Pokemon is already in your party", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                    if(dataSnapshot.child("party").getChildrenCount() == 0) {
                        Pokemon pokemon = dataSnapshot.child("pokemonList").child(pokemonname).getValue(Pokemon.class);
                        userReference.child("party").child("pokemon1").setValue(pokemon);
                        Toast.makeText(MyPokemonOpenDetail.this, "Pokemon added to the party", Toast.LENGTH_SHORT).show();
                    }
                    if(dataSnapshot.child("party").getChildrenCount() == 1) {
                        Pokemon pokemon = dataSnapshot.child("pokemonList").child(pokemonname).getValue(Pokemon.class);
                        userReference.child("party").child("pokemon2").setValue(pokemon);
                        Toast.makeText(MyPokemonOpenDetail.this, "Pokemon added to the party", Toast.LENGTH_SHORT).show();
                    }
                    if(dataSnapshot.child("party").getChildrenCount() == 2) {
                        Pokemon pokemon = dataSnapshot.child("pokemonList").child(pokemonname).getValue(Pokemon.class);
                        userReference.child("party").child("pokemon3").setValue(pokemon);
                        Toast.makeText(MyPokemonOpenDetail.this, "Pokemon added to the party", Toast.LENGTH_SHORT).show();
                    }
                    if(dataSnapshot.child("party").getChildrenCount() == 3) {
                        Pokemon pokemon = dataSnapshot.child("pokemonList").child(pokemonname).getValue(Pokemon.class);
                        userReference.child("party").child("pokemon4").setValue(pokemon);
                        Toast.makeText(MyPokemonOpenDetail.this, "Pokemon added to the party", Toast.LENGTH_SHORT).show();
                    }
                    if(dataSnapshot.child("party").getChildrenCount() == 4) {
                        Pokemon pokemon = dataSnapshot.child("pokemonList").child(pokemonname).getValue(Pokemon.class);
                        userReference.child("party").child("pokemon5").setValue(pokemon);
                        Toast.makeText(MyPokemonOpenDetail.this, "Pokemon added to the party", Toast.LENGTH_SHORT).show();
                    }
                    if(dataSnapshot.child("party").getChildrenCount() == 5) {
                        Pokemon pokemon = dataSnapshot.child("pokemonList").child(pokemonname).getValue(Pokemon.class);
                        userReference.child("party").child("pokemon6").setValue(pokemon);
                        Toast.makeText(MyPokemonOpenDetail.this, "Pokemon added to the party", Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(MyPokemonOpenDetail.this, "You already have 3 pokemon in your party. Clear your party to edit.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void attachID(){
        image = findViewById(R.id.poekmonImageInPokeDetail);
        pokemonName = findViewById(R.id.pokemonNameInPokeDetail);
        number = findViewById(R.id.pokemonNumberInPokeDetail);
        generation = findViewById(R.id.generationInPokeDetail);
        type = findViewById(R.id.typeInPokeDetail);
        attack = findViewById(R.id.attackInPokeDetail);
        defense = findViewById(R.id.defenseInPokeDetail);
        speed = findViewById(R.id.speedInPokeDetail);
        hp = findViewById(R.id.hpInPokeDetail);
        specialAttack = findViewById(R.id.specialAttackInPokeDetail);
        specialDefense = findViewById(R.id.specialDefenseInPokeDetail);
        description = findViewById(R.id.descriptionInPokeDetail);
        moves = findViewById(R.id.movesInPokeDetail);
        level = findViewById(R.id.levelInPokeDetail);
        currentStepTV = findViewById(R.id.currentStep);
        caughtAtStepTV = findViewById(R.id.caughtAtStep);
        levelUpButton = findViewById(R.id.level_up_button);
        evolveButton = findViewById(R.id.evolve_button);
        addToPartyButton = findViewById(R.id.add_to_party);
        evolvesAtLevel = findViewById(R.id.evolves_at_level);
        evolvesTo = findViewById(R.id.evolves_to);

        shimmerFrameLayout = findViewById(R.id.shimmerContainer_my_pokemon_detail);
        progressBar = findViewById(R.id.progress_bar);
    }


}