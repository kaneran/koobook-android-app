package dataaccess.room;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface AuditStatusDao {

    @Query("SELECT audit_auditId FROM Status WHERE status = :status")
    List<Integer> getAuditIds(String status);

    @Query("SELECT status FROM Status WHERE audit_auditId = :auditId")
    String getStatus(int auditId);

    @Query("UPDATE Status SET status = :status WHERE audit_auditId = :auditId")
    void updateStatus(String status, int auditId);
}
