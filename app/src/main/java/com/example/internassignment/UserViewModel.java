package com.example.internassignment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.internassignment.model.UserModel;

public class UserViewModel extends ViewModel {
    private  UserRepository userRepository;
    private LiveData<UserModel> users;

    public UserViewModel(Context context){
        userRepository=new UserRepository(context);
        users= userRepository.getUser();
    }

    public LiveData<UserModel> getUsers() {
        return users;
    }

    public void addUser(UserModel userModel){
        userRepository.insertUser(userModel);
    }
}
