package com.example.pokemonappdocw;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements SensorEventListener {

    Toolbar toolbar;
    Fragment pokeFragment;
    DrawerLayout drawer;
    ActionBarDrawerToggle mDrawerToggle;
    TextView displayNameInDrawer;
    NavigationView drawerNavView;

    TextView tvSteps;
    private SensorManager sensorManager;
    private boolean running = false;

    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Fresco.initialize(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        tvSteps = (TextView) findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        drawerNavView = findViewById(R.id.nav_view_drawer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {

            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer)
            {

                public void onDrawerClosed(View view)
                {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = false;
                }

                @Override
                public boolean onOptionsItemSelected(MenuItem item) {
                    return super.onOptionsItemSelected(item);
                }

                public void onDrawerOpened(View drawerView)
                {
                    displayNameInDrawer = findViewById(R.id.name_in_drawer);
                    userReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String hiText = "Hi, " + Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString().split(" ")[0];
                            displayNameInDrawer.setText(hiText);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    supportInvalidateOptionsMenu();

                    drawerNavView.setNavigationItemSelectedListener(drawerNavOptions);

                    //drawerOpened = true;
                }
            };

            mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.red700));
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            //noinspection deprecation
            drawer.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }




        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment fragment = new MyPokemonFragment();
        loadFragment(fragment);
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_my_pokemon:
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.pokedex_menu);
                    toolbar.setTitle(R.string.my_pokemon);
                    pokeFragment = new MyPokemonFragment();
                    loadFragment(pokeFragment);
                    return true;
                case R.id.navigation_scan:
                    toolbar.getMenu().clear();
                    toolbar.setTitle(R.string.scan);
                    pokeFragment = new QRScannerFragment();
                    loadFragment(pokeFragment);
                    return true;
                case R.id.navigation_pokedex:
                    toolbar.setTitle(R.string.pokedex);
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.pokedex_menu);
                    pokeFragment = new PokedexFragment();
                    loadFragment(pokeFragment);
                    return true;
            }
            return false;
        }
    };

    private NavigationView.OnNavigationItemSelectedListener drawerNavOptions = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            switch (menuItem.getItemId()){

                case(R.id.aboutUs):
                    break;

                case(R.id.logOut):
                    firebaseAuth.signOut();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case (R.id.item_storage_drawer):
                    Intent storageIntent = new Intent(HomeActivity.this, ItemStorage.class);
                    startActivity(storageIntent);
                    break;

                case (R.id.item_step_counter):
                    Intent stepIntent = new Intent(HomeActivity.this, StepCounterActivity.class);
                    startActivity(stepIntent);
                    break;
            }

            return false;
        }
    };

    @Override
    protected void onPostResume() {
        super.onPostResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this, "Sensor Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        running = false;
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (running){
            tvSteps.setText(String.valueOf(Math.round(sensorEvent.values[0])));
            userReference.child("currentStep").setValue(Integer.parseInt(tvSteps.getText().toString()));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
