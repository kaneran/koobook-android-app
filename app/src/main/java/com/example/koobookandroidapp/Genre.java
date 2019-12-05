package com.example.koobookandroidapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Genre {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int genreId;

    @ColumnInfo(name = "genreLabel")
    private String genreLabel;

    public Genre(int genreId, String genreLabel) {
        this.genreId = genreId;
        this.genreLabel = genreLabel;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreLabel() {
        return genreLabel;
    }

    public void setGenreLabel(String genreLabel) {
        this.genreLabel = genreLabel;
    }
}
