package com.sgd.pawfriends.bd;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sgd.pawfriends.PawFriendsApp;
import com.sgd.pawfriends.adapters.PetsAdapter;
import com.sgd.pawfriends.custom.PawFriendsConstants;
import com.sgd.pawfriends.custom.Utilities;
import com.sgd.pawfriends.entities.NdPets;

import java.io.File;
import java.io.IOException;
import java.util.List;

import id.zelory.compressor.Compressor;

/**
 * Created by Daza on 03/09/2017.
 */

public class BdPetsUtilities {

    //private static DatabaseReference mDatabase;
    private static CollectionReference NdColDocRef;
    private static final String LOG_TAG = BdPetsUtilities.class.getSimpleName();
    private static StorageReference storageRef;
    private List<NdPets> lstPets;
    private ValueEventListener listener;
    private PetsAdapter adapter;

    /**
     * static {
     * mDatabase = FirebaseDatabase.getInstance().getReference();
     * }
     **/

    public static void createPet(NdPets pet, final Context context) {
        /**
         mDatabase.child(PawFriendsConstants.ND_PETS).push().setValue(pet).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override public void onComplete(@NonNull Task<Void> task) {
        if(task.isSuccessful()){
        Utilities.showToastShort(R.string.add_pet_succes, context);
        }
        }
        });
         **/
        NdColDocRef = PawFriendsApp.getInstance().getFirestoreDatabase()
                .collection(PawFriendsConstants.ND_PETS);
        NdColDocRef.add(pet).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Log.v(LOG_TAG, "Compañero agregado");
                } else {
                    Log.v(LOG_TAG, "Un error ha ocurrido");
                }
            }
        });


    }

    public void deletePet(String token) {

    }

    public void editPet(NdPets pets) {

    }

    public static void uploadPhotoAndCreatePet(final String Uid, final String name, final Uri uri, final Context context) {
        storageRef = FirebaseStorage.getInstance().getReference();
        if (uri != null) {
            File file = new File(getRealPathFromUri(context, uri));
            File compressFile = null;
            try {
                compressFile = new Compressor(context).compressToFile(file);
                String nameWithDate = Utilities.concatTime(name);
                StorageReference ref = storageRef.child("users/" + Uid + "/images/pets/" + nameWithDate + ".jpg");
                ref.putFile(Uri.fromFile(compressFile)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        NdPets pet = new NdPets();
                        pet.setName(name);
                        pet.setOwner(Uid);
                        pet.setLocalUriImage(uri.getEncodedPath());
                        pet.setUrlImage(downloadUri.toString());
                        createPet(pet, context);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            NdPets pet = new NdPets();
            pet.setName(name);
            pet.setOwner(Uid);
            createPet(pet, context);
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
