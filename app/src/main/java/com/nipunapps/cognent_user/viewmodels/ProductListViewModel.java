package com.nipunapps.cognent_user.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nipunapps.cognent_user.interfaces.FirebaseProductListCallback;
import com.nipunapps.cognent_user.repositories.DataRepository;
import com.nipunapps.cognent_user.utils.Product;

import java.util.ArrayList;
import java.util.Objects;

public class ProductListViewModel extends AndroidViewModel {
    private final DataRepository dataRepository;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<Boolean>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>("");
    public LiveData<String> errorMessage = _errorMessage;

    private final MutableLiveData<ArrayList<Product>> _products = new MutableLiveData<ArrayList<Product>>(new ArrayList());
    public LiveData<ArrayList<Product>> products = _products;

    private final MutableLiveData<Product> _product = new MutableLiveData<Product>();
    public LiveData<Product> product = _product;

    private final MutableLiveData<Integer> _selected = new MutableLiveData<>(1);
    public LiveData<Integer> selected = _selected;

    public void setSelectedProduct(int pos){
        _product.postValue(Objects.requireNonNull(products.getValue()).get(pos));
    }

    public ProductListViewModel(Application application) {
        super(application);
        dataRepository = new DataRepository();
        getProducts();
    }

    public void getProducts() {
        dataRepository.getProducts(new FirebaseProductListCallback() {
            @Override
            public void onLoading() {
                _isLoading.postValue(true);
            }

            @Override
            public void onSuccess(ArrayList<Product> data) {
                _isLoading.postValue(false);
                _products.postValue(data);
            }

            @Override
            public void onError(String message) {
                _isLoading.postValue(false);
                _errorMessage.postValue(message);
            }
        });
    }

}
