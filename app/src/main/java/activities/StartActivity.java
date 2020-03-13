package activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import controllers.BookController;
import dataaccess.setup.AppDatabase;
import entities.Author;
import entities.Book;
import entities.BookAuthor;
import entities.BookGenre;
import entities.Genre;
import entities.Rating;
import entities.Review;
import entities.User;

import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();

    }

    public void insertTestAccountIntoRoomDatabase(){
        User user = db.userDao().getUser("test@city.ac.uk");
        if(user == null) {
            db.userDao().insertUserAccount(new User(0, "test@city.ac.uk", "TestUser"));
        }
    }



    public void signUpButtonClicked(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void loginButtonClicked(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
