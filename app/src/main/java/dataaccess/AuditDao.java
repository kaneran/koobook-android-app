package dataaccess;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import entities.Audit;

@Dao
public interface AuditDao {

    @Insert
    void insertAudit(Audit... audit);

    @Query("SELECT auditId FROM Audit WHERE user_userId = :userId AND book_bookId = :bookId")
    int getAuditId(int userId, int bookId);
}
