package com.nipunapps.cognent_user.interfaces;

public interface FirebaseRequestCallback {
    void onLoading();
    void onSuccess(String message);
    void onError(String message);
}
