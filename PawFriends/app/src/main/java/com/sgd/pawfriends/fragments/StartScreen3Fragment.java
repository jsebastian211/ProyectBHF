package com.sgd.pawfriends.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sgd.pawfriends.R;
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
 * Created by Daza on 26/08/2017.
 */

public class StartScreen3Fragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = StartScreen3Fragment.class.getSimpleName();
    @BindView(R.id.login_image)
    ImageView imageView;
    @BindView(R.id.btn_enter_no_login)
    TextView enterNoLogin;
    @BindView(R.id.btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.btn_log_in)
    Button btnLogIn;
    @BindView(R.id.confirmPasswordText)
    TextInputLayout txtConfirmPass;
    @BindView(R.id.passwordText)
    TextInputLayout txtPassword;
    @BindView(R.id.emailText)
    TextInputLayout txtEmail;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.relativeLayout1)
    RelativeLayout relativeLayout1;
    @BindView(R.id.txtResult)
    TextView txtResult;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    @BindView(R.id.ch_accept_term)
    CheckBox chAcceptTerm;
    @BindView(R.id.link_terms)
    TextView txtLinkTerms;
    @BindView(R.id.link_terms1)
    TextView txtLinkTerms1;
    @BindView(R.id.btnEmail)
    Button btnEmail;
    @BindView(R.id.first_login_layout)
    RelativeLayout firstLoginLayout;
    @BindView(R.id.login_layout)
    ConstraintLayout loginLayout;
    @BindView(R.id.txt_login_step)
    TextView txtLoginStep;
    @BindView(R.id.ch_accept_term1)
    CheckBox chAcceptTerm1;
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.layout_loading)
    RelativeLayout layoutLoading;
    @BindView(R.id.txt_restore_resend_title)
    TextView txtRestoreResendTitle;
    @BindView(R.id.txt_restore_resend_description)
    TextView txtRestoreResendDescription;


    @BindView(R.id.btn_res_password_email_step)
    TextView btnRestorePassResend;
    @BindView(R.id.btnConfirmResetResend)
    Button btnConfirmResetResend;
    @BindView(R.id.btnBackEmailLogin)
    Button btnBackEmailLogin;
    @BindView(R.id.txtEmailResPass)
    TextInputLayout txtEmailResPass;
    @BindView(R.id.restore_pass_resend_layout)
    RelativeLayout restorePassResendLayout;

    /**
     * Componentes para el login de face
     **/
    @BindView(R.id.fb_login_button)
    LoginButton fbLoginButton;
    //Variable para detectar las acciones o eventos del boton fbLoginButton
    private CallbackManager callbackManager;
    /**
     * Componentes para el registro a Fi0rebase
     **/
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;
    private SharedPreferences pref;
    //false restore pass
    //true resend email
    private Boolean resendRestoreOption = false;
    //Var to storage user and if is necessary resend verification email
    private FirebaseUser emailUser;


    public StartScreen3Fragment() {
    }

    public static StartScreen3Fragment newInstance() {
        StartScreen3Fragment fragment = new StartScreen3Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_start_3, container, false);
        ButterKnife.bind(this, rootView);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Glide.with(this.getContext()).load(R.drawable.start_screen_3).centerCrop().into(imageView);
        txtLinkTerms.setMovementMethod(LinkMovementMethod.getInstance());
        txtLinkTerms1.setMovementMethod(LinkMovementMethod.getInstance());
        enterNoLogin.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
        btnEmail.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        txtLoginStep.setText(R.string.log_in_step);
        btnRestorePassResend.setOnClickListener(this);
        btnConfirmResetResend.setOnClickListener(this);
        btnBackEmailLogin.setOnClickListener(this);


        /*********************Administracion login de Facebook*********************************/
        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.setFragment(this);
        fbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Inicio de sesion con facebook exitoso
                if (chAcceptTerm1.isChecked()) {
                    setUpFirstLoginUI(1);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                } else {
                    Utilities.showToastLong(R.string.terms_policies_confirmation, getContext());
                    LoginManager.getInstance().logOut();
                }
            }

            @Override
            public void onCancel() {
                //Inicio de sesion con facebook cancelado
                Utilities.showToastShort(R.string.cancel_fb_log, getContext());
            }

            @Override
            public void onError(FacebookException error) {
                //Inicio de sesion con facebook erroneo
                if (!Utilities.isNetworkAvailable(getContext())) {
                    Utilities.showToastLong(R.string.string_no_network, getContext());
                } else {
                    Utilities.showToastShort(R.string.error_fb_log, getContext());
                    Log.e(LOG_TAG, error.getMessage());
                }
            }
        });
        /**************************************************************************************/
        /*********************Administracion login de Firebase*********************************/
        firebaseAuth = FirebaseAuth.getInstance();
        //Clase oyente para notificar cuando tengamos el usuario al iniciar la sesion
        fireAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //BdUserUtilities.getUserByUID(user.getUid());
                    String method_login = Utilities.validateRegisterMethod(user);
                    if (method_login.equals(PawFriendsConstants.FACEBOOK_LOGIN)) {
                        pref.edit().putBoolean(getString(R.string.pref_logged_with_facebook), true).apply();
                        pref.edit().putBoolean(getString(R.string.pref_logged_with_firebase), false).apply();
                        pref.edit().putBoolean(getString(R.string.pref_logged_with_anonymous), false).apply();
                        startMainActivity();
                        BdUserUtilities.createUser(new NdUser(user.getUid(), user.getEmail(), method_login, null, new Date().getTime(), user.getDisplayName(), true));
                    } else if (method_login.equals(PawFriendsConstants.EMAIL_LOGIN)) {
                        pref.edit().putBoolean(getString(R.string.pref_logged_with_facebook), false).apply();
                        pref.edit().putBoolean(getString(R.string.pref_logged_with_firebase), true).apply();
                        pref.edit().putBoolean(getString(R.string.pref_logged_with_anonymous), false).apply();
                    } else if (method_login.equals(PawFriendsConstants.ANONYMOUS_LOGIN)) {
                        pref.edit().putBoolean(getString(R.string.pref_logged_with_facebook), false).apply();
                        pref.edit().putBoolean(getString(R.string.pref_logged_with_firebase), false).apply();
                        pref.edit().putBoolean(getString(R.string.pref_logged_with_anonymous), true).apply();
                        startMainActivity();
                    }
                }

            }
        };

        /**************************************************************************************/
        return rootView;
    }

    /**
     * Setup UI when is trying login
     *
     * @param type
     */

    private void setUpUiRev(int type) {
        if (type == 1) {
            relativeLayout.setVisibility(View.VISIBLE);
            relativeLayout1.setVisibility(View.GONE);
        } else if (type == 2) {
            relativeLayout.setVisibility(View.VISIBLE);
            relativeLayout1.setVisibility(View.GONE);
            btnLogIn.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
            enterNoLogin.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            progressBar.setVisibility(View.GONE);
            btnEnter.setVisibility(View.VISIBLE);
            txtResult.setVisibility(View.VISIBLE);
            btnLogIn.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.GONE);
        } else if (type == 4) {
            txtPassword.setErrorEnabled(false);
            txtPassword.setError(null);
            txtConfirmPass.setErrorEnabled(false);
            txtConfirmPass.setError(null);
        } else if (type == 5) {
            txtConfirmPass.setErrorEnabled(false);
            txtConfirmPass.setError(null);
        } else if (type == 6) {
            relativeLayout.setVisibility(View.VISIBLE);
            relativeLayout1.setVisibility(View.GONE);
            enterNoLogin.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            btnEnter.setVisibility(View.GONE);
            txtResult.setVisibility(View.GONE);
            btnLogIn.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
        } else if (type == 7) {
            layoutLoading.setVisibility(View.GONE);
            firstLoginLayout.setVisibility(View.VISIBLE);
        } else if (type == 8) {
            relativeLayout.setVisibility(View.VISIBLE);
            relativeLayout1.setVisibility(View.GONE);
            btnLogIn.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
            enterNoLogin.setVisibility(View.VISIBLE);
            TransitionManager.beginDelayedTransition(relativeLayout,
                    new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
            btnRestorePassResend.setText(R.string.resend_email);
            resendRestoreOption = true;
            txtRestoreResendTitle.setText(R.string.resend_email);
            txtRestoreResendDescription.setText(R.string.res_email_description);
            btnConfirmResetResend.setText(R.string.res_email_option);
            txtEmailResPass.getEditText().setText(emailUser.getEmail());
            txtEmailResPass.getEditText().setEnabled(false);
        }
    }

    /**
     * UI setup when is validate login
     */

    private void setUpFirstLoginUI(int i) {
        if (i == 1) {
            layoutLoading.setVisibility(View.VISIBLE);
            firstLoginLayout.setVisibility(View.GONE);
        } else if (i == 2) {
            relativeLayout.setVisibility(View.GONE);
            relativeLayout1.setVisibility(View.VISIBLE);
            enterNoLogin.setVisibility(View.GONE);
            btnLogIn.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.GONE);
        }
    }

    /**
     * Validate data from email password and confirm password's InputText
     */

    private void validateData() {
        String name = txtEmail.getEditText().getText().toString().trim();
        txtEmail.getEditText().setText(name);
        String password = txtPassword.getEditText().getText().toString();
        String confirm_password = txtConfirmPass.getEditText().getText().toString();

        if (validateEmail(name, txtEmail)) {
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
        if (txtConfirmPass.getVisibility() == View.VISIBLE) {
            boolean matchPassword = Utilities.reConfirmPassword(password, confirm_password);
            if (!matchPassword) {
                txtConfirmPass.setErrorEnabled(true);
                txtConfirmPass.setError(getString(R.string.password_not_equals));
                return false;
            } else {
                txtConfirmPass.setErrorEnabled(false);
                txtConfirmPass.setError(null);
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
            txtPassword.setErrorEnabled(true);
            txtPassword.setError(getString(R.string.password_no_valid));
            return false;
        } else {
            txtPassword.setErrorEnabled(false);
            txtPassword.setError(null);
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
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            //metodo que se ejecuta al terminar el proceso
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Utilities.exceptionFirebaseAdministrator(getContext(), task, LOG_TAG);
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
        /*    pref.edit().putBoolean(getString(R.string.pref_logged_with_facebook),false).apply();
            pref.edit().putBoolean(getString(R.string.pref_logged_with_firebase),false).apply();
            pref.edit().putBoolean(getString(R.string.pref_logged_with_anonymous),true).apply();
            startMainActivity();
            */
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Utilities.exceptionFirebaseAdministrator(getContext(), task, LOG_TAG);
                            setUpUiRev(7);
                        }
                    }
                });
    }

    private void startMainActivity() {
        pref.edit().putBoolean(getString(R.string.pref_first_launch), false).apply();
        Utilities.startMainActivity(getContext());
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Validate if is new user or  old user trying enter
     *
     * @param user
     * @param pass
     */

    private void handleFirebaseLoginOrRegister(String user, String pass) {
        if (chAcceptTerm.isChecked()) {
            setUpFirstLoginUI(2);
            if (txtConfirmPass.getVisibility() == View.VISIBLE) {
                createFirebaseUser(user, pass);
            } else {
                signInFirebase(user, pass);
            }
        } else {
            Utilities.showToastLong(R.string.terms_policies_confirmation, getContext());
        }
    }

    /**
     * Login user with firebase
     *
     * @param user
     * @param pass
     */

    private void signInFirebase(String user, String pass) {
        firebaseAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Utilities.exceptionFirebaseAdministratorForCreateAccount(getContext(), task, LOG_TAG);
                            setUpUiRev(2);
                        } else {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                startMainActivity();
                                BdUserUtilities.updateUniqueField(user.getUid(), NdUser.getName(NdUser.LAST_LOGIN), new Date().getTime());
                            } else {
                                Utilities.showToastLong(R.string.firebase_email_send_wait, getContext());
                                emailUser = user;
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
        firebaseAuth.createUserWithEmailAndPassword(user, pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Utilities.exceptionFirebaseAdministratorForCreateAccount(getContext(), task, LOG_TAG);
                            setUpUiRev(2);
                        } else {
                            final FirebaseUser user = firebaseAuth.getCurrentUser();
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
                                                    Utilities.showToastLong(R.string.firebase_email_send_error, getContext());
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
        firebaseAuth.addAuthStateListener(fireAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //Cuando la clase deja de "escuchar"
        if (firebaseAuth != null)
            firebaseAuth.removeAuthStateListener(fireAuthStateListener);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_enter_no_login) {
            //startMainActivity();
            if (!Utilities.isNetworkAvailable(getContext())) {
                Utilities.showToastLong(R.string.string_no_network, getContext());
            } else {
                if (chAcceptTerm1.isChecked()) {
                    setUpFirstLoginUI(1);
                    handleAnonymousLogIn();
                } else {
                    Utilities.showToastLong(R.string.terms_policies_confirmation, getActivity());
                }
            }
        } else if (i == R.id.btn_log_in) {
            if (txtConfirmPass.getVisibility() == View.VISIBLE) {
                txtConfirmPass.setVisibility(View.GONE);
            }
        } else if (i == R.id.btn_sign_in) {
            if (txtConfirmPass.getVisibility() == View.GONE) {
                txtConfirmPass.setVisibility(View.VISIBLE);
            }
        } else if (i == R.id.btnRegister) {
            if (!Utilities.isNetworkAvailable(getContext())) {
                Utilities.showToastLong(R.string.string_no_network, getContext());
            } else {
                validateData();
            }
        } else if (i == R.id.btn_enter) {
            if (txtConfirmPass.getVisibility() == View.VISIBLE) {
                txtConfirmPass.setVisibility(View.GONE);
            }
            setUpUiRev(6);
            //FirebaseAuth.getInstance().signOut();
            //BdUserUtilities.removeUserListener(firebaseAuth.getCurrentUser());
            txtConfirmPass.getEditText().setText(null);
            txtPassword.getEditText().setText(null);
        } else if (i == R.id.btnEmail) {
            chAcceptTerm.setChecked(chAcceptTerm1.isChecked());
            firstLoginLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        } else if (i == R.id.btnBack) {
            chAcceptTerm1.setChecked(chAcceptTerm.isChecked());
            loginLayout.setVisibility(View.GONE);
            firstLoginLayout.setVisibility(View.VISIBLE);
        } else if (i == R.id.btn_res_password_email_step) {
            loginLayout.setVisibility(View.GONE);
            restorePassResendLayout.setVisibility(View.VISIBLE);
        } else if (i == R.id.btnBackEmailLogin) {
            restorePassResendLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            if (resendRestoreOption) {
                TransitionManager.beginDelayedTransition(relativeLayout,
                        new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
                btnRestorePassResend.setText(R.string.res_password_option);
                resendRestoreOption = false;
                txtRestoreResendTitle.setText(R.string.res_password_option);
                txtRestoreResendDescription.setText(R.string.res_password_description);
                btnConfirmResetResend.setText(R.string.res_password_confirm);
                emailUser = null;
                txtEmailResPass.getEditText().setEnabled(true);
                txtEmailResPass.getEditText().setText("");
            }
        } else if (i == R.id.btnConfirmResetResend) {
            String email = txtEmailResPass.getEditText().getText().toString().trim();
            txtEmailResPass.getEditText().setText(email);
            if (validateEmail(email, txtEmailResPass)) {
                if (!resendRestoreOption) {
                    Utilities.sendResetPassEmail(getContext(), email, firebaseAuth);
                } else {
                    setUpFirstLoginUI(2);
                    if (!emailUser.isEmailVerified()) {
                        emailUser.sendEmailVerification()
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
                                                Utilities.showToastLong(R.string.firebase_email_send_error, getContext());
                                                Log.e(LOG_TAG, e.getMessage());
                                            }
                                        }
                                        TransitionManager.beginDelayedTransition(relativeLayout,
                                                new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
                                        btnRestorePassResend.setText(R.string.res_password_option);
                                        resendRestoreOption = false;
                                        txtRestoreResendTitle.setText(R.string.res_password_option);
                                        txtRestoreResendDescription.setText(R.string.res_password_description);
                                        btnConfirmResetResend.setText(R.string.res_password_confirm);
                                        emailUser = null;
                                        txtEmailResPass.getEditText().setEnabled(true);
                                        txtEmailResPass.getEditText().setText("");
                                    }
                                });
                    }
                }
                txtEmailResPass.getEditText().setText("");
                restorePassResendLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
            }
        }
    }


}