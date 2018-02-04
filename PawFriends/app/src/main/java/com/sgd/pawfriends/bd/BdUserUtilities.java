package com.sgd.pawfriends.bd;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.sgd.pawfriends.PawFriendsApp;
import com.sgd.pawfriends.custom.PawFriendsConstants;
import com.sgd.pawfriends.entities.NdUser;

/**
 * Created by Daza on 14/05/2017.
 */

public class BdUserUtilities {

    // private static DatabaseReference mDatabase;
    private static DocumentReference NdUserDocRef;
    private static final String LOG_TAG = BdUserUtilities.class.getSimpleName();
    private static NdUser user;
    private static ValueEventListener listener;


/**
 static {
 mDatabase = FirebaseDatabase.getInstance().getReference();
 listener = new ValueEventListener() {
@Override public void onDataChange(DataSnapshot dataSnapshot) {
user = dataSnapshot.getValue(NdUser.class);

}

@Override public void onCancelled(DatabaseError databaseError) {
Log.w(LOG_TAG, databaseError.toException());
}
};
 }
 **/

    /**
     * Method for creation of the user in BD
     *
     * @param user
     */
    public static void createUser(NdUser user) {
        // mDatabase.child(PawFriendsConstants.ND_USER).child(user.getToken()).setValue(user);
        NdUserDocRef = PawFriendsApp.getInstance().getFirestoreDatabase()
                .document(PawFriendsConstants.ND_USER.concat("/").concat(user.getToken()));
        NdUserDocRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "Usuario creado");
                } else {
                    Log.w(LOG_TAG, "Error al crear usuario ");
                }
            }
        });
        setUser(user);
    }

    /**
     * Target an User 'node' to watch updates
     * @param UID
     */
    /**
     public static void  getUserByUID(String UID){
     // mDatabase.child(PawFriendsConstants.ND_USER).child(UID).addValueEventListener(listener);
     NdUserDocRef = PawFriendsApp.getInstance().getFirestoreDatabase()
     .document(PawFriendsConstants.ND_USER.concat("/").concat(UID));
     NdUserDocRef.get()
     }
     **/

    /**
     * update a value
     *
     * @param UID
     * @param field
     * @param value
     */
    public static void updateUniqueField(String UID, String field, Object value) {
        //mDatabase.child(PawFriendsConstants.ND_USER).child(UID).child(field).setValue(value);
        NdUserDocRef = PawFriendsApp.getInstance().getFirestoreDatabase()
                .document(PawFriendsConstants.ND_USER.concat("/").concat(UID));
        NdUserDocRef.update(field, value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG, "Error updating document", e);
                    }
                });
    }

    /**
     * public static void removeUserListener(FirebaseUser user){
     * mDatabase.child(PawFriendsConstants.ND_USER).child(user.getUid()).removeEventListener(listener);
     * BdUserUtilities.user = null;
     * }
     **/

    public static NdUser getUser() {
        return user;
    }

    public static void setUser(NdUser user) {
        BdUserUtilities.user = user;
    }
}
