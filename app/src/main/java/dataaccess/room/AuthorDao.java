package dataaccess.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import entities.Author;

@Dao
public interface AuthorDao {

    @Query("SELECT authorId FROM Author WHERE name = :name")
    int getAuthorId(String name);

    @Query("SELECT name FROM Author WHERE authorId = :authorId")
    String getAuthorName(int authorId);

    @Insert
    void insertAuthor(Author... author);

}
