package com.nipunapps.cognent_user.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nipunapps.cognent_user.interfaces.FirebaseRequestCallback;
import com.nipunapps.cognent_user.interfaces.OnSuccessEvent;
import com.nipunapps.cognent_user.repositories.AuthRepository;

public class LoginViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<Boolean>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>("");
    public LiveData<String> errorMessage = _errorMessage;

    private final MutableLiveData<String> _successMessage = new MutableLiveData<>("");
    public LiveData<String> successMessage = _successMessage;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application.getApplicationContext());
    }

    public void loginUser(String email, String password, OnSuccessEvent onSuccessEvent){
        authRepository.loginUser(email, password, new FirebaseRequestCallback() {
            @Override
            public void onLoading() {
                _isLoading.postValue(true);
            }

            @Override
            public void onSuccess(String message) {
                _isLoading.postValue(false);
                onSuccessEvent.onSuccess(message);
            }

            @Override
            public void onError(String message) {
                _isLoading.postValue(false);
                _errorMessage.postValue(message);
            }
        });
    }
}
