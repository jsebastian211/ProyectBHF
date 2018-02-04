package com.sgd.pawfriends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sgd.pawfriends.fragments.PetsFragment;

public class PetsActivity extends AppCompatActivity {

    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getSupportFragmentManager();
        mFragment = new PetsFragment();
        final FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.main_container_pet, mFragment).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
