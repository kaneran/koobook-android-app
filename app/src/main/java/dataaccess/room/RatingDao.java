package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import entities.Rating;

@Dao
public interface RatingDao {

    @Insert
    void insertRatings(Rating... rating);
}
