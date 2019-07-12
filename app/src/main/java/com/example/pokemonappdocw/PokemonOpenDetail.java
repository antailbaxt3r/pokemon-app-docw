package com.example.pokemonappdocw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PokemonOpenDetail extends AppCompatActivity {

    private SimpleDraweeView image;
    private TextView number, pokemonName, generation, type, attack, defense, hp, specialAttack, specialDefense,
            speed, description;
    private String numberText, pokemonNameText, generationText, typeText, attackText, defenseText, hpText,
            specialAttackText, specialDefenseText, speedText, descriptionText, imageURLText, type1, type2
            ,numberTextFinal;
    private int numberInt;

    private ShimmerFrameLayout shimmerFrameLayout;

    private DatabaseReference pokemonReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_open_detail);
        attachID();

        pokemonNameText = getIntent().getStringExtra("name");
        pokemonReference = FirebaseDatabase.getInstance().getReference().child("allPokemon").child(pokemonNameText);

        shimmerFrameLayout.startShimmer();

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
//                descriptionText = dataSnapshot.child("description").getValue().toString();

                Uri uri = Uri.parse(imageURLText);

                if(type2.isEmpty()){
                    typeText = type1;
                }else{
                    typeText = type1 + ", " + type2;
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
//                description.setText(descriptionText);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.INVISIBLE);
    }

    private void attachID(){
        image = findViewById(R.id.poekmonImageInDetail);
        pokemonName = findViewById(R.id.pokemonNameInDetail);
        number = findViewById(R.id.pokemonNumberInOpenDetail);
        generation = findViewById(R.id.generationInDetail);
        type = findViewById(R.id.typeInDetail);
        attack = findViewById(R.id.attackInDetail);
        defense = findViewById(R.id.defenseInDetail);
        speed = findViewById(R.id.speedInDetail);
        hp = findViewById(R.id.hpInDetail);
        specialAttack = findViewById(R.id.specialAttackInDetail);
        specialDefense = findViewById(R.id.specialDefenseInDetail);
        description = findViewById(R.id.descriptionInDetail);
        shimmerFrameLayout = findViewById(R.id.shimmerContainer_pokedex);
    }


}
