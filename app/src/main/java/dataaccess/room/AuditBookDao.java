package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import entities.Audit;

@Dao
public interface AuditBookDao {

    @Query("SELECT book_bookId FROM Audit WHERE auditId = :auditId")
    int getBookId(int auditId);

    @Query("SELECT * FROM Audit WHERE user_userId = :userId AND book_bookId = :bookId")
    Audit getAudit(int userId, int bookId);

    @Query("SELECT auditId FROM Audit WHERE book_bookId = :bookId")
    List<Integer> getAuditIds(int bookId);
}
