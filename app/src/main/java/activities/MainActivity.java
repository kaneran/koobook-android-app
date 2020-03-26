package activities;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import controllers.BookController;
import controllers.UserController;
import dataaccess.setup.AppDatabase;
import entities.Author;
import entities.Book;
import entities.BookAuthor;
import entities.BookGenre;
import entities.Genre;
import entities.Rating;
import entities.Review;
import extras.Helper;
import fragments.*;

import fragments.MainSlider;
import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.List;

//Tutorial followed- https://www.youtube.com/watch?v=oeKtwd1DBfg
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    TextView textview_toolbar_title;
    public static String toolbar_title;
    MainSlider mainSlider;
    CameraViewFragment cameraViewFragment;
    SearchFragment searchFragment;
    Helper helper = new Helper();
    UserController userController = new UserController();
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        mainSlider = new MainSlider();
        cameraViewFragment = new CameraViewFragment();
        searchFragment = new SearchFragment();
        setContentView(R.layout.activity_main);
        toolbar =  findViewById(R.id.toolbar);
        textview_toolbar_title = findViewById(R.id.toolbar_title);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, mainSlider).commit();



        //int userId = db.userDao().getUserId("test@city.ac.uk");
        //userController.storeUserId(this, userId);

        //insertDummyBookDataIntoRoomDatabase();
        //helper.deleteBook("9781448108299", getApplicationContext());


    }

    public void insertDummyBookDataIntoRoomDatabase(){
        List<Book> dummyBooks = new ArrayList<>();
        BookController bookController = new BookController(getApplicationContext());
        Book book;
        db.bookDao().insertBook(new Book(0,"1","The epic 1", "It only gets better", 234,"Bennett arrested manuel perrine thought brought end drug cartel boss reign terror would get justice murder best friend. Forced hiding mass murderer seeking vengeance detective michael bennett must decide whether stay protect family hunt man hunting. Wants nothing make bennett suffer make pay",
                "http://books.google.com/books/content?id=U1I1IItMbbsC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",0));
        book = db.bookDao().getBookBasedOnIsbnNumber("1");
        insertDummyAuthor(book.getBookId(), "James Patterson");
        insertDummyGenre(book.getBookId(), "Fiction");
        dummyBooks.add(book);

        db.bookDao().insertBook(new Book(0,"2","The epic 2", "It only gets better", 234,"Famous film director wayne tennet stands accused stepdaughter crossing line. Would lie darkest thriller year hollywood twist never see coming. Hollywood director committed unspeakable crime",
                "http://books.google.com/books/content?id=70OWDQAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",0));
        book = db.bookDao().getBookBasedOnIsbnNumber("2");
        insertDummyAuthor(book.getBookId(), "James Patterson");
        insertDummyGenre(book.getBookId(), "Fiction");
        dummyBooks.add(book);

        db.bookDao().insertBook(new Book(0,"3","The epic 3", "It only gets better", 234,"Things get even complicated meets father first time coma. Deep panicking going survive future getting sucked people dreams really starting take toll",
                "http://books.google.com/books/content?id=jrnLxhfW7gMC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",0));
        book = db.bookDao().getBookBasedOnIsbnNumber("3");
        insertDummyAuthor(book.getBookId(), "James Patterson");
        insertDummyGenre(book.getBookId(), "Fiction");
        dummyBooks.add(book);

        db.bookDao().insertBook(new Book(0,"4","The epic 4", "It only gets better", 234,"High stakes action never get see new nba hottest game town showcase big men bigger money. Real games always taken place court",
                "http://books.google.com/books/content?id=qpYTGNXOzDYC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",0));
        book = db.bookDao().getBookBasedOnIsbnNumber("4");
        insertDummyAuthor(book.getBookId(), "James Patterson");
        insertDummyGenre(book.getBookId(), "Fiction");
        dummyBooks.add(book);

        db.bookDao().insertBook(new Book(0,"5","The epic 5", "It only gets better", 234,"Claim backman may obtained secrets would compromise american satellite surveillance. Smuggled country military cargo plane backman given new identity new home italy. Final hours oval office outgoing president grants full pardon joel backman notorious washington power broker spent last six years federal prison",
                "http://books.google.com/books/content?id=5L5kW7d2dwIC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",0));
        book = db.bookDao().getBookBasedOnIsbnNumber("5");
        insertDummyAuthor(book.getBookId(), "James Patterson");
        insertDummyGenre(book.getBookId(), "Fiction");
        dummyBooks.add(book);

        db.bookDao().insertBook(new Book(0,"6","The epic 6", "It only gets better", 234,"Recalls pre revolution life flashbacks offred must navigate terrifying landscape torture persecution present day two men upon future hangs. 1 bestseller discover dystopian novel behind award winning tv series read 2019 booker prize winning sequel testaments believe resistance believe light without shadow rather shadow unless also light. Masterfully conceived executed haunting vision future places",
                "http://books.google.com/books/content?id=o79lk6nTsRgC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",0));
        book = db.bookDao().getBookBasedOnIsbnNumber("6");
        insertDummyAuthor(book.getBookId(), "James Patterson");
        insertDummyGenre(book.getBookId(), "Fiction");
        dummyBooks.add(book);

        db.bookDao().insertBook(new Book(0,"7","The epic 7", "It only gets better", 234,"Addition text features expanded sections fair play social ethics. Latest edition ethics sport rest laurels finest comprehensive collection literature date moral ethical issues confronting sport contemporary society",
                "http://books.google.com/books/content?id=-zp77gObI0AC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",0));
        book = db.bookDao().getBookBasedOnIsbnNumber("7");
        insertDummyAuthor(book.getBookId(), "Dominic Fell");
        insertDummyGenre(book.getBookId(), "History");
        dummyBooks.add(book);

        db.bookDao().insertBook(new Book(0,"8","The epic 8", "It only gets better", 234,"Tells potent story woman thought late love man falls ambition secret selves poised moment end everything. Within six peter dead road accident nightmare end fairy tale romance. Peter sister lucy believe fairy tales tasks rogue reporter jack parlabane discovering dark truth beh",
                "http://books.google.com/books/content?id=-zp77gObI0AC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",0));
        book = db.bookDao().getBookBasedOnIsbnNumber("8");
        insertDummyAuthor(book.getBookId(), "James Swinden");
        insertDummyGenre(book.getBookId(), "Business");
        dummyBooks.add(book);

        db.bookDao().insertBook(new Book(0,"9","The epic 9", "It only gets better", 234,"Work literature featured seminal work key understanding appreciating written word. Discerning bibliophiles readers enjoy unforgettable classic literature 1001 books must read die trove reviews covering century memorable writing. Entry accompanied authoritative yet opinionated critical essay describing importance influence work question",
                "http://books.google.com/books/content?id=VEZcPgAACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api",0));
        book = db.bookDao().getBookBasedOnIsbnNumber("9");
        insertDummyAuthor(book.getBookId(), "Ian Hand");
        insertDummyGenre(book.getBookId(), "Sports");
        dummyBooks.add(book);

        db.bookDao().insertBook(new Book(0,"10","The epic 10", "It only gets better", 234,"Heavy emphasis placed upon understanding things done particular way rather simply presenting set cookbook rules always work. Book basic introduction lost wax casting emphasis jewelry making. Experienced casters however probably find useful ideas may even find new techniques",
                "http://books.google.com/books/content?id=e_09Enaf4tIC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",0));
        book = db.bookDao().getBookBasedOnIsbnNumber("10");
        insertDummyAuthor(book.getBookId(), "John Grisham");
        insertDummyGenre(book.getBookId(), "Education");
        dummyBooks.add(book);


        for(Book dummyBook: dummyBooks){
            insertDummyReviews(dummyBook.getBookId());
            insertDummyRatings(dummyBook.getBookId());
            bookController.storeBookIsbn(getApplicationContext(),dummyBook.getIsbnNumber());
            bookController.likeBook();
        }
    }

    public void insertDummyRatings(int bookId){
        db.ratingDao().insertRatings(new Rating(0,bookId,4.5,2.4,3.4,4.1,92,3,2,2,1,15));
    }

    public void insertDummyReviews(int bookId){
        db.reviewDao().insertReview(new Review(0, bookId, "This book is great!"));
        db.reviewDao().insertReview(new Review(0, bookId, "An absolute page turner"));
        db.reviewDao().insertReview(new Review(0, bookId, "Was kept in the loop the entire time"));
        db.reviewDao().insertReview(new Review(0, bookId, "Fantastic!"));
        db.reviewDao().insertReview(new Review(0, bookId, "I'd fefinitely recommend this book to my friends"));
    }

    public void insertDummyAuthor(int bookId, String authorName){
        db.authorDao().insertAuthor(new Author(0, authorName));
        int authorId = db.authorDao().getAuthorId(authorName);
        db.bookAuthorDao().insertBookAuthor(new BookAuthor(0, bookId, authorId));
    }

    public void insertDummyGenre(int bookId, String genreLabel){
        db.genreDao().insertGenre(new Genre(0, genreLabel));
        int genreId = db.genreDao().getGenreId(genreLabel);
        db.bookGenreDao().insertBookGenre(new BookGenre(0, bookId, genreId));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Setting up the bottom navigation menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        bottomNavigationView.getMenu().setGroupCheckable(0,true, true);
        toolbar.setVisibility(View.GONE);
        switch(menuItem.getItemId()){
            case R.id.navigation_home:

                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, mainSlider).commit();
                textview_toolbar_title.setText("Home");
                return true;

            case R.id.navigation_scan:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, cameraViewFragment).commit();
                textview_toolbar_title.setText("Scan book");
                return true;

            case R.id.navigation_search:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, searchFragment).commit();
                textview_toolbar_title.setText("Search book(s)");
                return true;
        }
        return false;
    }




}
