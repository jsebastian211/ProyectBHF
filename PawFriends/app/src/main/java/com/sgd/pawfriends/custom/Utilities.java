package com.sgd.pawfriends.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.sgd.pawfriends.LoginActivity;
import com.sgd.pawfriends.MainActivity;
import com.sgd.pawfriends.PawFriendsApp;
import com.sgd.pawfriends.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Daza on 04/04/2017.
 */

public class Utilities {

    public static final String LOG_TAG = Utilities.class.getSimpleName();


    public static boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,15}$");
        return pattern.matcher(password).matches();
    }

    public static boolean validateUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    public static boolean reConfirmPassword(String password, String password1) {
        return password.equals(password1);
    }

    public static void showToastLong(int message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(int message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void exceptionFirebaseAdministrator(Context context, Task<AuthResult> task, String LOG_TAG) {
        try {
            throw task.getException();
        } catch (FirebaseAuthWeakPasswordException e) {
            showToastLong(R.string.error_firebase_weak_pass, context);
            Log.e(LOG_TAG, e.getMessage());
        } catch (FirebaseAuthUserCollisionException e) {
            showToastLong(R.string.error_firebase_exists_account, context);
            Log.e(LOG_TAG, e.getMessage());
        } catch (FirebaseAuthInvalidCredentialsException e) {
            showToastLong(R.string.error_firebase_email_pass_invalid, context);
            Log.e(LOG_TAG, e.getMessage());
        } catch (FirebaseAuthInvalidUserException e) {
            showToastLong(R.string.error_firebase_email_invalid, context);
            Log.e(LOG_TAG, e.getMessage());
        } catch (Exception e) {
            showToastLong(R.string.error_firebase_log, context);
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public static void exceptionFirebaseAdministratorForCreateAccount(Context context, Task<AuthResult> task, String LOG_TAG) {
        try {
            throw task.getException();
        } catch (FirebaseAuthWeakPasswordException e) {
            showToastLong(R.string.error_firebase_weak_pass, context);
            Log.e(LOG_TAG, e.getMessage());
        } catch (FirebaseAuthUserCollisionException e) {
            showToastLong(R.string.error_firebase_exists_account, context);
            Log.e(LOG_TAG, e.getMessage());
        } catch (FirebaseAuthInvalidCredentialsException e) {
            showToastLong(R.string.error_firebase_email_pass_invalid, context);
            Log.e(LOG_TAG, e.getMessage());
        } catch (FirebaseAuthInvalidUserException e) {
            showToastLong(R.string.error_firebase_email_invalid, context);
            Log.e(LOG_TAG, e.getMessage());
        } catch (Exception e) {
            showToastLong(R.string.error_firebase_register, context);
            Log.e(LOG_TAG, e.getMessage());
        }
    }


    public static String validateRegisterMethod(FirebaseUser user) {
        String info = user.getProviders().size() > 0 ? user.getProviders().get(0) : "anonymous";

        if (info.equals("facebook.com")) {
            return PawFriendsConstants.FACEBOOK_LOGIN;
        } else if (info.equals("password")) {
            return PawFriendsConstants.EMAIL_LOGIN;
        }

        return PawFriendsConstants.ANONYMOUS_LOGIN;
    }


    public static void startMainActivity(Context context) {
        Intent in = new Intent(context, MainActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(in);
    }

    public static void startLoginActivity(Context context) {
        Intent in = new Intent(context, LoginActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(in);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void sendResetPassEmail(final Context context, String email, FirebaseAuth auth) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showToastLong(R.string.res_password_confirm_email_send, context);
                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                showToastLong(R.string.res_password_confirm_email_send_error, context);
                                Log.e(LOG_TAG, e.getMessage());
                            }
                        }
                    }
                });
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static boolean getBooleanPreference(Context context, SharedPreferences pref, int key, int defaul_key) {
        boolean sharedPreference = pref.getBoolean(
                context.getString(key), Boolean.parseBoolean(
                        context.getString(defaul_key)));

        return sharedPreference;
    }

    public static PawFriendsApp getPawFriendsApp(Context context) {
        return (PawFriendsApp) context.getApplicationContext();
    }

    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static String concatTime(String name) {
        return name.concat(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
    }

    public static void requestPermission(Activity activity, String[] permission, int code) {

        ActivityCompat.requestPermissions(activity,
                permission,
                code);

    }


}
