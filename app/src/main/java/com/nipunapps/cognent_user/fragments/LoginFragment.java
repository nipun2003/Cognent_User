package com.nipunapps.cognent_user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.nipunapps.cognent_user.R;
import com.nipunapps.cognent_user.activity.MainActivity;
import com.nipunapps.cognent_user.databinding.FragmentLoginBinding;
import com.nipunapps.cognent_user.utils.Constants;
import com.nipunapps.cognent_user.viewmodels.LoginViewModel;
import com.nipunapps.cognent_user.viewmodels.RegisterViewModel;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding loginBinding;
    private LoginViewModel loginViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        return loginBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(LoginViewModel.class);
        observeFields();
        loginBinding.login.setOnClickListener(v -> {
            loginUser();
        });
        loginBinding.etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE) {
                    loginBinding.login.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void loginUser() {
        String email = Objects.requireNonNull(loginBinding.etEmail.getText()).toString();
        String password = Objects.requireNonNull(loginBinding.etPassword.getText()).toString();
        if (email.isEmpty() || !Constants.validateEmail(email)) {
            loginBinding.etEmail.setError(requireContext().getString(R.string.valid_email));
            return;
        }
        if (password.isEmpty()) {
            loginBinding.etPasswordLayout.setError(requireContext().getString(R.string.valid_password));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loginBinding.etPasswordLayout.setErrorEnabled(false);
                }
            }, 2000);
            return;
        }
        loginViewModel.loginUser(email, password, message -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            requireContext().startActivity(intent);
            requireActivity().finish();
        });
    }

    private void observeFields() {
        loginViewModel.isLoading.observe(getViewLifecycleOwner(), (result) -> {
            loginBinding.progressBar.setVisibility(
                    (result) ? View.VISIBLE : View.GONE
            );
        });

        loginViewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
            if (!error.isEmpty())
                Snackbar.make(requireContext(), loginBinding.getRoot(), error, 2000).show();
        });
    }
}