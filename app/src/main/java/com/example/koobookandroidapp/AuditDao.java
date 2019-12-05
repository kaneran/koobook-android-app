package com.example.koobookandroidapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface AuditDao {

    @Insert
    void insertAudit(Audit... audit);

    @Query("SELECT auditId FROM Audit WHERE user_userId = :userId AND book_bookId = :bookId")
    int getAuditId(int userId, int bookId);
}
