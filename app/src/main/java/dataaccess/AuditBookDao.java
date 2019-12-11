package dataaccess;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import entities.Audit;

@Dao
public interface AuditBookDao {

    @Query("SELECT book_bookId FROM Audit WHERE auditId = :auditId")
    int getBookId(int auditId);

    @Query("SELECT * FROM Audit WHERE user_userId = :userId AND book_bookId = :bookId")
    Audit getAudit(int userId, int bookId);
}
