package com.sgd.pawfriends.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sgd.pawfriends.PawFriendsApp;
import com.sgd.pawfriends.R;
import com.sgd.pawfriends.adapters.PetsAdapter;
import com.sgd.pawfriends.bd.BdPetsUtilities;
import com.sgd.pawfriends.custom.PawFriendsConstants;
import com.sgd.pawfriends.custom.Utilities;
import com.sgd.pawfriends.entities.NdPets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * A placeholder fragment containing a simple view.
 */
public class PetsFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = PetsFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA = 1, SELECT_IMAGE = 0;
    private Boolean inflateAddOptions = false;
    private Menu mMenu;
    private MenuInflater mInflater;
    @BindView(R.id.txtPetName)
    EditText txtPetName;
    @BindView(R.id.txtPetBreed)
    EditText txtPetBreed;
    @BindView(R.id.rvPets)
    RecyclerView rvPets;
    @BindView(R.id.bottom_add_pets)
    LinearLayout bottomAddPets;
    @BindView(R.id.add_pet_confirm)
    FloatingActionButton fabAddPetConfirm;
    @BindView(R.id.cancel_pet_confirm)
    FloatingActionButton fabCancelPetConfirm;
    @BindView(R.id.ivPet)
    ImageView ivPet;
    @BindView(R.id.txtBirthday)
    EditText txtBirthday;

    @BindView(R.id.layout_pet_detail_main_sub1)
    ConstraintLayout layoutDetailSub1;
    @BindView(R.id.layout_pet_detail_main_sub2)
    ConstraintLayout layoutDetailSub2;
    @BindView(R.id.pet_detail_name)
    TextView petDetailName;


    PetsAdapter adapter;
    BdPetsUtilities petsUtilities;
    FirebaseDatabase firebaseDatabase;
    List<NdPets> lstPets = new ArrayList<>();
    ;
    PawFriendsApp mApplication;
    BottomSheetBehavior bottomSheetBehavior;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private Uri outputFileUri;

    public PetsFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pets, container, false);
        ButterKnife.bind(this, rootView);
        // setHasOptionsMenu(true);
        setListeners();
        setupDatabaseAndData();
        setupPetsList();
        setupFirestoreListener();


        bottomSheetBehavior = BottomSheetBehavior.from(bottomAddPets);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // this part hides the button immediately and waits bottom sheet
                // to collapse to show
                if (BottomSheetBehavior.STATE_HIDDEN != newState) {
                    fabAddPetConfirm.animate().scaleX(1).scaleY(1).setDuration(300).start();
                } else {
                    fabAddPetConfirm.animate().scaleX(0).scaleY(0).setDuration(300).start();
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                /**
                 fabAddPetConfirm.animate().scaleX(0 + (slideOffset*-1)).scaleY(0 +  (slideOffset*-1)).setDuration(0).start();
                 Log.v(LOG_TAG,fabAddPetConfirm.getScaleX() + "");
                 **/
            }
        });

        return rootView;
    }

    private void setupPetsList() {
        lstPets.add(0, new NdPets());
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvPets.setLayoutManager(horizontalLayoutManagaer);
        adapter = new PetsAdapter(getContext(), lstPets, this);
        rvPets.setAdapter(adapter);
    }

    private void setupDatabaseAndData() {
        //Database vars and data
        mApplication = (PawFriendsApp) getActivity().getApplicationContext();
        firebaseDatabase = mApplication.getDatabase();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //getPets(user.getUid());
    }

    private void setListeners() {
        // ivPet.setOnClickListener(this);
        //txtBirthday.setOnFocusChangeListener(this);
        txtBirthday.setKeyListener(null);

        //fabAddPetConfirm.setOnClickListener(this);
        // fabCancelPetConfirm.setOnClickListener(this);
    }

    private void setupFirestoreListener() {
        CollectionReference NdPetColRef = PawFriendsApp.getInstance().getFirestoreDatabase()
                .collection(PawFriendsConstants.ND_PETS);
        NdPetColRef.whereEqualTo("owner", user.getUid()).addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(LOG_TAG, "Listen failed.", e);
                    return;
                }

                lstPets.subList(1, lstPets.size()).clear();
                for (DocumentSnapshot doc : querySnapshot) {
                    lstPets.add(doc.toObject(NdPets.class));
                }

                adapter.notifyDataSetChanged();
                if (rvPets.getVisibility() == View.INVISIBLE)
                    rvPets.setVisibility(View.VISIBLE);
            }
        });


    }

    public void getPets(String token) {

        DatabaseReference mDatabase = firebaseDatabase.getReference();
        mDatabase.child(PawFriendsConstants.ND_PETS).orderByChild("owner").equalTo(token)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        lstPets.subList(1, lstPets.size()).clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            NdPets pet = snapshot.getValue(NdPets.class);
                            lstPets.add(pet);
                        }

                        //lstPets.add(0,new NdPets(null,null,null,null,null,null,null,null,null,null,null,null));
                        adapter.notifyDataSetChanged();
                        rvPets.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v(LOG_TAG, "No pet information");
                    }
                });
    }

    @OnClick(R.id.ivPet)
    public void loadImage() {
        if (Utilities.checkPermission(getContext(), Manifest.permission.CAMERA)) {
            final List<Intent> cameraIntents = new ArrayList<Intent>();
            final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            final PackageManager packageManager = getActivity().getPackageManager();
            final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
            for (ResolveInfo res : listCam) {
                final String packageName = res.activityInfo.packageName;
                final Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(packageName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                cameraIntents.add(intent);
            }

            // Filesystem.
            final Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            //galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

            // Chooser of filesystem options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

            // Add the camera options.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

            startActivityForResult(chooserIntent, SELECT_IMAGE);
        } else {
            Utilities.showToastShort(R.string.camera_permission_advice, getContext());
            final Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");
            startActivityForResult(chooserIntent, SELECT_IMAGE);
        }
    }

    @OnClick(R.id.cancel_pet_confirm)
    public void cancel_add_option() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        cleanFields();
    }

    @OnClick(R.id.add_pet_confirm)
    public void add_pet() {
        if (txtPetName.getText().toString().equals("") || txtPetBreed.getText().toString().equals("")) {
            Utilities.showToastShort(R.string.missing_field_pets, getContext());
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            BdPetsUtilities.uploadPhotoAndCreatePet(user.getUid(), txtPetName.getText().toString(), outputFileUri, getContext());
        }
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (v.getTag(R.string.tag_pet) != null) {
            if (Integer.parseInt(v.getTag(R.string.tag_pet).toString()) == 0) {

                if (layoutDetailSub1.getVisibility() == View.GONE) {
                    layoutDetailSub2.setVisibility(View.GONE);
                    layoutDetailSub1.setVisibility(View.VISIBLE);
                }

                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            } else {
                if (layoutDetailSub2.getVisibility() == View.GONE) {
                    layoutDetailSub1.setVisibility(View.GONE);
                    layoutDetailSub2.setVisibility(View.VISIBLE);
                    showPet(Integer.parseInt(v.getTag(R.string.tag_pet).toString()));

                }
            }
        }
    }

    private void showPet(int pos) {
        NdPets pet = lstPets.get(pos);
        petDetailName.setText(pet.getName());
    }


    private void cleanFields() {
        txtPetName.setText("");
        txtPetBreed.setText("");
        Glide.with(getContext()).load(R.drawable.ic_menu_camera).centerCrop().into(ivPet);
    }

    @OnFocusChange(R.id.txtBirthday)
    public void showCalendar(View v, boolean hasFocus) {
        if (hasFocus) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            txtBirthday.clearFocus();
                        }
                    });


            datePickerDialog.show();
        }
    }

    /*
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
       int id = v.getId();
        if(id == R.id.txtBirthday && hasFocus){
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            txtBirthday.clearFocus();
                        }
                    });


            datePickerDialog.show();
        }
    }
    */

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat formatter1 = new SimpleDateFormat(PawFriendsConstants.DATE_DD_MM_YYYY, Locale.getDefault());
        SimpleDateFormat formatter2 = new SimpleDateFormat(PawFriendsConstants.DATE_DD_MMMM_YYYY, Locale.getDefault());
        try {

            Date date = formatter1.parse(dayOfMonth + "-" + month + "-" + year);
            txtBirthday.setText(formatter2.format(date));
            txtBirthday.clearFocus();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = !action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                if (isCamera) {

                    outputFileUri = data.getData();
                    //Bundle bundle = data.getExtras();
                    //final Bitmap map = (Bitmap) bundle.get("data");
                    Glide.with(getContext()).load(outputFileUri).into(ivPet);
                    //ivPet.setImageBitmap(map);

                } else {
                    if (data != null) {
                        outputFileUri = data.getData();
                        Glide.with(getContext()).load(outputFileUri).into(ivPet);
                        //File file = new File(outputFileUri.getEncodedPath());
                        //ivPet.setImageURI(Uri.parse(file.toString()));
                    }
                }
            }
        }
    }

    /**
     public native String stringFromJNI();

     static {
     System.loadLibrary("native-lib");
     }
     **/
}
