package com.nipunapps.cognent_user.repositories;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nipunapps.cognent_user.R;
import com.nipunapps.cognent_user.interfaces.FirebaseRequestCallback;
import com.nipunapps.cognent_user.utils.User;

public class AuthRepository {
    private final Context context;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference usersCollection = firebaseFirestore.collection("Users");
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public AuthRepository(Context context) {
        this.context = context;
    }

    public void registerUser(User user, FirebaseRequestCallback firebaseRequestCallback) {
        firebaseRequestCallback.onLoading();
        try {
            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnSuccessListener(authResult -> {
                FirebaseUser fUser = authResult.getUser();
                assert fUser != null;
                user.setUserId(fUser.getUid());
                fUser.sendEmailVerification().addOnSuccessListener(result -> {
                    usersCollection.document(fUser.getUid()).set(user).addOnSuccessListener(res -> {
                        firebaseRequestCallback.onSuccess(context.getString(R.string.email_verification_sent, user.getEmail()));
                        mAuth.signOut();
                    }).addOnFailureListener(e -> {
                        firebaseRequestCallback.onError(context.getString(R.string.error_storing_user_details));
                    });
                }).addOnFailureListener(e -> {
                    firebaseRequestCallback.onError(context.getString(R.string.error_send_email));
                });
            })
                    .addOnFailureListener(e -> {
                        Log.e("Nipun", e.getLocalizedMessage());
                        firebaseRequestCallback.onError(e.getLocalizedMessage());
                    });
        } catch (Exception e) {
            firebaseRequestCallback.onError(context.getString(R.string.something_went_wrong));
        }
    }

    public void loginUser(String email, String password, FirebaseRequestCallback firebaseRequestCallback) {
        firebaseRequestCallback.onLoading();
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                FirebaseUser fUser = authResult.getUser();
                if (fUser.isEmailVerified()) {
                    firebaseRequestCallback.onSuccess("Ok");
                } else {
                    fUser.sendEmailVerification().addOnSuccessListener(result -> {
                        firebaseRequestCallback.onSuccess(context.getString(R.string.email_verification_sent, email));
                        mAuth.signOut();
                    }).addOnFailureListener(e -> {
                        firebaseRequestCallback.onError(context.getString(R.string.error_send_email));
                    });
                }
            })
                    .addOnFailureListener(e -> {
                        Log.e("Nipun", e.getLocalizedMessage());
                        firebaseRequestCallback.onError(e.getLocalizedMessage());
                    });
        } catch (Exception e) {
            firebaseRequestCallback.onError(context.getString(R.string.something_went_wrong));
        }
    }
}
