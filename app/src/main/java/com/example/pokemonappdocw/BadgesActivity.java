package com.example.pokemonappdocw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BadgesActivity extends AppCompatActivity {

    Toolbar toolbar;

    private ImageView one, two, three, four;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getDrawable(R.drawable.ic_arrow_back_red_24dp));
        setSupportActionBar(toolbar);

        one  = findViewById(R.id.badge1);
        two  = findViewById(R.id.badge2);
        three  = findViewById(R.id.badge3);
        four  = findViewById(R.id.badge4);

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


        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("gymsWon").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("gym1").exists()){
                    if (dataSnapshot.child("gym1").getValue().toString().equals("WON")){
                        one.setVisibility(View.VISIBLE);
                    }else{
                        one.setVisibility(View.INVISIBLE);
                    }
                }
                if (dataSnapshot.child("gym2").exists()){
                    if (dataSnapshot.child("gym2").getValue().toString().equals("WON")){
                        two.setVisibility(View.VISIBLE);
                    }else{
                        two.setVisibility(View.INVISIBLE);
                    }
                }
                if (dataSnapshot.child("gym3").exists()){
                    if (dataSnapshot.child("gym3").getValue().toString().equals("WON")){
                        three.setVisibility(View.VISIBLE);
                    }else{
                        three.setVisibility(View.INVISIBLE);
                    }
                }
                if (dataSnapshot.child("gym4").exists()){
                    if (dataSnapshot.child("gym4").getValue().toString().equals("WON")){
                        four    .setVisibility(View.VISIBLE);
                    }else{
                        four.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
