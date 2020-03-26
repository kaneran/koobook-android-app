package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.google.android.flexbox.JustifyContent;

import entities.Color;

@Dao
public interface ColorDao {

    @Insert
    void insertColor(Color... color);

    @Query("SELECT backgroundColor FROM Color WHERE book_bookId =:bookId")
    String getBackgroundColorString(int bookId);

    @Query("UPDATE Color SET backgroundColor= :backgroundColor WHERE book_bookId =:bookId")
    public void updateUserEmail(int bookId, String backgroundColor);
}
