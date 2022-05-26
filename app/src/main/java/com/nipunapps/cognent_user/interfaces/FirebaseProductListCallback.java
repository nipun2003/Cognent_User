package com.nipunapps.cognent_user.interfaces;

import com.nipunapps.cognent_user.utils.Product;

import java.util.ArrayList;

public interface FirebaseProductListCallback {

    void onLoading();
    void onSuccess(ArrayList<Product> data);
    void onError(String message);
}
