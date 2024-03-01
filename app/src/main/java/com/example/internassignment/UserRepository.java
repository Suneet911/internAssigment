package com.example.internassignment;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.service.autofill.UserData;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {
    private final UserDao userDao;

    public UserRepository(Context context){
        UserDatabase db= UserDatabase.getInstance(context);
                userDao= db.userDao();
    }

    public LiveData<UserModel> getUser(){
        return userDao.getUser();
    }
    public void insertUser(UserModel userModel){
//        userDao.insert(userModel);
        new InsertAsyncTask(userDao).execute(userModel);
    }

    // AsyncTask to insert user in background
    private static class InsertAsyncTask extends AsyncTask<UserModel, Void, Void> {
        private final UserDao asyncTaskDao;

        InsertAsyncTask(UserDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserModel... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
