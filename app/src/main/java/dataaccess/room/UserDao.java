package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import java.util.List;

import entities.User;

@Dao
public interface UserDao {

    @Insert
    void insertUserAccont(User... user);

    @Query("SELECT userId FROM User where email = :email")
    public int getUserId(String email);

    @Query("SELECT firstName FROM User where userId = :userId")
    public String getUsersName(int userId);

    @Query("SELECT * FROM User where firstName = :firstName")
    List<User> test(String firstName);





}

