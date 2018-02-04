package com.sgd.pawfriends;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.sgd.pawfriends.custom.Utilities;
import com.sgd.pawfriends.fragments.StartScreen1Fragment;
import com.sgd.pawfriends.fragments.StartScreen2Fragment;
import com.sgd.pawfriends.fragments.StartScreen3Fragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.ButterKnife;

/**
 *  Created by DAZA
 *  First launch activity for explain app's purpose and login
 */

public class StartActivity extends AppCompatActivity {

    private static final String LOG_TAG = StartActivity.class.getSimpleName();

    private SectionsPagerAdapter mSectionsPagerAdapter;
    protected ViewPager mViewPager;
    private boolean accept_terms = false;
    private static final int REQUEST_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_launch_activity);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //     if(!Utilities.checkPermission(this,android.Manifest.permission.CAMERA)) {
        Utilities.requestPermission(this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        //    }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "co.com.sgd.pawfriends",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.v("KeyHashFB:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    /**
    public void setmViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }
     **/

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return StartScreen1Fragment.newInstance();
            } else if (position == 1) {
                return StartScreen2Fragment.newInstance();
            } else {
                return StartScreen3Fragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Utilities.showToastShort(R.string.permission_denied, this);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG_TAG, "Permission is granted1");
                return true;
            } else {

                Log.v(LOG_TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOG_TAG, "Permission is granted1");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG_TAG, "Permission is granted2");
                return true;
            } else {

                Log.v(LOG_TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOG_TAG, "Permission is granted2");
            return true;
        }
    }
}
