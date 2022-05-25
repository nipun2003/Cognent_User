package com.nipunapps.cognent_user.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.nipunapps.cognent_user.R;
import com.nipunapps.cognent_user.databinding.FragmentRegisterBinding;
import com.nipunapps.cognent_user.interfaces.OnSuccessEvent;
import com.nipunapps.cognent_user.utils.Constants;
import com.nipunapps.cognent_user.utils.User;
import com.nipunapps.cognent_user.viewmodels.RegisterViewModel;

import java.util.Objects;


public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding registerBinding;
    private RegisterViewModel registerViewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        return registerBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        registerViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(RegisterViewModel.class);
        observeFields();
        registerBinding.register.setOnClickListener(v -> {
            registerUser();
        });

        registerBinding.etConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE) {
                    registerBinding.register.performClick();
                    return true;
                }
                return false;
            }
        });
        registerBinding.goToLogin.setOnClickListener(v -> {
            view.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.from_right_exit));
            navController.navigate(R.id.goToLoginGlobal);
        });
    }


    private void registerUser() {
        String name = Objects.requireNonNull(registerBinding.etName.getText()).toString();
        String email = Objects.requireNonNull(registerBinding.etEmail.getText()).toString();
        String phone = Objects.requireNonNull(registerBinding.etPhone.getText()).toString();
        String password = Objects.requireNonNull(registerBinding.etPassword.getText()).toString();
        String confirmPassword = Objects.requireNonNull(registerBinding.etConfirmPassword.getText()).toString();
        if (name.length() < 3) {
            registerBinding.etName.setError(requireContext().getString(R.string.valid_name));
            return;
        }
        if (email.isEmpty() || !Constants.validateEmail(email)) {
            registerBinding.etEmail.setError(requireContext().getString(R.string.valid_email));
            return;
        }
        if (phone.length() < 10 || !Constants.validatePhone(phone)) {
            registerBinding.etPhone.setError(requireContext().getString(R.string.valid_phone));
            return;
        }
        if (password.length() < 8) {
            registerBinding.etPasswordLayout.setError(requireContext().getString(R.string.valid_password));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    registerBinding.etPasswordLayout.setErrorEnabled(false);
                }
            }, 2000);
            return;
        }
        if (!confirmPassword.equals(password)) {
            registerBinding.etConfirmPasswordLayout.setError(requireContext().getString(R.string.password_mismatch));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    registerBinding.etConfirmPasswordLayout.setErrorEnabled(false);
                }
            }, 2000);
            return;
        }

        User user = new User(name, email, phone, password);
        registerViewModel.registerUser(user, message -> {
            Snackbar.make(requireContext(), registerBinding.getRoot(), message, 4000).show();
            registerBinding.goToLogin.performClick();
        });
    }


    private void observeFields() {
        registerViewModel.isLoading.observe(getViewLifecycleOwner(), (result) -> {
            registerBinding.progressBar.setVisibility(
                    (result) ? View.VISIBLE : View.GONE
            );
        });

        registerViewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
            if (!error.isEmpty())
                Snackbar.make(requireContext(), registerBinding.getRoot(), error, 2000).show();
        });
    }
}