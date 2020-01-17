package controllers;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.koobookandroidapp.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dataaccess.setup.AppDatabase;
import entities.Audit;
import entities.Author;
import entities.Book;
import entities.BookAuthor;
import entities.BookGenre;
import entities.Genre;
import entities.Rating;
import entities.Review;
import extras.Helper;
import extras.MyComparator;
import fragments.BookReviewFragment;
import fragments.ErrorFragment;

public class BookController extends AsyncTask<String, Void, Boolean> {
    AppDatabase db;
    Book book;
    String isbn;
    HashMap<BookData,String> bookDataMap;
    String[] genres;
    String[] reviews;
    String[] authors;

    Context context;

    public BookController(Context context) {
        this.context = context;
    }

    //Checks in room database to see if book exists
    public boolean checkIfBookExistsInDatabase(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        isbn = getBookIsbnFromSharedPreferneces(context);
        book = db.bookDao().getBook(isbn);
        return !(book == null);
    }

    //Store the book isbn in a shared preference file
    public boolean storeBookIsbn(Context context, String isbn) {
        try {
            SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("BookPref", Context.MODE_PRIVATE).edit();
            editor.putString("isbn", isbn).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }

    //Retrieve the isbn from the shared preference file
    public String getBookIsbnFromSharedPreferneces(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("BookPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("isbn", "default");
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try{
            BookReviewFragment bookReviewFragment = new BookReviewFragment();
            ErrorFragment errorFragment = new ErrorFragment();
            boolean bookInformationreceived = receiveBookInformation(strings);
            if (bookInformationreceived == true){
                FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, bookReviewFragment).commit();

            } else{
                FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, errorFragment).commit();
            }
            return true;
        } catch (Exception e){

            return false;
        }

    }

