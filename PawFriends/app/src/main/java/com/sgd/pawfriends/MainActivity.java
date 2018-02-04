package com.sgd.pawfriends;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sgd.pawfriends.custom.Utilities;
import com.sgd.pawfriends.fragments.PrincipalFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean mDoubleBackToExitPressedOnce = false;
    @BindView(R.id.bnvMenu)
    BottomNavigationView mBnvMenu;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        mFragment = new PrincipalFragment();
        final FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        transaction.replace(R.id.main_container, mFragment).commit();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                boolean anonymous_login =
                        Utilities.getBooleanPreference(getApplicationContext(), mPref, R.string.pref_logged_with_anonymous,
                                R.string.pref_logged_with_anonymous_default);

                // if(anonymous_login) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                validateAndGetUserData(user);
                //}
            }
        };

        mBnvMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.bnv_op_main:
                        mFragment = new PrincipalFragment();
                        break;
                }
                final FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, mFragment).commit();
                return true;
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    private void validateAndGetUserData(FirebaseUser user) {
        if (user == null) {
            goLoginActivity();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mDoubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            validateDoubleTapExit();
        }
    }

    private void validateDoubleTapExit() {
        this.mDoubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getText(R.string.confirm_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mDoubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(View view) {

        boolean facebook_login = Utilities.getBooleanPreference(getApplicationContext(), mPref,
                R.string.pref_logged_with_facebook, R.string.pref_logged_with_facebook_default);

        boolean anonymous_login =
                Utilities.getBooleanPreference(getApplicationContext(), mPref, R.string.pref_logged_with_anonymous,
                        R.string.pref_logged_with_anonymous_default);

        // if(!anonymous_login) {
        if (facebook_login) {
            LoginManager.getInstance().logOut();
        }

        // BdUserUtilities.removeUserListener(mFirebaseAuth.getCurrentUser());
        FirebaseAuth.getInstance().signOut();
        mPref.edit().putBoolean(getString(R.string.pref_logged_with_facebook), false).apply();
        mPref.edit().putBoolean(getString(R.string.pref_logged_with_firebase), false).apply();
        mPref.edit().putBoolean(getString(R.string.pref_logged_with_anonymous), false).apply();
        // }

        goLoginActivity();
    }

    private void goLoginActivity() {
        Utilities.startLoginActivity(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Cuando la clase empieza a "escuchar"
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //Cuando la clase deja de "escuchar"
        if (mFirebaseAuth != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

}
