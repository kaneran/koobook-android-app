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

    @Query("SELECT status FROM Status WHERE audit_auditId = :auditId")
    String getStatus(int auditId);

    @Query("UPDATE Status SET status= :newStatus WHERE audit_auditId = :auditId")
    void updateStatusStatus(String newStatus, int auditId);

    @Query("UPDATE Status SET reason= :reason WHERE audit_auditId = :auditId")
    void updateStatusReason(String reason, int auditId);

}