    //Credit to  //Code from https://systembash.com/a-simple-java-tcp-server-and-tcp-client/ and
    // Rodolk from https://stackoverflow.com/questions/19839172/how-to-read-all-of-inputstream-in-server-socket-java for the TCP client implementation
    public boolean receiveBookInformation(String... strings){
        byte[] messageByte = new byte[1000];
        boolean end = false;
        String bookDataString = "";
        String fromReceiver = "";
        try{
            String data;
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("192.168.1.252",9876);
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            //Send isbn number to the server
            outToServer.writeBytes(isbn + "#");

            //Receive the book data from the server
            while(!end){
                int bytesRead = in.read(messageByte);
                bookDataString += new String(messageByte, 0, bytesRead);
                if(bookDataString.length()> 0){
                    end = true;
                }

            }
            //reset the end boolean value and dataString value
            end = false;

            //Send a FIN message to the server to tell it that it is ready to close connection
            outToServer.writeBytes("FIN");
            while(!end){
                int bytesRead = in.read(messageByte);
                fromReceiver += new String(messageByte, 0, bytesRead);

                //Checks to see if the Server acknowledged(ACK) and also it is ready to close connection (FIN)
                if(fromReceiver.length()> 0 && fromReceiver.contains("FIN") && fromReceiver.contains("ACK")){
                    outToServer.writeBytes("ACK");

                }
                //Check to see if the server has proceeded to close its connection from its side
                if(fromReceiver.length()>0 && fromReceiver.contains("CLOSED")){
                    end = true;
                }
            }
            clientSocket.close();
            bookDataMap = EncodeBookData(bookDataString);
            genres = SplitStringsAndPutIntoList(bookDataMap.get(BookData.Genres));
            authors = SplitStringsAndPutIntoList(bookDataMap.get(BookData.Authors));
            reviews = SplitStringsAndPutIntoList(bookDataMap.get(BookData.AmazonReviews));
            boolean bookInformationSaved = saveBookInformation(bookDataMap, authors, genres, reviews);
            return bookInformationSaved;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }



    public boolean saveBookInformation(HashMap<BookData,String> bookDataMap, String[] authors,String[] genres, String[] reviews){
        try {

            Helper helper = new Helper();
            RatingController ratingController = new RatingController();
            String isbnNumber = helper.checkIfBookDataAttributeNull(bookDataMap.get(BookData.Isbn));
            String title = helper.checkIfBookDataAttributeNull(bookDataMap.get(BookData.Title));
            String subtitle = helper.checkIfBookDataAttributeNull(bookDataMap.get(BookData.Subtitle));
            int pageCount = helper.convertStringToInt(bookDataMap.get(BookData.PageCount));
            String summary = helper.checkIfBookDataAttributeNull(bookDataMap.get(BookData.Description));
            String thumbnailUrl = helper.checkIfBookDataAttributeNull(bookDataMap.get(BookData.ThumbnailUrl));
            double amazonAverageRating = helper.convertStringToDouble(bookDataMap.get(BookData.AmazonAverageRating));
            double googleBooksAverageRating = helper.convertStringToDouble(bookDataMap.get(BookData.GoogleBooksAverageRating));
            double goodreadsAverageRating = helper.convertStringToDouble(bookDataMap.get(BookData.GoodreadsAverageRating));
            int amazonFiveStarRatingPercentage = helper.convertStringToInt(bookDataMap.get(BookData.AmazonFiveStarRatingPercentage));
            int amazonFourStarRatingPercentage = helper.convertStringToInt(bookDataMap.get(BookData.AmazonFourStarRatingPercentage));
            int amazonThreeStarRatingPercentage = helper.convertStringToInt(bookDataMap.get(BookData.AmazonThreeStarRatingPercentage));
            int amazonTwoStarRatingPercentage = helper.convertStringToInt(bookDataMap.get(BookData.AmazonTwoStarRatingPercentage));
            int amazonOneStarRatingPercentage = helper.convertStringToInt(bookDataMap.get(BookData.AmazonOneStarRatingPercentage));
            int amazonReviewsCount = helper.convertStringToInt(bookDataMap.get(BookData.AmazonReviewsCount));
            HashMap<BookData, Double> averageRatingMap = new HashMap<>();

            //db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();

            db.bookDao().insertBook(new Book(0, isbnNumber, title, subtitle, pageCount, summary, thumbnailUrl, 0));

            int bookId = db.bookDao().getBook(isbnNumber).bookId;

            int authorId;
            for (String author : authors) {
                db.authorDao().insertAuthor(new Author(0, author));
                authorId = db.authorDao().getAuthorId(author);
                db.bookAuthorDao().insertBookAuthor(new BookAuthor(0, bookId, authorId));
            }

            int genreId;
            for (String genre : genres) {
                db.genreDao().insertGenre(new Genre(0, genre));
                genreId = db.genreDao().getGenreId(genre);
                db.bookGenreDao().insertBookGenre(new BookGenre(0, bookId, genreId));
            }


            averageRatingMap.put(BookData.AmazonAverageRating, amazonAverageRating);
            averageRatingMap.put(BookData.GoogleBooksAverageRating, googleBooksAverageRating);
            averageRatingMap.put(BookData.GoodreadsAverageRating, goodreadsAverageRating);
            double overallAverageRating = ratingController.computeOverallAverageRating(averageRatingMap);
            db.ratingDao().insertRatings(new Rating(0, bookId, overallAverageRating, amazonAverageRating, googleBooksAverageRating, goodreadsAverageRating,
                    amazonFiveStarRatingPercentage, amazonFourStarRatingPercentage, amazonThreeStarRatingPercentage, amazonTwoStarRatingPercentage, amazonOneStarRatingPercentage,
                    amazonReviewsCount));

            for (String review : reviews) {
                if(review != "")
                    db.reviewDao().insertReview(new Review(0, bookId, review));
            }

            return true;
        } catch(Exception e){
            e.printStackTrace();
            String errorMsg = e.getMessage();
            return false;
        }
    }

    public void displayBookInformation(View view, FragmentManager fragmentManager, Toolbar toolbar){
        try {
            db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
            String isbn = getBookIsbnFromSharedPreferneces(context);
            Book book = db.bookDao().getBook(isbn);
            toolbar.setTitle(book.getTitle());
            List<Integer> genreIds = db.bookGenreDao().getGenreIdsOfBook(book.bookId);
            List<String> genreLabels = new ArrayList<>();
            for (int i = 0; i < genreIds.size(); i++) {
                genreLabels.add(db.genreDao().getGenreLabel(genreIds.get(i)));
            }
            List<Integer> authorIds = db.bookAuthorDao().getAuthorIdsOfBook(book.bookId);
            List<String> authorNames = new ArrayList<>();
            for (int i = 0; i < authorIds.size(); i++) {
                authorNames.add(db.authorDao().getAuthorName(authorIds.get(i)));
            }

            Rating ratings = db.ratingDao().getRating(book.bookId);
            ImageView imageview_bookthumbnail = view.findViewById(R.id.imageview_bookthumbnail);
            TextView textview_book_subtitle = view.findViewById(R.id.textview_book_subtitle);
            TextView textview_book_isbn = view.findViewById(R.id.textview_book_isbn);
            TextView textview_genres = view.findViewById(R.id.textview_genres);
            TextView textview_authors = view.findViewById(R.id.textview_authors);
            RatingBar ratingbar_overall_rating = view.findViewById(R.id.ratingbar_overallrating);

            if (!book.getThumbnailUrl().matches("")) {
                //Credit to the creators of Picasso at https://square.github.io/picasso/
                Picasso.with(context).load(book.getThumbnailUrl()).into(imageview_bookthumbnail);
            }

            if (book.getSubtitle().matches("")) {
                textview_book_subtitle.setText("No subtitle");
            } else {
                textview_book_subtitle.setText(book.getSubtitle());
            }
            textview_book_isbn.setText(book.getIsbnNumber());
            String genres = "";
            for (int i = 0; i < genreLabels.size(); i++) {
                if (i == (genreLabels.size() - 1) && genreLabels.get(i) != null) {
                    genres += genreLabels.get(i);
                } else if (genreLabels.get(i) != null) {
                    genres += (genreLabels.get(i) + ", ");
                }
            }
            if (genres.length() == 0) {
                textview_genres.setText("Unavailable");
            } else {
                textview_genres.setText(genres);
            }

            String authors = "";
            for (int i = 0; i < authorNames.size(); i++) {
                if (i == (authorNames.size() - 1) && authorNames.get(i) != null) {
                    authors += authorNames.get(i);
                } else if (authorNames.get(i) != null) {
                    authors += (authorNames.get(i) + ", ");
                }
            }
            if (authors.length() == 0) {
                textview_authors.setText("Unavailable");
            } else {
                textview_authors.setText(authors);
            }
            ratingbar_overall_rating.setRating((float) ratings.getOverallAverageRating());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void displayBookInformationInBriefSummaryTab(View view){
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        TextView textview_book_summary = view.findViewById(R.id.textview_book_summary);
        String isbn = getBookIsbnFromSharedPreferneces(context);
        Book book = db.bookDao().getBook(isbn);
        if(book.getSummary().length() == 0){
            textview_book_summary.setText("Unavailable");
        } else {
            textview_book_summary.setText(book.getSummary());
        }
    }

    public void displayBookInformationInReviewsTab(View view){
        MyComparator myComparator = new MyComparator();
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        String isbn = getBookIsbnFromSharedPreferneces(context);
        Book book = db.bookDao().getBook(isbn);
        TextView textview_reviews = view.findViewById(R.id.textview_reviews);
        List<Review> reviews = db.reviewDao().getReviews(book.bookId);
        Collections.sort(reviews, myComparator);
        String reviewsString = "";

        //Display the first 4 reviews
        for(int i =0; i<reviews.size(); i++){

            if (i == (reviews.size() - 1) && i<5 && !reviews.get(i).getReview().matches("")) {
                reviewsString += ("\"" + reviews.get(i).getReview() + "\"");
            } else if(i<5 && !reviews.get(i).getReview().matches("")){
                reviewsString += ("\"" + reviews.get(i).getReview() + "\"\n\n");
            }

        } if(reviewsString.matches("")){
            textview_reviews.setText("Unavailable");
        } else {
            textview_reviews.setText(reviewsString);
        }
    }

    public void displayBookInformationInRatingTab(View view){
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        String isbn = getBookIsbnFromSharedPreferneces(context);
        Book book = db.bookDao().getBook(isbn);
        Rating ratings = db.ratingDao().getRating(book.getBookId());

        TextView textview_amazon_rating_breakdown_title = view.findViewById(R.id.textview_amazonratingbreakdown_title);
        RatingBar ratingbar_amazon_average_rating = view.findViewById(R.id.ratingbar_amazon_average_rating);
        RatingBar ratingbar_googlebooks_average_rating = view.findViewById(R.id.ratingbar_googlebooks_average_rating);
        RatingBar ratingbar_goodreads_average_rating = view.findViewById(R.id.ratingbar_goodreads_average_rating);
        TextView textview_four_to_five_star_rating_percentage = view.findViewById(R.id.textview_fourtofivestarratingpercentage);
        TextView textview_one_star_rating_percentage = view.findViewById(R.id.textview_onestarratingpercentage);

        ratingbar_amazon_average_rating.setRating((float)ratings.getAmazonAverageRating());
        ratingbar_googlebooks_average_rating.setRating((float)ratings.getGoogleBooksAverageRating());
        ratingbar_goodreads_average_rating.setRating((float)ratings.getGoodreadsAverageRating());

        if(ratings.getAmazonReviewsCount()==0){
            textview_amazon_rating_breakdown_title.setText("Amazon rating breakdown");
        } else{
            textview_amazon_rating_breakdown_title.setText("Amazon rating breakdown ("+ ratings.getAmazonReviewsCount() +" reviews)");
        }
        int fourToFiveStarRatingPercentage = ratings.getAmazonFiveStarRatingPercentage() + ratings.getAmazonFourStarRatingPercentage();
        if(fourToFiveStarRatingPercentage == 0){
            textview_four_to_five_star_rating_percentage.setText("Unavailable");
        } else{
            textview_four_to_five_star_rating_percentage.setText(fourToFiveStarRatingPercentage+"%");
        }
        if(ratings.getAmazonOneStarRatingPercentage() == 0){
            textview_one_star_rating_percentage.setText("Unavailable");
        } else{
            textview_one_star_rating_percentage.setText(ratings.getAmazonOneStarRatingPercentage()+ "%");
        }

    }





    //If the current string built by the string builder is "*" then that means that the book source returned no data for that specific field.
    //Hence why I want to replace it with ""
    public HashMap<BookData,String> EncodeBookData(String bookData) {
        char[] chars  = bookData.toCharArray();
        HashMap<BookData,String> bookDataMap = new HashMap<BookData,String>();
        StringBuilder sb = new StringBuilder();
        int bookDataIndex = 0;
        for(int i =0; i<chars.length; i++) {

            if(chars[i] == '$') {
                bookDataIndex++;
                switch(bookDataIndex){
                    case 1:
                        bookDataMap.put(BookData.Title, sb.toString());
                    case 2:
                        bookDataMap.put(BookData.Authors, sb.toString());
                    case 3:
                        bookDataMap.put(BookData.Description, sb.toString());
                    case 4:
                        bookDataMap.put(BookData.Subtitle, sb.toString());
                    case 5:
                        bookDataMap.put(BookData.Isbn, sb.toString());
                    case 6:
                        bookDataMap.put(BookData.GoogleBooksAverageRating, sb.toString());
                    case 7:
                        bookDataMap.put(BookData.GoodreadsAverageRating, sb.toString());
                    case 8:
                        bookDataMap.put(BookData.AmazonAverageRating, sb.toString());
                    case 9:
                        bookDataMap.put(BookData.AmazonFiveStarRatingPercentage, sb.toString());
                    case 10:
                        bookDataMap.put(BookData.AmazonFourStarRatingPercentage, sb.toString());
                    case 11:
                        bookDataMap.put(BookData.AmazonThreeStarRatingPercentage, sb.toString());
                    case 12:
                        bookDataMap.put(BookData.AmazonTwoStarRatingPercentage, sb.toString());
                    case 13:
                        bookDataMap.put(BookData.AmazonOneStarRatingPercentage, sb.toString());
                    case 14:
                        bookDataMap.put(BookData.AmazonReviews, sb.toString());
                    case 15:
                        bookDataMap.put(BookData.AmazonReviewsCount, sb.toString());
                    case 16:
                        bookDataMap.put(BookData.Genres, sb.toString());
                    case 17:
                        bookDataMap.put(BookData.PageCount, sb.toString());
                    case 18:
                        bookDataMap.put(BookData.ThumbnailUrl, sb.toString());
                }
                sb.setLength(0);

            }
            else {
                sb.append(chars[i]);

            }

        }

        return bookDataMap;
    }

    public static String[] SplitStringsAndPutIntoList(String str){
        String[] stringArray = str.split("#",100);

        return stringArray;
    }

    public enum BookData {
        Title,
        Authors,
        Description,
        Subtitle,
        Isbn,
        GoogleBooksAverageRating,
        GoodreadsAverageRating,
        AmazonAverageRating,
        AmazonFiveStarRatingPercentage,
        AmazonFourStarRatingPercentage,
        AmazonThreeStarRatingPercentage,
        AmazonTwoStarRatingPercentage,
        AmazonOneStarRatingPercentage,
        AmazonReviews,
        AmazonReviewsCount,
        Genres,
        PageCount,
        ThumbnailUrl
    }

    public void likeBook(){
        UserController userController = new UserController();
        int userId = userController.getUserIdFromSharedPreferneces(context);
        String isbn = getBookIsbnFromSharedPreferneces(context);
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        int bookId = db.bookDao().getBook(isbn).getBookId();

        Audit audit = db.auditDao().getAudit(userId,bookId);
        if (audit != null){
            //Update Status record
            updateStatus(audit.getAuditId(), BookStatus.Like, db);

        } else{
            //Create new Audit and status record
            createAudit(userId, bookId, BookStatus.Like, db);
        }

    }
    public void updateStatus(int auditId,BookStatus bookStatus, AppDatabase db){
        String newStatus = bookStatus.toString();
        db.statusDao().updateStatusStatus(newStatus, auditId);
        db.statusDao().updateStatusReason("",auditId);
    }
    public void createAudit(int userId, int bookId, BookStatus bookStatus, AppDatabase db){
        db.auditDao().insertAudit(new Audit(0, userId, bookId));
        int auditId = db.auditDao().getAudit(userId,bookId).getAuditId();
        createStatus(auditId, bookStatus, db);
    }

    public void createStatus(int auditId, BookStatus bookStatus, AppDatabase db){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String status = bookStatus.toString();
        db.statusDao().insertStatus(new entities.Status(0,auditId,status,timeStamp,""));
    }


    public enum BookStatus{
        Like, Dislike, ReviewLater
    }
}
