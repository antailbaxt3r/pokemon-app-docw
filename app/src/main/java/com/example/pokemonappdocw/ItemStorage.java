package com.example.pokemonappdocw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemStorage extends AppCompatActivity {

    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout noItemLayout;
    private RecyclerView recyclerView;
    private ItemRVAdapter adapter;

    private Toolbar toolbar;

    private List<Item> itemList = new ArrayList<>();

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

        shimmerFrameLayout.startShimmer();

        itemList.clear();
        itemReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    Item item = shot.getValue(Item.class);
                    itemList.add(item);
                }

                if (itemList.isEmpty()){
                    noItemLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else {
                    adapter = new ItemRVAdapter(getApplicationContext(), itemList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    noItemLayout.setVisibility(View.GONE);
                }

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void attachId(){

        shimmerFrameLayout = findViewById(R.id.shimmerContainer_item_storage);
        noItemLayout = findViewById(R.id.noItemsLayout);
        recyclerView = findViewById(R.id.item_storage_rv);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(getDrawable(R.drawable.ic_arrow_back_red_24dp));
        setSupportActionBar(toolbar);

        itemReference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid()).child("itemList");
    }
}
