package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import entities.Rating;

@Dao
public interface RatingDao {

    @Insert
    void insertRatings(Rating... rating);

    @Query("SELECT ratingId FROM Rating WHERE book_bookId = :bookId")
    int getRatingId(int bookId);
}
