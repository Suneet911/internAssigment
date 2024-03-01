package com.example.internassignment;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user_table")
    LiveData<UserModel> getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserModel userModel);
}
