package com.example.pokemonappdocw;

import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    Fragment pokeFragment;
    DrawerLayout drawer;
    ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Fresco.initialize(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);

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

                public void onDrawerOpened(View drawerView)
                {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = true;
                }
            };

            mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.red700));
            mDrawerToggle.setDrawerIndicatorEnabled(true);
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
                    toolbar.setTitle(R.string.my_pokemon);
                    pokeFragment = new MyPokemonFragment();
                    loadFragment(pokeFragment);
                    return true;
                case R.id.navigation_scan:
                    toolbar.getMenu().clear();
                    toolbar.setTitle(R.string.scan);
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
}
