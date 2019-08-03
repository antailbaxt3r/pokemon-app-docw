package com.example.pokemonappdocw;

import com.example.pokemonappdocw.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ItemStorage extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView potions, revives;
    private CardView potionCard, reviveCard;
    private DatabaseReference itemReference;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_storage);

        firebaseAuth = FirebaseAuth.getInstance();
        attachId();

        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        System.out.println(firebaseAuth.getUid());

        itemReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String potionCount = "", reviveCount = "";
                    if (dataSnapshot.child("potions").exists())
                        potionCount = dataSnapshot.child("potions").getValue().toString();
                    if (dataSnapshot.child("revives").exists())
                        reviveCount = dataSnapshot.child("revives").getValue().toString();

                    if (potionCount.isEmpty() || potionCount.equals("0")) {
                        potions.setText(getString(R.string.x0));
                    } else {
                        potions.setText("x" + potionCount);
                    }

                    if (reviveCount.isEmpty() || reviveCount.equals("0")) {
                        revives.setText(getString(R.string.x0));
                    } else {
                        revives.setText("x" + reviveCount);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        potionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog potionDialog = new Dialog(ItemStorage.this);
                potionDialog.setContentView(R.layout.dialog_box);
                potionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView heading =  potionDialog.findViewById(R.id.dialog_box_heading);
                heading.setText(getString(R.string.potion));
                TextView body = potionDialog.findViewById(R.id.dialog_box_body);
                body.setText(getString(R.string.usepotion));
                Button positiveButton = potionDialog.findViewById(R.id.dialog_box_positive_button);
                positiveButton.setText(getString(R.string.yes));
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        usePotion();
                        itemReference.child("potions").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int count = Integer.parseInt(dataSnapshot.getValue().toString());
                                if(count != 0){
                                    count--;
                                    itemReference.child("potions").setValue(count);
                                }
                                else{
                                    Toast.makeText(ItemStorage.this, "You don't have any potions :(", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        potionDialog.dismiss();

                    }
                });
                Button negativeButton = potionDialog.findViewById(R.id.dialog_box_negative_button);
                negativeButton.setText(getString(R.string.cancel));
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        potionDialog.dismiss();
                    }
                });

                potionDialog.show();




            }
        });

        reviveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog potionDialog = new Dialog(ItemStorage.this);
                potionDialog.setContentView(R.layout.dialog_box);
                potionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView heading =  potionDialog.findViewById(R.id.dialog_box_heading);
                heading.setText(getString(R.string.revive));
                TextView body = potionDialog.findViewById(R.id.dialog_box_body);
                body.setText(getString(R.string.useRevive));
                Button positiveButton = potionDialog.findViewById(R.id.dialog_box_positive_button);
                positiveButton.setText(getString(R.string.yes));
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        useRevive();
                        itemReference.child("revives").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int count = Integer.parseInt(dataSnapshot.getValue().toString());

                                if(count != 0){
                                    count--;
                                    itemReference.child("revives").setValue(count);
                                }
                                else{
                                    Toast.makeText(ItemStorage.this, "You don't have any revives :(", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        potionDialog.dismiss();
                    }
                });
                Button negativeButton = potionDialog.findViewById(R.id.dialog_box_negative_button);
                negativeButton.setText(getString(R.string.cancel));
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        potionDialog.dismiss();
                    }
                });

                potionDialog.show();

            }
        });

    }

    private void useRevive() {
        //code to use Revive
    }

    private void usePotion() {
        //code to use Potion
    }

    private void attachId(){

        potions = findViewById(R.id.potionCount);
        revives = findViewById(R.id.reviveCount);
        potionCard = findViewById(R.id.potionCard);
        reviveCard = findViewById(R.id.reviveCard);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getDrawable(R.drawable.ic_arrow_back_red_24dp));
        setSupportActionBar(toolbar);

        itemReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                .child("itemList");
    }
}
