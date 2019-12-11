package dataaccess;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import entities.Color;

@Dao
public interface ColorDao {

    @Insert
    void insertColor(Color... color);
}
