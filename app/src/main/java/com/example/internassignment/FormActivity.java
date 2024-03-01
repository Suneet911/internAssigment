package com.example.internassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import com.example.internassignment.databinding.ActivityFormBinding;

public class FormActivity extends AppCompatActivity {

    private ActivityFormBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewModelFactory factory = new ViewModelFactory(getApplicationContext());

        userViewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     if (validateForm()) {
                                                         String usernameInput = binding.etUsername.getText().toString().trim();
                                                         String emailInput = binding.etEmail.getText().toString().trim();
                                                         String phoneInput = binding.etPhoneNo.getText().toString().trim();
                                                         String passwordInput = binding.etPassword.getText().toString().trim();

                                                         UserModel userModel = new UserModel();
                                                         userModel.setUsername(usernameInput);
                                                         userModel.setEmail(emailInput);
                                                         userModel.setPhoneNumber(phoneInput);
                                                         userModel.setPassword(passwordInput);

                                                         userViewModel.addUser(userModel);

                                                         Intent intent = new Intent(FormActivity.this, MainActivity.class);
                                                         intent.putExtra("username", usernameInput);
                                                         intent.putExtra("email", emailInput);
                                                         intent.putExtra("phone", phoneInput);
                                                         startActivity(intent);
                                                     }
                                                 }
                                             }
        );
    }

    public boolean validateForm() {
        boolean isValid = true;

        String usernameInput = binding.etUsername.getText().toString().trim();
        String emailInput = binding.etEmail.getText().toString().trim();
        String phoneInput = binding.etPhoneNo.getText().toString().trim();
        String passwordInput = binding.etPassword.getText().toString().trim();
        String reEnterPasswordInput = binding.etReEnterPassword.getText().toString().trim();


        if (usernameInput.isEmpty()) {
            isValid = false;
            binding.nameLayout.setHelperText("Field can't be empty");
        } else {
            binding.nameLayout.setHelperText(null);
        }

        if (emailInput.isEmpty()) {
            isValid = false;
            binding.emailLayout.setHelperText("Field can't be empty");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            isValid = false;
            binding.emailLayout.setHelperText("Enter valid email address");
        } else {
            binding.emailLayout.setHelperText(null);
        }

        if (phoneInput.isEmpty()) {
            isValid = false;
            binding.phoneLayout.setHelperText("Field can't be empty");
        } else {
            binding.phoneLayout.setHelperText(null);
        }
        if (passwordInput.isEmpty()) {
            isValid = false;
            binding.passwordLayout.setHelperText("Field can't be empty");
        } else {

            binding.rePasswordLayout.setHelperText(null);
        }
        if (reEnterPasswordInput.isEmpty()) {
            isValid = false;
            binding.rePasswordLayout.setHelperText("Field can't be empty");
        } else if (!passwordInput.equals(reEnterPasswordInput)) {
            isValid = false;
            binding.passwordLayout.setHelperText("Password and Confirm password does not match");
            binding.rePasswordLayout.setHelperText("Password and Confirm password does not match");
        } else {
            binding.passwordLayout.setHelperText(null);
            binding.rePasswordLayout.setHelperText(null);
        }

        return isValid;

    }
}