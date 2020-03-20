package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import java.util.List;

import entities.User;

@Dao
public interface UserDao {

    @Insert
    void insertUserAccount(User... user);

    @Query("SELECT userId FROM User where email = :email")
    public int getUserId(String email);


    @Query("SELECT * FROM User where email = :email")
    public User getUser(String email);


    @Query("UPDATE User SET email= :email WHERE userId=4")
    public void updateUserEmail(String email);

    @Query("SELECT userId FROM User")
    public List<Integer> getUserIds();

}

