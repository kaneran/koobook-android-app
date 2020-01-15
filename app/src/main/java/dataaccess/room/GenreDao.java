package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import entities.Genre;

@Dao
public interface GenreDao {

    @Insert
    void insertGenre(Genre... genre);

    @Query("SELECT genreId FROM Genre WHERE genreLabel = :genreLabel")
    int getGenreId(String genreLabel);

    @Query("SELECT genreLabel FROM Genre WHERE genreId = :genreId")
    String getGenreLabel(int genreId);
}
