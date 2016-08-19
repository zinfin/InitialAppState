package com.bignerdranch.android.animals.controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.bignerdranch.android.animals.R;
import com.bignerdranch.android.animals.controller.AnimalFragment;

public class AnimalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        if (savedInstanceState == null) {
            AnimalFragment animalFragment = new AnimalFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, animalFragment)
                    .commit();
        }
    }
}
