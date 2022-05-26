package com.nipunapps.cognent_user.interfaces;

import com.nipunapps.cognent_user.utils.Product;

import java.util.ArrayList;

public interface FirebaseProductDetailCallback {
    void onLoading();
    void onSuccess(Product data);
    void onError(String message);
}
