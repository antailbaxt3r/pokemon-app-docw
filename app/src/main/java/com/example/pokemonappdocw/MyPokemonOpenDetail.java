package com.example.pokemonappdocw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyPokemonOpenDetail extends AppCompatActivity {

    private SimpleDraweeView image;
    private CardView levelUpButton, evolveButton, addToPartyButton;
    private TextView number, pokemonName, generation, type, attack, defense, hp, specialAttack, specialDefense,
            speed, description, moves, level, currentStepTV, caughtAtStepTV;
    private String numberText, pokemonNameText, generationText, typeText, attackText, defenseText, hpText,
            specialAttackText, specialDefenseText, speedText, descriptionText, imageURLText, type1, type2
            ,numberTextFinal, move1, move2, moveText, levelText, currentStep, caughtAtStep;
    private int numberInt;

    private ShimmerFrameLayout shimmerFrameLayout;

    private DatabaseReference pokemonReference, userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pokemon_open_detail);
        attachID();

        pokemonNameText = getIntent().getStringExtra("name");
        pokemonReference = FirebaseDatabase.getInstance().getReference().child("allPokemon").child(pokemonNameText);
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        shimmerFrameLayout.startShimmer();

        addToPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPokemonToParty(pokemonName.getText().toString());
            }
        });

        pokemonReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                imageURLText = dataSnapshot.child("imageURL").getValue().toString();
                pokemonNameText = dataSnapshot.child("pokemonName").getValue().toString();
                numberText = dataSnapshot.child("number").getValue().toString();
                generationText = dataSnapshot.child("generation").getValue().toString();
                hpText = dataSnapshot.child("hp").getValue().toString();
                type1 = dataSnapshot.child("type1").getValue().toString().toUpperCase();
                type2 = dataSnapshot.child("type2").getValue().toString().toUpperCase();
                attackText = dataSnapshot.child("attack").getValue().toString();
                defenseText = dataSnapshot.child("defense").getValue().toString();
                speedText = dataSnapshot.child("speed").getValue().toString();
                specialAttackText = dataSnapshot.child("specialAttack").getValue().toString();
                specialDefenseText = dataSnapshot.child("specialDefence").getValue().toString();
                descriptionText = dataSnapshot.child("description").getValue().toString();
                move1 = dataSnapshot.child("move1").getValue().toString();
                move2 = dataSnapshot.child("move2").getValue().toString();

                if (dataSnapshot.child("caughtAtStep").getValue() != null) {
                    caughtAtStep = dataSnapshot.child("caughtAtStep").getValue().toString();
                }

                if (dataSnapshot.child("level").getValue() != null)
                    levelText = dataSnapshot.child("level").getValue().toString();

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("pokemonList").child(pokemonNameText).child("caughtAtStep").getValue() != null) {
                            caughtAtStep = dataSnapshot.child("pokemonList").child(pokemonNameText).child("caughtAtStep").getValue().toString();
                        }

                        if (dataSnapshot.child("pokemonList").child(pokemonNameText).child("level").getValue() != null)
                            levelText = dataSnapshot.child("pokemonList").child(pokemonNameText).child("level").getValue().toString();

                        currentStep = dataSnapshot.child("currentStep").getValue().toString();

                        System.out.println(currentStep);
                        System.out.println(caughtAtStep);
                        level.setText(levelText);
                        caughtAtStepTV.setText(caughtAtStep);
                        currentStepTV.setText(currentStep);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        levelUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int level = Integer.parseInt(levelText);
                System.out.println(currentStepTV.getText().toString() + " and " + caughtAtStepTV.getText().toString() + " and " + level);

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
            }
        }, 1000);
    }

    private void addPokemonToParty(final String pokemonname) {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("party").hasChild(pokemonname)){
                    Toast.makeText(MyPokemonOpenDetail.this, "This Pokemon is already in your party", Toast.LENGTH_SHORT).show();
                }else if (dataSnapshot.child("party").getChildrenCount() <= 5){
                    Pokemon pokemon = dataSnapshot.child("pokemonList").child(pokemonname).getValue(Pokemon.class);
                    userReference.child("party").child(pokemonname).setValue(pokemon);
                    Toast.makeText(MyPokemonOpenDetail.this, "Pokemon added to the party", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyPokemonOpenDetail.this, "You already have 6 pokemon in your party. Clear your party to edit.", Toast.LENGTH_SHORT).show();
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

        shimmerFrameLayout = findViewById(R.id.shimmerContainer_my_pokemon_detail);
    }


}
