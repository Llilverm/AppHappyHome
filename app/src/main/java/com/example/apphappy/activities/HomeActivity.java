package com.example.apphappy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.apphappy.R;
import com.example.apphappy.fragment.ChatsFragment;
import com.example.apphappy.fragment.FiltersFragment;
import com.example.apphappy.fragment.HomeFragment;
import com.example.apphappy.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //instancias
        bottomNavigation=findViewById(R.id.bottom_navigation); //con esto cada vez que le de click se va para cada una de ellas
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    }

    //lancemos los fragments

    void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.itemHome) {
                        openFragment(new HomeFragment());

                    } else if (item.getItemId() == R.id.itemFilter) {
                        openFragment(new FiltersFragment());

                    } else if (item.getItemId() == R.id.itemChats) {
                        openFragment(new ChatsFragment());

                    } else if (item.getItemId() == R.id.itemPerfil) {
                        openFragment(new ProfileFragment());

                    }
                    return true;//aca deber ser true no false para que me muestre los nombres de los fragments
                }

            };

}