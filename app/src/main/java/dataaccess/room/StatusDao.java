package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import entities.Status;

import java.util.List;

@Dao
public interface StatusDao {

    @Insert
    void insertStatus(Status... status);

    @Query("SELECT * FROM Status")
    List<Status> getStatuses();

}
