package entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Book.class, parentColumns = "bookId", childColumns = "book_bookId", onDelete = ForeignKey.CASCADE))
public class Color {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int colorId;

    @ColumnInfo(name = "book_bookId")
    private int bookId;

    @ColumnInfo(name = "fontColor")
    private String fontColor;

    @ColumnInfo(name = "backgroundColor")
    private String backgroundColor;

    public Color(int colorId, int bookId, String fontColor, String backgroundColor) {
        this.colorId = colorId;
        this.bookId = bookId;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
