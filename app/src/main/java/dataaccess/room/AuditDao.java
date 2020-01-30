package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import entities.Audit;

@Dao
public interface AuditDao {

    @Insert
    void insertAudit(Audit... audit);

    @Query("SELECT auditId FROM Audit WHERE user_userId = :userId AND book_bookId = :bookId")
    int getAuditId(int userId, int bookId);

    @Query("SELECT * FROM Audit WHERE user_userId = :userId AND book_bookId = :bookId")
    Audit getAudit(int userId, int bookId);

    @Query("SELECT auditId FROM Audit WHERE user_userId = :userId ORDER BY auditId DESC")
    List<Integer> getAuditIds(int userId);
}
