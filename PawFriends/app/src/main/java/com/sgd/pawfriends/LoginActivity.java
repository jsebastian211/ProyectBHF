package com.sgd.pawfriends;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sgd.pawfriends.bd.BdUserUtilities;
import com.sgd.pawfriends.custom.PawFriendsConstants;
import com.sgd.pawfriends.custom.Utilities;
import com.sgd.pawfriends.entities.NdUser;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.util.Arrays;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by DAZA
 * Activity for login functions
 */

public class LoginActivity extends Activity implements View.OnClickListener {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.login_image)
    ImageView mImageView;
    @BindView(R.id.btn_enter_no_login)
    TextView mEnterNoLogin;
    @BindView(R.id.btn_sign_in)
    Button mBtnSignIn;
    @BindView(R.id.btn_log_in)
    Button mBtnLogIn;
    @BindView(R.id.confirmPasswordText)
    TextInputLayout mTxtConfirmPass;
    @BindView(R.id.passwordText)
    TextInputLayout mTxtPassword;
    @BindView(R.id.emailText)
    TextInputLayout mTxtEmail;
    @BindView(R.id.btnRegister)
    Button mBtnRegister;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.relativeLayout1)
    RelativeLayout mRelativeLayout1;
    @BindView(R.id.txtResult)
    TextView mTxtResult;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;
    @BindView(R.id.ch_accept_term)
    CheckBox mChAcceptTerm;
    @BindView(R.id.link_terms)
    TextView mTxtLinkTerms;
    @BindView(R.id.link_terms1)
    TextView mTxtLinkTerms1;
    @BindView(R.id.btnEmail)
    Button mBtnEmail;
    @BindView(R.id.first_login_layout)
    RelativeLayout mFirstLoginLayout;
    @BindView(R.id.login_layout)
    ConstraintLayout mLoginLayout;
    @BindView(R.id.txt_login_step)
    TextView mTxtLoginStep;
    @BindView(R.id.ch_accept_term1)
    CheckBox mChAcceptTerm1;
    @BindView(R.id.btnBack)
    Button mBtnBack;
    @BindView(R.id.layout_loading)
    RelativeLayout mLayoutLoading;
    @BindView(R.id.txt_restore_resend_title)
    TextView mTxtRestoreResendTitle;
    @BindView(R.id.txt_restore_resend_description)
    TextView mTxtRestoreResendDescription;


    @BindView(R.id.btn_res_password_email_step)
    TextView mBtnRestorePassResend;
    @BindView(R.id.btnConfirmResetResend)
    Button mBtnConfirmResetResend;
    @BindView(R.id.btnBackEmailLogin)
    Button mBtnBackEmailLogin;
    @BindView(R.id.txtEmailResPass)
    TextInputLayout mTxtEmailResPass;
    @BindView(R.id.restore_pass_resend_layout)
    RelativeLayout mRestorePassResendLayout;

    /**
     * Componentes para el login de face
     **/
    @BindView(R.id.fb_login_button)
    LoginButton mFbLoginButton;
    //Variable para detectar las acciones o eventos del boton mFbLoginButton
    private CallbackManager mCallbackManager;
    /**
     * Componentes para el registro a Fi0rebase
     **/
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFireAuthStateListener;
    private SharedPreferences mPref;
    //false restore pass
    //true resend email
    private Boolean mResendRestoreOption = false;
    //Var to storage user and if is necessary resend verification email
    private FirebaseUser mEmailUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawable(null);
        ButterKnife.bind(this);
        Glide.with(getApplicationContext()).load(R.drawable.start_screen_3).centerCrop().into(mImageView);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mTxtLinkTerms.setMovementMethod(LinkMovementMethod.getInstance());
        mTxtLinkTerms1.setMovementMethod(LinkMovementMethod.getInstance());
        mEnterNoLogin.setOnClickListener(this);
        mBtnLogIn.setOnClickListener(this);
        mBtnSignIn.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mBtnEnter.setOnClickListener(this);
        mBtnEmail.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
        mBtnRestorePassResend.setOnClickListener(this);
        mBtnConfirmResetResend.setOnClickListener(this);
        mBtnBackEmailLogin.setOnClickListener(this);

        /*********************Administracion login de Facebook*********************************/
        mCallbackManager = CallbackManager.Factory.create();
        mFbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        mFbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Inicio de sesion con facebook exitoso
                if (mChAcceptTerm1.isChecked()) {
                    setUpFirstLoginUI(1);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                } else {
                    Utilities.showToastLong(R.string.terms_policies_confirmation, LoginActivity.this);
                    LoginManager.getInstance().logOut();
                }
            }

            @Override
            public void onCancel() {
                //Inicio de sesion con facebook cancelado
                Utilities.showToastShort(R.string.cancel_fb_log, LoginActivity.this);
            }

            @Override
            public void onError(FacebookException error) {
                //Inicio de sesion con facebook erroneo
                if (!Utilities.isNetworkAvailable(LoginActivity.this)) {
                    Utilities.showToastLong(R.string.string_no_network, LoginActivity.this);
                } else {
                    Utilities.showToastShort(R.string.error_fb_log, LoginActivity.this);
                    Log.e(LOG_TAG, error.getMessage());
                }
            }
        });
        /**************************************************************************************/
        /*********************Administracion login de Firebase*********************************/
        mFirebaseAuth = FirebaseAuth.getInstance();
        //Clase oyente para notificar cuando tengamos el usuario al iniciar la sesion
        mFireAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //BdUserUtilities.getUserByUID(user.getUid());
                    String method_login = Utilities.validateRegisterMethod(user);
                    if (method_login.equals(PawFriendsConstants.FACEBOOK_LOGIN)) {
                        mPref.edit().putBoolean(getString(R.string.pref_logged_with_facebook), true).apply();
                        mPref.edit().putBoolean(getString(R.string.pref_logged_with_firebase), false).apply();
                        mPref.edit().putBoolean(getString(R.string.pref_logged_with_anonymous), false).apply();
                        startMainActivity();
                        BdUserUtilities.createUser(new NdUser(user.getUid(), user.getEmail(), method_login, null, new Date().getTime(), user.getDisplayName(), true));
                    } else if (method_login.equals(PawFriendsConstants.EMAIL_LOGIN)) {
                        mPref.edit().putBoolean(getString(R.string.pref_logged_with_facebook), false).apply();
                        mPref.edit().putBoolean(getString(R.string.pref_logged_with_firebase), true).apply();
                        mPref.edit().putBoolean(getString(R.string.pref_logged_with_anonymous), false).apply();
                    } else if (method_login.equals(PawFriendsConstants.ANONYMOUS_LOGIN)) {
                        mPref.edit().putBoolean(getString(R.string.pref_logged_with_facebook), false).apply();
                        mPref.edit().putBoolean(getString(R.string.pref_logged_with_firebase), false).apply();
                        mPref.edit().putBoolean(getString(R.string.pref_logged_with_anonymous), true).apply();
                        startMainActivity();
                    }
                }

            }
        };

        /**************************************************************************************/
    }

    /**
     * Setup UI when is trying login
     *
     * @param type
     */

    private void setUpUiRev(int type) {
        if (type == 1) {
            mRelativeLayout.setVisibility(View.VISIBLE);
            mRelativeLayout1.setVisibility(View.GONE);
        } else if (type == 2) {
            mRelativeLayout.setVisibility(View.VISIBLE);
            mRelativeLayout1.setVisibility(View.GONE);
            mBtnLogIn.setVisibility(View.VISIBLE);
            mBtnSignIn.setVisibility(View.VISIBLE);
            mEnterNoLogin.setVisibility(View.VISIBLE);

        } else if (type == 3) {
            mProgressBar.setVisibility(View.GONE);
            mBtnEnter.setVisibility(View.VISIBLE);
            mTxtResult.setVisibility(View.VISIBLE);
            mBtnLogIn.setVisibility(View.GONE);
            mBtnSignIn.setVisibility(View.GONE);
        } else if (type == 4) {
            mTxtPassword.setErrorEnabled(false);
            mTxtPassword.setError(null);
            mTxtConfirmPass.setErrorEnabled(false);
            mTxtConfirmPass.setError(null);
        } else if (type == 5) {
            mTxtConfirmPass.setErrorEnabled(false);
            mTxtConfirmPass.setError(null);
        } else if (type == 6) {
            mRelativeLayout.setVisibility(View.VISIBLE);
            mRelativeLayout1.setVisibility(View.GONE);
            mEnterNoLogin.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mBtnEnter.setVisibility(View.GONE);
            mTxtResult.setVisibility(View.GONE);
            mBtnLogIn.setVisibility(View.VISIBLE);
            mBtnSignIn.setVisibility(View.VISIBLE);
        } else if (type == 7) {
            mLayoutLoading.setVisibility(View.GONE);
            mFirstLoginLayout.setVisibility(View.VISIBLE);
        } else if (type == 8) {
            mRelativeLayout.setVisibility(View.VISIBLE);
            mRelativeLayout1.setVisibility(View.GONE);
            mBtnLogIn.setVisibility(View.VISIBLE);
            mBtnSignIn.setVisibility(View.VISIBLE);
            mEnterNoLogin.setVisibility(View.VISIBLE);
            TransitionManager.beginDelayedTransition(mRelativeLayout,
                    new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
            mBtnRestorePassResend.setText(R.string.resend_email);
            mResendRestoreOption = true;
            mTxtRestoreResendTitle.setText(R.string.resend_email);
            mTxtRestoreResendDescription.setText(R.string.res_email_description);
            mBtnConfirmResetResend.setText(R.string.res_email_option);
            mTxtEmailResPass.getEditText().setText(mEmailUser.getEmail());
            mTxtEmailResPass.getEditText().setEnabled(false);
        }
    }

    /**
     * UI setup when is validate login
     */

    private void setUpFirstLoginUI(int i) {
        if (i == 1) {
            mLayoutLoading.setVisibility(View.VISIBLE);
            mFirstLoginLayout.setVisibility(View.GONE);
        } else if (i == 2) {
            mRelativeLayout.setVisibility(View.GONE);
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mEnterNoLogin.setVisibility(View.GONE);
            mBtnLogIn.setVisibility(View.GONE);
            mBtnSignIn.setVisibility(View.GONE);
        }
    }

    /**
     * Validate data from email password and confirm password's InputText
     */

    private void validateData() {
        String name = mTxtEmail.getEditText().getText().toString().trim();
        mTxtEmail.getEditText().setText(name);
        String password = mTxtPassword.getEditText().getText().toString();
        String confirm_password = mTxtConfirmPass.getEditText().getText().toString();

        if (validateEmail(name, mTxtEmail)) {
            if (validatePassword(password)) {
                if (matchPassword(password, confirm_password)) {
                    handleFirebaseLoginOrRegister(name, password);
                }
            } else {
                setUpUiRev(5);
            }
        } else {
            setUpUiRev(4);
        }
    }

    /**
     * Validate if the password and confirm password field's text are equal
     *
     * @param password
     * @param confirm_password
     * @return
     */
    private boolean matchPassword(String password, String confirm_password) {
        if (mTxtConfirmPass.getVisibility() == View.VISIBLE) {
            boolean matchPassword = Utilities.reConfirmPassword(password, confirm_password);
            if (!matchPassword) {
                mTxtConfirmPass.setErrorEnabled(true);
                mTxtConfirmPass.setError(getString(R.string.password_not_equals));
                return false;
            } else {
                mTxtConfirmPass.setErrorEnabled(false);
                mTxtConfirmPass.setError(null);
                return true;
            }
        } else {
            return true;
        }

    }

    /**
     * Validate the structure of the email is correct
     *
     * @param name
     * @param input
     * @return
     */
    private boolean validateEmail(String name, TextInputLayout input) {
        boolean validEmail = Utilities.validateEmail(name);
        if (!validEmail) {
            input.setErrorEnabled(true);
            input.setError(getString(R.string.email_no_valid));
            return false;
        } else {
            input.setErrorEnabled(false);
            input.setError(null);
            return true;
        }
    }

    /**
     * Validate if the structure of the password is correct
     *
     * @param password
     * @return
     */
    private boolean validatePassword(String password) {
        boolean validPassword = Utilities.validatePassword(password);
        if (!validPassword) {
            mTxtPassword.setErrorEnabled(true);
            mTxtPassword.setError(getString(R.string.password_no_valid));
            return false;
        } else {
            mTxtPassword.setErrorEnabled(false);
            mTxtPassword.setError(null);
            return true;
        }
    }

    /**
     * Manage the facebook's token to register into firebase
     *
     * @param accessToken
     */
    private void handleFacebookAccessToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        //Iniciar sesión con una credencial
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            //metodo que se ejecuta al terminar el proceso
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Utilities.exceptionFirebaseAdministrator(LoginActivity.this, task, LOG_TAG);
                    LoginManager.getInstance().logOut();
                    setUpUiRev(7);
                }
            }
        });
    }

    /**
     * Login anonymously
     */

    private void handleAnonymousLogIn() {
        mFirebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Utilities.exceptionFirebaseAdministrator(LoginActivity.this, task, LOG_TAG);
                            setUpUiRev(7);
                        }
                    }
                });
    }

    private void startMainActivity() {
        mPref.edit().putBoolean(getString(R.string.pref_first_launch), false).apply();
        Utilities.startMainActivity(this);
    }

    /**
     * Handle intent result of facebook and google login activities
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Validate if is new user or  old user trying enter
     *
     * @param user
     * @param pass
     */

    private void handleFirebaseLoginOrRegister(String user, String pass) {
        if (mChAcceptTerm.isChecked()) {
            setUpFirstLoginUI(2);
            if (mTxtConfirmPass.getVisibility() == View.VISIBLE) {
                createFirebaseUser(user, pass);
            } else {
                signInFirebase(user, pass);
            }
        } else {
            Utilities.showToastLong(R.string.terms_policies_confirmation, this);
        }
    }

    /**
     * Login user with firebase
     *
     * @param user
     * @param pass
     */

    private void signInFirebase(String user, String pass) {
        mFirebaseAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Utilities.exceptionFirebaseAdministratorForCreateAccount(LoginActivity.this, task, LOG_TAG);
                            setUpUiRev(2);
                        } else {
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                startMainActivity();
                                BdUserUtilities.updateUniqueField(user.getUid(), NdUser.getName(NdUser.LAST_LOGIN), new Date().getTime());
                            } else {
                                Utilities.showToastLong(R.string.firebase_email_send_wait, getApplication());
                                mEmailUser = user;
                                setUpUiRev(8);
                            }
                        }
                    }
                });
    }

    /**
     * Create new user using firebase
     *
     * @param user
     * @param pass
     */

    private void createFirebaseUser(String user, String pass) {
        mFirebaseAuth.createUserWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Utilities.exceptionFirebaseAdministratorForCreateAccount(LoginActivity.this, task, LOG_TAG);
                            setUpUiRev(2);
                        } else {
                            final FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            BdUserUtilities.createUser(
                                    new NdUser(user.getUid(), user.getEmail(), PawFriendsConstants.EMAIL_LOGIN, false, new Date().getTime(), user.getDisplayName(), true));
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                setUpUiRev(3);
                                                BdUserUtilities.updateUniqueField(user.getUid(), NdUser.getName(NdUser.EMAIL_SENDED), true);
                                            } else {
                                                try {
                                                    throw task.getException();
                                                } catch (Exception e) {
                                                    setUpUiRev(3);
                                                    Utilities.showToastLong(R.string.firebase_email_send_error, getApplication());
                                                    Log.e(LOG_TAG, e.getMessage());
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        //Cuando la clase empieza a "escuchar"
        mFirebaseAuth.addAuthStateListener(mFireAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //Cuando la clase deja de "escuchar"
        if (mFirebaseAuth != null)
            mFirebaseAuth.removeAuthStateListener(mFireAuthStateListener);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_enter_no_login) {
            //startMainActivity();
            if (!Utilities.isNetworkAvailable(this)) {
                Utilities.showToastLong(R.string.string_no_network, this);
            } else {
                if (mChAcceptTerm1.isChecked()) {
                    setUpFirstLoginUI(1);
                    handleAnonymousLogIn();
                } else {
                    Utilities.showToastLong(R.string.terms_policies_confirmation, this);
                }
            }
        } else if (i == R.id.btn_log_in) {
            if (mTxtConfirmPass.getVisibility() == View.VISIBLE) {
                mTxtConfirmPass.setVisibility(View.GONE);
            }
        } else if (i == R.id.btn_sign_in) {
            if (mTxtConfirmPass.getVisibility() == View.GONE) {
                mTxtConfirmPass.setVisibility(View.VISIBLE);
            }
        } else if (i == R.id.btnRegister) {
            if (!Utilities.isNetworkAvailable(this)) {
                Utilities.showToastLong(R.string.string_no_network, this);
            } else {
                validateData();
            }
        } else if (i == R.id.btn_enter) {
            if (mTxtConfirmPass.getVisibility() == View.VISIBLE) {
                mTxtConfirmPass.setVisibility(View.GONE);
            }
            setUpUiRev(6);
            //FirebaseAuth.getInstance().signOut();
            //BdUserUtilities.removeUserListener(mFirebaseAuth.getCurrentUser());
            mTxtConfirmPass.getEditText().setText(null);
            mTxtPassword.getEditText().setText(null);
        } else if (i == R.id.btnEmail) {
            mChAcceptTerm.setChecked(mChAcceptTerm1.isChecked());
            mFirstLoginLayout.setVisibility(View.GONE);
            mLoginLayout.setVisibility(View.VISIBLE);
        } else if (i == R.id.btnBack) {
            mChAcceptTerm1.setChecked(mChAcceptTerm.isChecked());
            mLoginLayout.setVisibility(View.GONE);
            mFirstLoginLayout.setVisibility(View.VISIBLE);
        } else if (i == R.id.btn_res_password_email_step) {
            mLoginLayout.setVisibility(View.GONE);
            mRestorePassResendLayout.setVisibility(View.VISIBLE);
        } else if (i == R.id.btnBackEmailLogin) {
            mRestorePassResendLayout.setVisibility(View.GONE);
            mLoginLayout.setVisibility(View.VISIBLE);
            if (mResendRestoreOption) {
                TransitionManager.beginDelayedTransition(mRelativeLayout,
                        new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
                mBtnRestorePassResend.setText(R.string.res_password_option);
                mResendRestoreOption = false;
                mTxtRestoreResendTitle.setText(R.string.res_password_option);
                mTxtRestoreResendDescription.setText(R.string.res_password_description);
                mBtnConfirmResetResend.setText(R.string.res_password_confirm);
                mEmailUser = null;
                mTxtEmailResPass.getEditText().setEnabled(true);
                mTxtEmailResPass.getEditText().setText("");
            }
        } else if (i == R.id.btnConfirmResetResend) {
            String email = mTxtEmailResPass.getEditText().getText().toString().trim();
            mTxtEmailResPass.getEditText().setText(email);
            if (validateEmail(email, mTxtEmailResPass)) {
                if (!mResendRestoreOption) {
                    Utilities.sendResetPassEmail(this, email, mFirebaseAuth);
                } else {
                    setUpFirstLoginUI(2);
                    if (!mEmailUser.isEmailVerified()) {
                        mEmailUser.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            setUpUiRev(3);
                                        } else {
                                            try {
                                                throw task.getException();
                                            } catch (Exception e) {
                                                setUpUiRev(3);
                                                Utilities.showToastLong(R.string.firebase_email_send_error, getApplication());
                                                Log.e(LOG_TAG, e.getMessage());
                                            }
                                        }
                                        TransitionManager.beginDelayedTransition(mRelativeLayout,
                                                new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
                                        mBtnRestorePassResend.setText(R.string.res_password_option);
                                        mResendRestoreOption = false;
                                        mTxtRestoreResendTitle.setText(R.string.res_password_option);
                                        mTxtRestoreResendDescription.setText(R.string.res_password_description);
                                        mBtnConfirmResetResend.setText(R.string.res_password_confirm);
                                        mEmailUser = null;
                                        mTxtEmailResPass.getEditText().setEnabled(true);
                                        mTxtEmailResPass.getEditText().setText("");
                                    }
                                });
                    }
                }
                mTxtEmailResPass.getEditText().setText("");
                mRestorePassResendLayout.setVisibility(View.GONE);
                mLoginLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}