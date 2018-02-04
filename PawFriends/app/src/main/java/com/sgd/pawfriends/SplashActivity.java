package com.sgd.pawfriends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Daza on 20/03/2017.
 * First Activity represent Splash screen before truly start the app
 */



public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean first_launch = pref.getBoolean(
                getString(R.string.pref_first_launch), Boolean.parseBoolean(
                        getString(R.string.pref_first_launch_default)));

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (first_launch) {
            pref.edit().putBoolean(getString(R.string.pref_first_launch_anim1), true).apply();
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        } else if (!first_launch && user != null) {
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
        } else if (!first_launch && user == null) {
            Intent in = new Intent(this, LoginActivity.class);
            startActivity(in);
        }
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        //Cuando la clase empieza a "escuchar"
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //Cuando la clase deja de "escuchar"
        if (firebaseAuth != null)
            firebaseAuth.removeAuthStateListener(authStateListener);
    }
}