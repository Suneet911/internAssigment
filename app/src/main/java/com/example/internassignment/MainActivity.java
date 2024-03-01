package com.example.internassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.internassignment.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ViewModelFactory factory = new ViewModelFactory(getApplicationContext());
        userViewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);

        userViewModel.getUsers().observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                if (userModel !=null){
                    Intent intent= getIntent();
                    String username=intent.getStringExtra("username");
                    String email=intent.getStringExtra("email");
                    String phone=intent.getStringExtra("phone");

                    binding.userName.setText(username);
                    binding.userEmail.setText(email);
                    binding.userPhone.setText(phone);

                }
            }
        });


    }
}