package com.nipunapps.cognent_user.repositories;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nipunapps.cognent_user.interfaces.FirebaseProductDetailCallback;
import com.nipunapps.cognent_user.interfaces.FirebaseProductListCallback;
import com.nipunapps.cognent_user.utils.Product;

import java.util.ArrayList;

public class DataRepository {

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference productCollection = firebaseFirestore.collection("Products");

    public void getProduct(String pid, FirebaseProductDetailCallback firebaseProductDetailCallback) {
        firebaseProductDetailCallback.onLoading();
        try {
            productCollection.document(pid).get().addOnSuccessListener(result -> {
                firebaseProductDetailCallback.onSuccess(result.toObject(Product.class));

            }).addOnFailureListener(e -> {
                firebaseProductDetailCallback.onError(e.getLocalizedMessage());
            });
        } catch (Exception e) {
            firebaseProductDetailCallback.onError(e.getLocalizedMessage());
        }
    }

    public void getProducts(FirebaseProductListCallback firebaseProductListCallback) {
        firebaseProductListCallback.onLoading();
        ArrayList<Product> products = new ArrayList<>();
        try {
            productCollection.get().addOnCompleteListener(result -> {
                if (result.isSuccessful()) {
                    products.clear();
                    for (QueryDocumentSnapshot documentSnapshot : result.getResult()) {
                        Product product = documentSnapshot.toObject(Product.class);
                        products.add(product);
                    }
                    firebaseProductListCallback.onSuccess(products);
                } else {
                    firebaseProductListCallback.onError("Something went wrong");
                }
            }).addOnFailureListener(e -> {
                firebaseProductListCallback.onError(e.getLocalizedMessage());
            });
        } catch (Exception e) {
            firebaseProductListCallback.onError(e.getLocalizedMessage());
        }
    }
}
