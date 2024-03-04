package com.example.internassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.internassignment.databinding.ActivityFormBinding;
import com.example.internassignment.model.Country;
import com.example.internassignment.model.State;
import com.example.internassignment.model.UserModel;
import com.example.internassignment.utils.ApiService;
import com.example.internassignment.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.coroutines.Continuation;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormActivity extends AppCompatActivity {

    private ActivityFormBinding binding;
    private UserViewModel userViewModel;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = RetrofitClient.getApiService();
        ViewModelFactory factory = new ViewModelFactory(getApplicationContext());

//        String error = binding.countryName.getText().toString();
//        Toast.makeText(FormActivity.this, error, Toast.LENGTH_SHORT).show();

        userViewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);

        fetchCountryAndState();


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     if (validateForm()) {
                                                         login();
                                                     }
                                                 }
                                             }
        );
    }

    private void fetchCountryAndState() {
        try {
            String api_key = "Vnd2YlcxUDg2bjVSajZXS2hBQTZQUWwyT0E3NEhWanVyeVlFOUs5QQ==";
            Call<List<Country>> countriesCall = apiService.getCountries(api_key);
            countriesCall.enqueue(new Callback<List<Country>>() {
                @Override
                public void onResponse(@NonNull Call<List<Country>> call, @NonNull Response<List<Country>> response) {
                    if (response.isSuccessful()) {
                        List<Country> countries = response.body();
                        List<String> countryNames = new ArrayList<>();
                        assert countries != null;
                        for (Country country : countries) {
                            countryNames.add(country.getName());
                        }
                        setCountry(countryNames);
                        Log.e("FormActivity2", Arrays.toString(countryNames.toArray()));
                        Spinner autoComplete = binding.countryName;

                        Log.e("FormActivity", Arrays.toString(countryNames.toArray()));
                        autoComplete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedCountry = (String) parent.getItemAtPosition(position);
                                String countryCode = "";
                                for (Country country : countries) {
                                    if (country.getName().equals(selectedCountry)) {
                                        countryCode = country.getIso2();
                                        break;
                                    }
                                }
//                                Toast.makeText(FormActivity.this, countryCode, Toast.LENGTH_SHORT).show();
                                Call<List<State>> stateCall = apiService.getState(api_key, countryCode);
                                stateCall.enqueue(new Callback<List<State>>() {
                                    @Override
                                    public void onResponse(@NonNull Call<List<State>> call, Response<List<State>> response) {
                                        if (response.isSuccessful()) {
                                            List<State> stateList = response.body();
                                            List<String> stateNames = new ArrayList<>();
                                            assert stateList != null;
                                            for (State state : stateList) {
                                                stateNames.add(state.getName());
                                            }
                                            setState(stateNames);
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<List<State>> call, Throwable t) {
                                        Toast.makeText(FormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Handle the case where nothing is selected if needed
                            }
                        });


                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Country>> call, Throwable t) {
                    Toast.makeText(FormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(FormActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setState(List<String> stateNames) {

        Spinner autoComplete = findViewById(R.id.stateText);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, stateNames);
        autoComplete.setAdapter(arrayAdapter);
    }

    private void setCountry(List<String> countryNames) {

        Spinner autoComplete = binding.countryName;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, countryNames);
        autoComplete.setAdapter(arrayAdapter);
    }

    private void login() {
        String usernameInput = binding.etUsername.getText().toString().trim();
        String emailInput = binding.etEmail.getText().toString().trim();
        String phoneInput = binding.etPhoneNo.getText().toString().trim();
        String passwordInput = binding.etPassword.getText().toString().trim();
        String countryInput=binding.countryName.getSelectedItem().toString().trim();
        String stateInput=binding.stateText.getSelectedItem().toString().trim();

        UserModel userModel = new UserModel();
        userModel.setUsername(usernameInput);
        userModel.setEmail(emailInput);
        userModel.setPhoneNumber(phoneInput);
        userModel.setPassword(passwordInput);
        userModel.setCountry(countryInput);
        userModel.setState(stateInput);

        userViewModel.addUser(userModel);

        Intent intent = new Intent(FormActivity.this, MainActivity.class);
        intent.putExtra("username", usernameInput);
        intent.putExtra("email", emailInput);
        intent.putExtra("phone", phoneInput);
        intent.putExtra("country",countryInput);
        intent.putExtra("state", stateInput);
        startActivity(intent);
    }

    public boolean validateForm() {
        boolean isValid = true;

        String usernameInput = binding.etUsername.getText().toString().trim();
        String emailInput = binding.etEmail.getText().toString().trim();
        String phoneInput = binding.etPhoneNo.getText().toString().trim();
        String passwordInput = binding.etPassword.getText().toString().trim();
        String reEnterPasswordInput = binding.etReEnterPassword.getText().toString().trim();
//        String countryInput = binding.countryName.getText().toString().trim();
//        String stateInput = binding.stateText.getText().toString().trim();


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

//        if (countryInput.isEmpty()) {
//            isValid = false;
////            binding.countryLayout.setHelperText("Please select a country");
//        } else {
////            binding.countryLayout.setHelperText(null);
//        }

//        if (stateInput.isEmpty()) {
//            isValid = false;
//            binding.stateLayout.setHelperText("Please select a state");
//        } else {
//            binding.stateLayout.setHelperText(null);
//        }

        if (passwordInput.isEmpty()) {
            isValid = false;
            binding.passwordLayout.setHelperText("Field can't be empty");
        } else {
            binding.passwordLayout.setHelperText(null);
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