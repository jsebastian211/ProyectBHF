package com.sgd.pawfriends;

import android.*;
import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


/**
 * Created by Daza on 27/03/2017.
 */

public class PawFriendsApp extends Application {

    private static PawFriendsApp singletonApp;
    private FirebaseDatabase mDatabase;
    private FirebaseFirestore mFirestoreDatabase;
    public boolean isDevelopment = true;
    public FirebaseUser user;


    public static PawFriendsApp getInstance(){
        return singletonApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singletonApp = this;
        //Firebase realtime database
        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);
        //Firestore
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestoreDatabase = FirebaseFirestore.getInstance();
        mFirestoreDatabase.setFirestoreSettings(settings);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


    }

    public FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public FirebaseFirestore getFirestoreDatabase() {
        if (mFirestoreDatabase == null) {
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            mFirestoreDatabase = FirebaseFirestore.getInstance();
            mFirestoreDatabase.setFirestoreSettings(settings);
        }
        return mFirestoreDatabase;
    }


}
