package com.example.koobookandroidapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserAuditDao {

    @Query("SELECT * From Audit where user_userId = :userId")
    List<Audit> getAudits(int userId);
}
