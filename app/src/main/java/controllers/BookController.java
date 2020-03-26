package controllers;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koobookandroidapp.R;
import com.squareup.picasso.Callback;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dataaccess.setup.AppDatabase;
import entities.Audit;
import entities.Author;
import entities.Book;
import entities.BookAuthor;
import entities.BookGenre;
import entities.Color;
import entities.Genre;
import entities.Rating;
import entities.Review;
import extras.Helper;
import extras.MyComparator;
import fragments.BookReviewFragment;
import fragments.ErrorFragment;
import fragments.SearchBookResultsFragment;

import static extras.Helper.splitStringsAndPutIntoList;

public class BookController extends AsyncTask<String, Void, Boolean> {
    UserController userController;
    Helper helper;
    AppDatabase db;
    Book book;
    String isbn;
    HashMap<BookData,String> bookDataMap;
    String[] genres;
    String[] reviews;
    String[] authors;
    List<Book> books;
    List<Integer> auditIds;
    String reasonForDislikingBook;
    Context context;
    String data;
    boolean isMoreThanOneBook;
    ProgressDialog progressDialog;
    boolean bookInformationreceived;

    public BookController(Context context) {
        this.context = context;
        isMoreThanOneBook = false;
        books = new ArrayList<>();
        auditIds = new ArrayList<>();
        db = Room.databaseBuilder(context, AppDatabase.class, "production").allowMainThreadQueries().build();
        userController = new UserController();
        helper = new Helper();
    }



    //Checks in room database to see if book exists using the stored isbn
    public boolean checkIfBookExistsInDatabaseUsingIsbn(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        isbn = getBookIsbnFromSharedPreferences(context);
        book = db.bookDao().getBookBasedOnIsbnNumber(isbn);
        data = "!isMoreThanOneBook"+isbn;
        return !(book == null);
    }

    //Checks in room database to see if book exists using the author and title
    //If the book does exist then store the isbn in the preference file as this will be later used to display the rleevent book information into the "Book Review" fragment
    public boolean checkIfBookExistsInDatabaseUsingTitleAndAuthor(Context context, String title, String author) {
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        //The reason why the return type is a list of book is becuase they may be more than one book that contains the title
        List<Book> books = db.bookDao().getBookBasedOnTitle("%"+title.toLowerCase()+"%");
        if(books == null){
            return false;
        } else{
            book= checkIfAuthorTypedByUserMatchesAuthorAffiliatedWithBook(books, author);
            if(book == null){
                return false;
            } else{
                storeBookIsbn(context, book.getIsbnNumber());
                return true;
            }
        }

    }

    //This check among the list of books to see if one of the books contains the author typed by the user which is one of the methods arguments,
    // if so then it will return that Book entity, otherwise it will return null. The method works by iterating through each book in the list of books. For each book, it then gets
    //the author Ids based on the books Id. Then for each author Id, it then uses it to get the authors name and this is used to check whether it matches the author typed by the user.
    //if it does then set the index to index value of the book currently being iterated in the loop and then break. Otherwise it continunes to loop through the other books it corresponding authors
    //After exiting the outer for loop block, the index is used to check whether it equals the default. If it does then this means that the book with the author was not found and the method Returns null.
    //Otherwise, the index is used to get the book from the book list and this is returned.
    public Book checkIfAuthorTypedByUserMatchesAuthorAffiliatedWithBook(List<Book> books, String author){
        int index = 0;
        String authorName= "";
        for(int i =1 ; i<books.size()+1; i++){
            int x = i-1;
            List<Integer> authorIds = db.bookAuthorDao().getAuthorIdsOfBook(books.get(x).getBookId());
            for(int authorId: authorIds){
                authorName = db.authorDao().getAuthorName(authorId);
                if(author.matches(authorName) || author.matches(authorName.toLowerCase())){
                    index =  x;
                    break;
                }
            }

        }
        if(index == 0){
            return null;
        } else{
            return books.get(index);
        }

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
    public String getBookIsbnFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("BookPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("isbn", "");
    }

    //Store the data string, containing all the brief information about the 10 books, in a shared preference file
    public boolean storeBooksDataString(Context context, String booksDataString) {
        try {
            SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("BooksDataPref", Context.MODE_PRIVATE).edit();
            editor.putString("booksData", booksDataString).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Retrieve the data string, containing all the brief information about the 10 books,  from the shared preference file
    public String getBooksDataStringFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("BooksDataPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("booksData", "");
    }

    //Store the type of books(Liked books or books that needs reviewing),the user wants to view, in a shared preference file
    public boolean storeBookListType(Context context, BookListType bookListType) {
        try {
            SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("BookListTypePref", Context.MODE_PRIVATE).edit();
            editor.putString("bookListType", bookListType.toString()).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Retrieve the book list type from the shared preference file
    public String getBookListTypeFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("BookListTypePref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("bookListType", "default");
    }

    //Load the progress dialog to keep the user informed that the book information is being retrieved
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Fetching information about book", "Please wait", false, false);
    }

    //Close the progress dialog and load the relevent fragment. If the flag ,which records whether the receives information is for only one book, Returns true,
    // then check the If the flag for retieving the book information is false. If so then load the Error message to inform the user
    //that something went wrong during the process for collecting the book information. Otherwise, the "Book Review" will be loaded with the retrieved information being displayed
    //If the flag indciated that the received information was for more than one book then load the "Search results" fragment where the information will be loaded into the
    //recycler view.
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        BookReviewFragment bookReviewFragment = new BookReviewFragment();
        ErrorFragment errorFragment = new ErrorFragment();
        SearchBookResultsFragment searchBookResultsFragment =  new SearchBookResultsFragment();
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        super.onPostExecute(aBoolean);
        try {
            TimeUnit.SECONDS.sleep(2);
        }catch (Exception e){
            
        }
        progressDialog.dismiss();

        if(isMoreThanOneBook == false){

            if (bookInformationreceived == true){
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, bookReviewFragment).commit();

            } else{
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, errorFragment).commit();
            }
        } else{
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, searchBookResultsFragment).commit();
        }
    }

    //Collect the book information
    @Override
    protected Boolean doInBackground(String... strings) {
        try{

            bookInformationreceived = receiveBookInformation(strings);
            return true;
        } catch (Exception e){

            return false;
        }

    }

    //Credit to  //Code from https://systembash.com/a-simple-java-tcp-server-and-tcp-client/ and
    // Rodolk from https://stackoverflow.com/questions/19839172/how-to-read-all-of-inputstream-in-server-socket-java for the TCP client implementation
    //This method works by first connecting to the server using a TCP socket. After the connection has been established, this method will sent the data which may contain
    //the isbn, author, title or all of them along with the boolean flag value for isMoreThanOneBook to tell the server what it needs to do. After sending this data, it then
    //send a "#" symbol which tells the server that it has received the entire string. Note that the server was configure to contunite reading from the stream until this symbol is received.
    //After the server sends back the information about the book(s), the partial handshake occurs for the client and server to close connection. After closing the connection,
    //the recieved data is then formatted and depending on whether the data is for one or more books. A different workflow occurs which is outlined in the method
    public boolean receiveBookInformation(String... strings){
        byte[] messageByte = new byte[1000];
        boolean end = false;
        boolean success = false;
        String receivedDataString = "";
        String fromReceiver = "";
        try{
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("192.168.1.252",9876);
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            //Send data(isbn or title+author to the server
            outToServer.writeBytes(data + "#");

            //Receive the book data from the server
            while(!end){
                int bytesRead = in.read(messageByte);
                receivedDataString += new String(messageByte, 0, bytesRead);
                if(receivedDataString.contains("]d2C>^+")){
                    end = true;
                }

            }

            receivedDataString = receivedDataString.replace("]d2C>^+","");
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

            //If it the retrieved data is for only one book get then get the data attribute values from the data string and then use it store it into a hashmap.
            //Next, as the data attributes for genres, author and reviews are concatanated together, properly format these such that it is in string list. Using the hashmap and string lists, use it to save it into the room database
            //If the recieved data is for more than one book then store the data string into a shared preference file such that it can later separate the books information and put into a book list to be passed into the BookListAdapter
            if(isMoreThanOneBook == false){
                String formattedDataString = receivedDataString.replace("#","").replace("$","").replace("*","");
                if(!formattedDataString.matches("")) {
                    bookDataMap = decodeBookData(receivedDataString);

                    //If the user searched for the book without entering the isbn then we need to save the retireved Isbn
                    storeBookIsbn(context, bookDataMap.get(BookData.Isbn));

                    //A final check to see if the book already corresponds to a book record in the Room database
                    if (checkIfBookExistsInDatabaseUsingIsbn(context) == false) {
                        //As the data attribute values for Genres, Author, Amazon are currently in form of "item1#item2#item3", split these by the # and create a string list
                        genres = splitStringsAndPutIntoList(bookDataMap.get(BookData.Genres));
                        authors = splitStringsAndPutIntoList(bookDataMap.get(BookData.Authors));
                        reviews = splitStringsAndPutIntoList(bookDataMap.get(BookData.AmazonReviews));
                        saveBookInformation(bookDataMap, authors, genres, reviews);
                        success = true;
                    }
                } else{
                    success = false;
                }
            } else{
                storeBooksDataString(context, receivedDataString);
                success = true;

            }


        }
        //If an exception is thrown then still try to format and save the book information
        catch (Exception e){

            String formattedDataString = receivedDataString.replace("#","").replace("$","").replace("*","");
            if(!formattedDataString.matches("")){
                //If it the retrieved data is for only one book then save it
                //Otherwise, seperate the books information and put into a book list to passed into the BookListAdapter
                if(isMoreThanOneBook == false){
                    bookDataMap = decodeBookData(receivedDataString);

                    //If the user searched for the book without entering the isbn then we need to save the retrieved Isbn
                    storeBookIsbn(context,bookDataMap.get(BookData.Isbn));

                    //A final check to see if the book already corresponds to a book record in the Room database
                    if(checkIfBookExistsInDatabaseUsingIsbn(context) == false){
                        genres = splitStringsAndPutIntoList(bookDataMap.get(BookData.Genres));
                        authors = splitStringsAndPutIntoList(bookDataMap.get(BookData.Authors));
                        reviews = splitStringsAndPutIntoList(bookDataMap.get(BookData.AmazonReviews));
                        saveBookInformation(bookDataMap, authors, genres, reviews);
                        success = true;
                    }
                } else{
                    storeBooksDataString(context, receivedDataString);
                    success = true;

                }

                return true;

            } else {
                e.printStackTrace();
                success = false;
            }
        }
        return success;
    }

    //This method will retrieve the data string, containing all the brief information of 10 books, and use it to split such that we have the data string for each book.
    //For each data string, the data attributes will be extracted from it and stored into its a hashmap
    //Then each hashmap is used to create a new Book entity instance which is then added to the list of books which is what is returned
    public List<Book> getBooksFromBooksDataString(){
        String booksDataString = getBooksDataStringFromSharedPreferences(context);
        if(!booksDataString.matches("")) {

            List<HashMap<BookData, String>> bookDataMaps = decodeBooksData(booksDataString);

            for (HashMap<BookData, String> bookDataMap : bookDataMaps) {
                String[] authors = splitStringsAndPutIntoList(bookDataMap.get(BookData.Authors));
                String formatetedAuthor ="";
                //Format the author text to be displayed in the recycler view. This was neccessary I didn't want to display all authors of the book as it may result
                //in inconsistencies regarding how each book is displayed in the recycler view.
                if(authors.length >1){
                    formatetedAuthor = authors[0] + " and " + (authors.length-1) +" more";
                } else{
                    formatetedAuthor = authors[0];
                }
                if(!(formatetedAuthor.length()>2)){
                    formatetedAuthor = "Author(s) unavailable";
                }

                //I stored the authors into the subtitle attribute of the book such that I can easily bind it from the Search results adpater class, note that this list will not be stored in the Room database
                Book book = new Book(0, bookDataMap.get(BookData.Isbn), bookDataMap.get(BookData.Title), formatetedAuthor, 0, "", bookDataMap.get(BookData.ThumbnailUrl), 0);

                //Check if book has already been seen by user
                Book duplicateBook = db.bookDao().getBookBasedOnIsbnNumber(book.getIsbnNumber());
                boolean bookExistInRoomDatabase = (duplicateBook != null);

                if(bookExistInRoomDatabase != true){
                    books.add(book);
                } else{
                    int userId = userController.getUserIdFromSharedPreferneces(context);
                    int bookId = duplicateBook.getBookId();
                    boolean userSeenBook = (db.auditBookDao().getAudit(userId,bookId) != null);
                    if(userSeenBook != true){
                        books.add(book);
                    }
                }

            }
        } return books;
    }


    //This method extracts all the required information from each of the methods arugments and this is used to store into the tables in the Room database
    public boolean saveBookInformation(HashMap<BookData,String> bookDataMap, String[] authors,String[] genres, String[] reviews){
        try {
            Helper helper = new Helper();
            RatingController ratingController = new RatingController();
            //Extract data attributes into its own variable
            String isbnNumber = helper.checkIfBookDataAttributeNull(bookDataMap.get(BookData.Isbn));
            String title = helper.checkIfBookDataAttributeNull(bookDataMap.get(BookData.Title));
            String subtitle = helper.checkIfBookDataAttributeNull(bookDataMap.get(BookData.Subtitle));
            int pageCount = helper.convertStringToInt(bookDataMap.get(BookData.PageCount));
            String summary = helper.checkIfBookDataAttributeNull(bookDataMap.get(BookData.Description));
            String thumbnailUrl = helper.checkIfBookDataAttributeNull(bookDataMap.get(BookData.ThumbnailUrl));
            String dominantColor = helper.checkIfBookDataAttributeNull(bookDataMap.get(BookData.DominantColor));
            double amazonAverageRating = helper.convertStringToDouble(bookDataMap.get(BookData.AmazonAverageRating));
            double googleBooksAverageRating = helper.convertStringToDouble(bookDataMap.get(BookData.GoogleBooksAverageRating));
            double goodreadsAverageRating = helper.convertStringToDouble(bookDataMap.get(BookData.GoodreadsAverageRating));
            int amazonFiveStarRatingPercentage = helper.convertStringToInt(bookDataMap.get(BookData.AmazonFiveStarRatingPercentage));
            int amazonFourStarRatingPercentage = helper.convertStringToInt(bookDataMap.get(BookData.AmazonFourStarRatingPercentage));
            int amazonThreeStarRatingPercentage = helper.convertStringToInt(bookDataMap.get(BookData.AmazonThreeStarRatingPercentage));
            int amazonTwoStarRatingPercentage = helper.convertStringToInt(bookDataMap.get(BookData.AmazonTwoStarRatingPercentage));
            int amazonOneStarRatingPercentage = helper.convertStringToInt(bookDataMap.get(BookData.AmazonOneStarRatingPercentage));
            int amazonReviewsCount = helper.convertStringToInt(bookDataMap.get(BookData.AmazonReviewsCount));
            String formattedGenre;

            HashMap<BookData, Double> averageRatingMap = new HashMap<>();

            //Insert book into room database
            db.bookDao().insertBook(new Book(0, isbnNumber, title, subtitle, pageCount, summary, thumbnailUrl, 0));
            int bookId = db.bookDao().getBookBasedOnIsbnNumber(isbnNumber).bookId;

            //Use book id to insert new record in Color table
            db.colorDao().insertColor(new Color(0, bookId,"",dominantColor));

            //Use book id from previously stored book record and use it to iterate through each author and insert a Author row into the room database
            int authorId;
            for (String author : authors) {
                authorId = db.authorDao().getAuthorId(author);

                //If author id is equal to a number then this implies that the author already exists in the room database and therefore skip the step for inserting a new
                //author record
                if(authorId == 0){
                    db.authorDao().insertAuthor(new Author(0, author));
                    authorId = db.authorDao().getAuthorId(author);
                }

                //Author Id from previously stored author record and use it along with the Book Id to insert a new BookAuthor row into the room database
                db.bookAuthorDao().insertBookAuthor(new BookAuthor(0, bookId, authorId));
            }

            int genreId;
            for (String genre : genres) {
                //Some of the genres retrieved from the google books api may contains only numbers
                formattedGenre = genre.replace("[^A-Za-z]","");
                //If the formated genre string contains nothing then that implies that the genre only contained numbers
                //Otherwise, genre is checked to see if the it already exists in the Genre table, if it does then skip the step for inserting genre. Finally,
                //insert a new row in the BookGenre table using the book id and the genre id
                if(!formattedGenre.matches("")){
                    genreId = db.genreDao().getGenreId(genre);
                    if(genreId ==0){
                        db.genreDao().insertGenre(new Genre(0, genre));
                        genreId = db.genreDao().getGenreId(genre);
                    }
                    db.bookGenreDao().insertBookGenre(new BookGenre(0, bookId, genreId));
                }
            }

            //Put the average rating for Amazon, Google books and Goodreads into the hashmap
            averageRatingMap.put(BookData.AmazonAverageRating, amazonAverageRating);
            averageRatingMap.put(BookData.GoogleBooksAverageRating, googleBooksAverageRating);
            averageRatingMap.put(BookData.GoodreadsAverageRating, goodreadsAverageRating);
            //Get the overall average rating and use this along with the indivdual rating to insert a new row in the Rating table in the room database
            double overallAverageRating = ratingController.computeOverallAverageRating(averageRatingMap);
            db.ratingDao().insertRatings(new Rating(0, bookId, overallAverageRating, amazonAverageRating, googleBooksAverageRating, goodreadsAverageRating,
                    amazonFiveStarRatingPercentage, amazonFourStarRatingPercentage, amazonThreeStarRatingPercentage, amazonTwoStarRatingPercentage, amazonOneStarRatingPercentage,
                    amazonReviewsCount));

            //Iterate through each review and use the book Id to insert a new row in the Review table in the room database
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

    //In the top half of the "Book review" page(excluding the tabs), all the book information will be displayed into the relevent text views which
    //were accessed using the View was is the argument this this method.
    public void displayBookInformationInBookReviewPage(View view, TextView textview_toolbar_title){
        try {
            Book book = getBookUsingIsbnFromSharedPreferences();
            //If the title is long then only display the first five words from it to be displayed on the screen
            if(book.getTitle().split(" ").length>5){
                String trimmedTitle = "";
                String[] initalWords = book.getTitle().split(" ");
                for(int i=0; i<6; i++){
                    if(i == 5) {
                        trimmedTitle += initalWords[i];
                    } else{
                        trimmedTitle += initalWords[i] + " ";
                    }
                }
                trimmedTitle+= "...";
                textview_toolbar_title.setText(trimmedTitle);
            } else{
                textview_toolbar_title.setText(book.getTitle());
            }
            //Use the book id to get the genre ids from the room database
            //then for each genre id, get the genre label and add it to the list of genres
            List<Integer> genreIds = db.bookGenreDao().getGenreIdsOfBook(book.bookId);
            List<String> genreLabels = new ArrayList<>();
            for (int i = 0; i < genreIds.size(); i++) {
                genreLabels.add(db.genreDao().getGenreLabel(genreIds.get(i)));
            }

            //Use the book id to get the author ids from the room database
            //then for each author id, get the author's name and add it to the list of author names
            List<Integer> authorIds = db.bookAuthorDao().getAuthorIdsOfBook(book.bookId);
            List<String> authorNames = new ArrayList<>();
            for (int i = 0; i < authorIds.size(); i++) {
                String authorName = db.authorDao().getAuthorName(authorIds.get(i));

                if(!authorName.matches("")){
                authorNames.add(authorName);
                }
            }
            Rating ratings = db.ratingDao().getRating(book.bookId);
            ImageView imageview_bookthumbnail = view.findViewById(R.id.imageview_bookthumbnail);
            TextView textview_book_subtitle = view.findViewById(R.id.textview_book_subtitle);
            TextView textview_book_isbn = view.findViewById(R.id.textview_book_isbn);
            TextView textview_genres = view.findViewById(R.id.textview_genres);
            TextView textview_authors = view.findViewById(R.id.textview_authors);
            RatingBar ratingbar_overall_rating = view.findViewById(R.id.ratingbar_overallrating);
            TextView textview_overall_rating = view.findViewById(R.id.textview_overall_average_rating);

            //If the data attribute for thumbnail url is non-empty, use Picasso to download the image using the url and
            //load it into the image view. Otherwise, leave the image view to display the default "no image" image
            if (!book.getThumbnailUrl().matches("")) {
                //Credit to the creators of Picasso at https://square.github.io/picasso/ and Mahen from https://stackoverflow.com/questions/44113180/android-picasso-is-not-loading-image-when-i-use-fit for this solution
                Picasso.with(context).load(book.getThumbnailUrl()).resize(300,450).centerInside().into(imageview_bookthumbnail, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i("Picasso", "onSuccess: TRUE");
                    }

                    @Override
                    public void onError() {
                        Log.i("Picasso", "onError: TRUE");
                    }
                });
            }

            if (book.getSubtitle().matches("")) {
                textview_book_subtitle.setText("No subtitle");
            } else {
                textview_book_subtitle.setText(book.getSubtitle());
            }
            textview_book_isbn.setText(book.getIsbnNumber());
            String genres = "";
            String formattedGenres;
            //Interated through each genre and add it to the string which will be used for storing all the genres which
            //are separated by commas. Before using this string, it is then formatted to remove all numbers as
            //there was a bug where some of the genre labels were just numbers
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
                //Credit to Gian from https://stackoverflow.com/questions/17516049/java-removing-numeric-values-from-string the solution for removing numeric values from a string
                formattedGenres = genres.replaceAll("[^A-Za-z]","");
                if(!formattedGenres.matches("")){
                    textview_genres.setText(genres);
                } else{
                    textview_genres.setText("Unavailable");
                }

            }

            //Interated through each author and added it to the string which will be used for storing all the authors names which
            //are separated by commas.
            String authors = "";
            for (int i = 0; i < authorNames.size(); i++) {
                if (i == (authorNames.size() - 1)) {
                    authors += authorNames.get(i);
                } else {
                    authors += (authorNames.get(i) + ", ");
                }
            }
            if (authors.length() == 0) {
                textview_authors.setText("Unavailable");
            } else {

                //If there are more than one author then the bug arises where a comma appears after the final author,
                //hence why I am checking if the last character in the string is a comma and if it is then remove it
                char[] chars = authors.toCharArray();
                if(chars[chars.length-1] == ','){
                    authors =  authors.substring(0, authors.length() - 1);
                }
                textview_authors.setText(authors);
            }
            ratingbar_overall_rating.setRating((float) ratings.getOverallAverageRating());
            if(ratings.getOverallAverageRating() == 0){
                textview_overall_rating.setText("");
            } else{
                textview_overall_rating.setText("("+ratings.getOverallAverageRating()+")");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //This will display all the information in the Brief summary tab in the "Book review" page
    //this involves getting the isbn number(previously stored) from the shared preference file which is then used
    //to get the Book entity from the room database. The data attribute "summary" is retrieved from the entity
    //and loaded into the text view which displays the brief summary text.
    public void displayBookInformationInBriefSummaryTab(View view){
        TextView textview_book_summary = view.findViewById(R.id.textview_book_summary);
        Book book = getBookUsingIsbnFromSharedPreferences();
        if(book.getSummary().length() == 0){
            textview_book_summary.setText("Unavailable");
        } else {
            textview_book_summary.setText(book.getSummary());
        }
    }

    //This will display all the information in the Review tab in the "Book review" page
    //this involves getting the isbn number(previously stored) from the shared preference file which is then used
    //to get the Book entity from the room database. The book id retrieved from the entity is then usedto get the reviews
    //from the room database. The reviews were sorted according to the length of each review such that the shortest
    //reviews will be first in the sorted list. It then iterates through the first 4 reviews from the sorted list and adds each
    //review to a string which was used to store the 4 reviews into a single string. While appending each review to that string, a 2 new blank lines
    //will be inserted to allow each review to have space. Finally, this string, containing the formatted reviews, will
    //will be displayed in the text view which displays the reviews on screen
    public void displayBookInformationInReviewsTab(View view){
        MyComparator myComparator = new MyComparator();
        Book book = getBookUsingIsbnFromSharedPreferences();
        TextView textview_reviews = view.findViewById(R.id.textview_reviews);
        List<Review> reviews = db.reviewDao().getReviews(book.bookId);
        Collections.sort(reviews, myComparator);
        String reviewsString = "";
        String review;

        //Display the first 4 reviews
        for(int i =0; i<reviews.size(); i++){

            if (i == (reviews.size() - 1) && i<5 && !reviews.get(i).getReview().matches("")) {
                review = reviews.get(i).getReview().replace("\"","");
                reviewsString += ("\"" + review + "\"");
            } else if(i<5 && !reviews.get(i).getReview().matches("")){
                reviewsString += ("\"" + reviews.get(i).getReview() + "\"\n\n");
            }

        } if(!(reviewsString.length()>3)){
            textview_reviews.setText("Unavailable");
        } else {
            textview_reviews.setText(reviewsString);
        }
    }



    //This will display all the information in the Review tab in the "Book review" page
    //this involves getting the isbn number(previously stored) from the shared preference file which is then used
    //to get the Book entity from the room database. Using the book id retrieved from the Book entity, the Rating entity
    //was retrieved from the room database. After intialising the textviews and rating bars, I used the data attributes from the
    //Rating entity to display it into its relevent textview and rating bar.
    public void displayBookInformationInRatingTab(View view){

        Book book = getBookUsingIsbnFromSharedPreferences();
        Rating ratings = db.ratingDao().getRating(book.getBookId());

        TextView textview_amazon_rating_breakdown_title = view.findViewById(R.id.textview_amazonratingbreakdown_title);
        RatingBar ratingbar_amazon_average_rating = view.findViewById(R.id.ratingbar_amazon_average_rating);
        TextView textview_amazon_average_rating = view.findViewById(R.id.textview_amazon_average_rating);
        RatingBar ratingbar_googlebooks_average_rating = view.findViewById(R.id.ratingbar_googlebooks_average_rating);
        TextView textview_googlebooks_average_rating = view.findViewById(R.id.textview_google_books_average_rating);
        RatingBar ratingbar_goodreads_average_rating = view.findViewById(R.id.ratingbar_goodreads_average_rating);
        TextView textview_goodreads_average_rating = view.findViewById(R.id.textview_goodreads_average_rating);
        TextView textview_four_to_five_star_rating_percentage = view.findViewById(R.id.textview_fourtofivestarratingpercentage);
        TextView textview_one_star_rating_percentage = view.findViewById(R.id.textview_onestarratingpercentage);

        if(ratings != null) {
            ratingbar_amazon_average_rating.setRating((float) ratings.getAmazonAverageRating());
            if (ratings.getAmazonAverageRating() == 0) {
                textview_amazon_average_rating.setText("");
            } else {
                textview_amazon_average_rating.setText("(" + (float) ratings.getAmazonAverageRating() + ")");
            }
            ratingbar_googlebooks_average_rating.setRating((float) ratings.getGoogleBooksAverageRating());
            if (ratings.getGoogleBooksAverageRating() == 0) {
                textview_googlebooks_average_rating.setText("");
            } else {
                textview_googlebooks_average_rating.setText("(" + (float) ratings.getGoogleBooksAverageRating() + ")");
            }

            ratingbar_goodreads_average_rating.setRating((float) ratings.getGoodreadsAverageRating());
            if (ratings.getGoodreadsAverageRating() == 0) {
                textview_goodreads_average_rating.setText("");
            } else {
                textview_goodreads_average_rating.setText("(" + (float) ratings.getGoodreadsAverageRating() + ")");
            }

            if (ratings.getAmazonReviewsCount() == 0) {
                textview_amazon_rating_breakdown_title.setText("Amazon rating breakdown");
            } else {
                textview_amazon_rating_breakdown_title.setText("Amazon rating breakdown (" + ratings.getAmazonReviewsCount() + " reviews)");
            }
            int fourToFiveStarRatingPercentage = ratings.getAmazonFiveStarRatingPercentage() + ratings.getAmazonFourStarRatingPercentage();
            if (fourToFiveStarRatingPercentage == 0) {
                textview_four_to_five_star_rating_percentage.setText("Unavailable");
            } else {
                textview_four_to_five_star_rating_percentage.setText(fourToFiveStarRatingPercentage + "%");
            }
            if (ratings.getAmazonOneStarRatingPercentage() == 0) {
                textview_one_star_rating_percentage.setText("Unavailable");
            } else {
                textview_one_star_rating_percentage.setText(ratings.getAmazonOneStarRatingPercentage() + "%");
            }
        }
        //If the Rating attribute was null then appropriate string values were set to the textviews to inform
        //the user that there was no information relating to ratings.
        else{
            textview_amazon_average_rating.setText("");
            textview_goodreads_average_rating.setText("");
            textview_googlebooks_average_rating.setText("");
            textview_amazon_rating_breakdown_title.setText("Amazon rating breakdown");
            textview_four_to_five_star_rating_percentage.setText("Unavailable");
            textview_one_star_rating_percentage.setText("Unavailable");
        }
    }

    public void displayBookInformationInRecommendedBooksPage(View view){
        Book book = getBookUsingIsbnFromSharedPreferences();

        ImageView imageview_recommended_book_thumbnail = view.findViewById(R.id.imageview_recommended_book_thumbnail);
        TextView textview_title_of_selected_book = view.findViewById(R.id.textview_title_of_selected_book);

        //Check if the book has a valid thumbnail url, if it does not then override the thumbnail url string to be that of the default thumbnail url
        String bookThumbnailUrl = book.getThumbnailUrl();
        if(bookThumbnailUrl.matches("")){
            bookThumbnailUrl = "https://i.gyazo.com/a1b02a68b87056cb4469a6bcb6785932.png";
        }
        //Use picasso to download the image using the url and load it into the image view
        Picasso.with(context).load(bookThumbnailUrl).resize(200,400).centerInside().into(imageview_recommended_book_thumbnail, new Callback() {
            @Override
            public void onSuccess() {
                Log.i("Picasso", "onSuccess: TRUE");
            }

            @Override
            public void onError() {
                Log.i("Picasso", "onError: TRUE");
            }
        });

        textview_title_of_selected_book.setText(book.getTitle());

        //Display custom toast message
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0,0);
        toast.show();
    }

    public Book getBookUsingIsbnFromSharedPreferences(){
        String isbn = getBookIsbnFromSharedPreferences(context);
        Book book = db.bookDao().getBookBasedOnIsbnNumber(isbn);
        return book;
    }

    //This method takes the isbn, title and author name as its argument and these values arised when the user
    //executed a workflow for getting information about a book i.e scanned book/manually searched book/ tapped on new book from search results
    //it first checks to see if the isbn is empty. If it isn't then it store the isbn into the shared preference file for future reference
    //it then uses this stored Isbn to check whether a Book record exists in the database. If the book does exist then proceed to load the book
    //information into the "Book reivew" page. If the book doesn't exist then the Async task for retrieving the book information from the
    //C# server console application will execute.

    //If the entered Isbn was empty then check whether both the title and author arugments contain non-empty values. If yes
    //then this implies that the user wants to find one book and this information will be used during the execition of the
    //Async task for retrieving the book information from the C# server console application.

    //If only one of the title and author arugment contain non-empty value then this implies that the user wants to be
    //find more than one book. Hence why we not including the data containing the title/author but also a string "!isMoreThanOneBook"
    //to inform the C# server console application, during TCP connection, that the client(android app) wants to retrieve information for
    //more than one book. This data is used during the execution of the Async task for retrieving the information of the books from the C# server console application.
    public void searchBook(String isbn, String title, String authorFullName){
        BookReviewFragment bookReviewFragment = new BookReviewFragment();
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        if(!isbn.matches("")){
            storeBookIsbn(context, isbn);
            boolean bookExists = checkIfBookExistsInDatabaseUsingIsbn(context);
            if(bookExists == false){
                execute();
            } else{
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, bookReviewFragment).commit();
            }
        } else if(!title.matches("") && !authorFullName.matches("")){
            boolean bookExists = checkIfBookExistsInDatabaseUsingTitleAndAuthor(context, title, authorFullName);
            if(bookExists == true){
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, bookReviewFragment).commit();
            } else{
                data = "!isMoreThanOneBook"+ title + " " + authorFullName;
                execute();
            }

        } else if(!title.matches("")){
            data = "isMoreThanOneBook"+title;
            isMoreThanOneBook = true;
            execute();
        } else if(!authorFullName.matches("")){
            data = "isMoreThanOneBook"+authorFullName;
            isMoreThanOneBook = true;
            execute();
        } else{
            //Display error message
        }
    }

    //This method will split the concatanated string, containing all the books data where
    // each group of book data is split by the "|" symbol, by each book and store it in a string list
    //This needs to be done because the data received from the C# application console server will be in the form
    // bookData1|bookDat2|bookData3 etc. I used this symbol to overcome the challenge of allowing the Android application
    //to decode the concantated string such that I can get the data string for each book which can be split further
    public static ArrayList<String> splitBooksData(String booksData) {

        //Convert the books data string to a char array and iterate through each character. If the character does not equal
        //the "|" symbol then the string builder will append the character. If does encounter the "|" then the formed string
        //in the string builder will be added to a list and the contents of the string builder will be reset. This process
        //repats until all the entire books data string has been read. The output should a list of arrays containing the data string of each book
        char[] chars  = booksData.toCharArray();
        ArrayList<String> bookDataList = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for(int i =0; i<chars.length; i++) {

            if(chars[i] == '|') {
                bookDataList.add(sb.toString());
                sb.setLength(0);

            }
            else {
                sb.append(chars[i]);
            }

        }

        return bookDataList;
    }



    //If the current string built by the string builder is "*" then that means that the book source returned no data for that specific field.
    //Hence why I want to replace it with "". The inclusion of the "*" to deal with empty data attributes was configured
    //in the C# console server application.

    //Each data attribute for a book is split by the "$" to make it easier to know what each data attribute represents
    //For example, an example string that could be taken as its argument could be "title$author$description$subtitle$isbn$4.5$3.4$2.4$90$6$2$1$1$review$13$genre$12$thumbnailurl"
    //note this is not what the user has enter as the arugment, this the pre-defined format in which the C# console server application
    //will sent to the android application
    public HashMap<BookData,String> decodeBookData(String bookData) {

        //Convert the data string to a char array and terate through each character. If the character does not equal
        //the symbol "$" then the string builder will append it. If it does then string created by the string builder along with its
        //correpsponding data attribute type(enum) is added to a hashmap and the cotents of the string builder wll be reset
        //This process repeats until the entire book data string has been read. The hashmap will then be returned and this makes
        //it simple to load the specific data attributes from hashmap as I know the key(enum).
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
                    case 19:
                        bookDataMap.put(BookData.DominantColor, sb.toString());
                }
                sb.setLength(0);

            }
            else {
                sb.append(chars[i]);

            }

        }

        return bookDataMap;
    }

    //This method will first split the string based on the books which were by separated by the "|" symbol.
    //The output is a list of data strings which contains the information for each book but it is concatenated a single string
    // separated by the "$" symbol. Each string is then
    //decoded and added to the hashmap which is then added to the a list of hashmaps which is
    //returned by this method. Essentially what is returned is all the book data attributes for each book which makes
    //it easier to load into the recycler view under the "Search book results" fragment.
    public List<HashMap<BookData,String>> decodeBooksData(String receivedDataString) {

        List<String> bookDataStrings = splitBooksData(receivedDataString);

        List<HashMap<BookData,String>> bookDataMaps = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for(String bookDataString: bookDataStrings){
            char[] chars = bookDataString.toCharArray();
            int bookDataIndex = 0;
            HashMap<BookData,String> bookDataMap = new HashMap<BookData,String>();
            for(int i =0; i<chars.length; i++) {

                if(chars[i] == '$') {
                    bookDataIndex++;

                    if(bookDataIndex ==1){
                        bookDataMap.put(BookData.Title, sb.toString());
                    } else if(bookDataIndex ==2){
                        bookDataMap.put(BookData.Isbn, sb.toString());
                    } else if(bookDataIndex == 3){
                        bookDataMap.put(BookData.Authors, sb.toString());
                    }
                    sb.setLength(0);

                    //If the end of the data string has been read then the remaining stirng in the stirng
                    //builder is presumably the thumbnail url
                } else if(i == (chars.length-1)){
                    bookDataMap.put(BookData.ThumbnailUrl, sb.toString());
                }
                else {
                    sb.append(chars[i]);
                }
            }
            bookDataMaps.add(bookDataMap);
            sb.setLength(0);
        }
        return bookDataMaps;
    }



    //This was used during the creation of the hashmaps initialised in the methods that decoded the book data string/books data string
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
        ThumbnailUrl,
        DominantColor
    }

    //This methods works by retrieving the previously stored user Id and isbn from the shared preference file
    //The retrieved isbn is then used to get the book id and this id along with the user id is used to get
    // the Audit record from the room database. If the audit record is null then create a new audit record
    //to reflect that the user liked a particular book. If the audit record does exist then update the record to reflect
    //the same scenario as well.
    public void likeBook(){
        int userId = userController.getUserIdFromSharedPreferneces(context);
        String isbn = getBookIsbnFromSharedPreferences(context);
        int bookId = db.bookDao().getBookBasedOnIsbnNumber(isbn).getBookId();

        Audit audit = db.auditDao().getAudit(userId,bookId);
        if (audit != null){
            //Update Status record
            updateStatus(audit.getAuditId(), BookStatus.Liked,"");

        } else{
            //Create new Audit and status record
            createAudit(userId, bookId, BookStatus.Liked,"");
        }

    }

    //This methods works by retrieving the previously stored user Id and isbn from the shared preference file
    //The retrieved isbn is then used to get the book id and this id along with the user id is used to get
    // the Audit record from the room database. If the audit record is null then create a new audit record
    //to reflect that the user wanted to review a particular book later. If the audit record does exist then update the record to reflect
    //the same scenario as well.
    public void reviewBookLater(){
        int userId = userController.getUserIdFromSharedPreferneces(context);
        String isbn = getBookIsbnFromSharedPreferences(context);
        int bookId = db.bookDao().getBookBasedOnIsbnNumber(isbn).getBookId();

        Audit audit = db.auditDao().getAudit(userId,bookId);
        if (audit != null){
            //Update Status record
            updateStatus(audit.getAuditId(), BookStatus.ReviewLater,"");
        } else{
            //Create new Audit and status record
            createAudit(userId, bookId, BookStatus.ReviewLater, "");
        }
    }

    //This methods takes an arugment which holds the string value which represents the reason for the why the user disliked a book
    // based on this value, a string to be used when updating/inserting an audit record in the room database will be
    //initialised. The method then retrieves the previously stored user Id and isbn from the shared preference file
    //The retrieved isbn is then used to get the book id and this id along with the user id is used to get
    // the Audit record from the room database. If the audit record is null then create a new audit record
    //to reflect that the user dislking a particular book along with the reason for disliking it. If the audit record does exist then update the record to reflect
    //the same scenario as well.
    public void dislikeBook(String selectedChoice){
        if(selectedChoice.equals("Didn't like the genre")){
            reasonForDislikingBook = "Genre";
        } else{
            reasonForDislikingBook = "LostInterests";
        }
        int userId = userController.getUserIdFromSharedPreferneces(context);
        String isbn = getBookIsbnFromSharedPreferences(context);
        int bookId = db.bookDao().getBookBasedOnIsbnNumber(isbn).getBookId();

        Audit audit = db.auditDao().getAudit(userId,bookId);
        if (audit != null){
            //Update Status record
            updateStatus(audit.getAuditId(), BookStatus.Disliked,reasonForDislikingBook);

        } else{
            //Create new Audit and status record
            createAudit(userId, bookId, BookStatus.Disliked, reasonForDislikingBook);
        }

    }

    //This method will update the Status value for a given Audit record which is retrieved by using the audit it
    //from the method's arguments.If the new audit status is "Disliked" then update the "Reason" data attribute to
    //be the reason value acquired from this method's argument. However, if the new audit status is not "Disliked" then
    //update erase the value stored in the "Reason" data attribute as the "Reason" value is only valid for audits where
    //the status is "Disliked"
    public void updateStatus(int auditId,BookStatus bookStatus,String reason){
        String newStatus = bookStatus.toString();
        db.statusDao().updateStatusStatus(newStatus, auditId);
        if(newStatus.equals("Disliked")){
            db.statusDao().updateStatusReason(reason,auditId);
        }else{
            db.statusDao().updateStatusReason("",auditId);
        }

    }

    //The method uses all of its arugments to create a new audit record in the Room database.
    //After creating the new audit record, the audit id of the new audit record is used to create a new Status record
    public void createAudit(int userId, int bookId, BookStatus bookStatus,String reason){
        db.auditDao().insertAudit(new Audit(0, userId, bookId));
        int auditId = db.auditDao().getAudit(userId,bookId).getAuditId();
        createStatus(auditId, bookStatus, reason);
    }

    //In addition to the method using all of its arugments, it also uses a string which holds the timestamp of the current
    //time and these were used to create a new Status record in the room database
    public void createStatus(int auditId, BookStatus bookStatus, String reason){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String status = bookStatus.toString();
        db.statusDao().insertStatus(new entities.Status(0,auditId,status,timeStamp,reason));
    }


    public enum BookStatus{
        Liked, Disliked, ReviewLater
    }

    //This method first gets the user id from the shared preference, it also gets the type of books from another shared preference file.
    //Regarding that preference file, this will be updated based on whether the user decided to click the option to view books that were liked or books that needs reviewing
    //Based on the book list type, it retrieves the books matching that type and Returns this list to be loaded into the Recycler adapter
    public List<Book> getBooksBasedOnStatus(TextView toolbar_title){
        UserController userController = new UserController();

        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        int userId = userController.getUserIdFromSharedPreferneces(context);
        String bookListType = getBookListTypeFromSharedPreferences(context);
        if(bookListType.equals(BookListType.Liked.toString())){
            books = getBooksUsingStatus(userId,BookStatus.Liked, null);
            toolbar_title.setText("Liked books");
        } else if (bookListType.equals(BookListType.NeedsReviewing.toString())){
            books = getBooksUsingStatus(userId,BookStatus.ReviewLater, null);
            toolbar_title.setText("Needs reviewing");
        }
        return books;
    }

    //This method works by using the userId from the method's arguments to get all the audit ids from the Room database
    //If the Book status which is also from the method's arugments is equal to "Disliked" then for for each audit id, it uses it to get the "Reason" attrbiute value from the Status record where the foreign is that of the audit id
    //and if that "Reason" matches the Reason value which is also from the method's arugments then that audit id will be added to the list of audit ids

    //If the Book status does not equal "Disliked" then it iterates through each audit id and uses it to get the "Status" attrbiute value from the Status entity
    //where the foriegn is that of the audit id. If that "Status" attributes matches the book status passed in the method's arguments then added that audit to the list of audit ids

    //Regardless of which path was executed, after iterating through all the audits ids, each audit id is used to get the Book entity which
    //were utimately added to the final list of books which is what this method Returns.
    public List<Book> getBooksUsingStatus(int userId, BookStatus bookStatus, DislikedBookReason dislikedBookReason){
        List<Book> books = new ArrayList<>();
        List<Integer> auditIds = new ArrayList<>();
        List<Integer> allAuditIdsBasedOnUserId = null;

        String auditStatus ="";
        String auditReason = "";

        allAuditIdsBasedOnUserId = db.auditDao().getAuditIds(userId);

        //If the book status is disliked then only the audits id that satisify the reason for disliking a book is added to the list
        if(bookStatus.equals(BookStatus.Disliked) && dislikedBookReason != null){
            for (int auditId : allAuditIdsBasedOnUserId) {
                auditReason = db.statusDao().getReason(auditId);
                if (auditReason.equals(dislikedBookReason.toString())) {
                    auditIds.add(auditId);
                }
            }
        }else {
            //For each audit id, use it to get the Status from the Status entity and if it equals to "Liked" then add the audit it to the AuditIds list
            for (int auditId : allAuditIdsBasedOnUserId) {
                auditStatus = db.statusDao().getStatus(auditId);
                if (auditStatus.equals(bookStatus.toString())) {
                    auditIds.add(auditId);
                }
            }
        }

        //For each audit id in the updated list of audit ids with a specific audit status, get the book id using that audit id and use that to get the book entity
        books = getBooksFromAuditIds(auditIds);

        return books;
    }

    //This iterates through each audit id from the list audits ids(from the method's arguments) and uses the id to get the book id from the BookAudit table in the Room database
    //It then uses that book id to get the Book entity from the Room database and this is added to the list of books. After iterating through all the audit ids,
    //the list of books is returned.
    public List<Book> getBooksFromAuditIds(List<Integer> auditIds){
        List<Book> books = new ArrayList<>();
        //For each audit id in the updated list of audit ids with a specific audit status, get the book id using that audit id and use that to get the book entity
        for(int auditId : auditIds){
            int bookId = db.auditBookDao().getBookId(auditId);
            book = db.bookDao().getBookBasedOnBookId(bookId);
            books.add(book);
        }
        return books;
    }

    //This method iterates through each book from the list of books(passed into the method's arguments) and first gets the book's id. This
    //id is then uses to get all the genre ids from the BookGenre table in the Room database. Then for each genre id, it uses it to get the genre label
    // from the Genre table and if that retrieved label matches the genre(passed into the method's arguments), then this book is added to the newly initallised
    //list of books. After interating through each book, the new/filtered list of books is returned.
    public List<Book> getBooksBasedOnGenre(String genre, List<Book> books){
        List<Book> filteredBooks = new ArrayList<>();
        for(Book book: books) {
            int bookId = book.getBookId();
            List<Integer> genreIds = db.bookGenreDao().getGenreIdsOfBook(bookId);
            for (int genreId : genreIds) {
                String genreLabel = db.genreDao().getGenreLabel(genreId);
                if (genreLabel.equals(genre)) {
                    filteredBooks.add(book);
                }
            }
        }
        return filteredBooks;
    }

    //Takes a list of Books as its arguments and Returns a another list of books which only contains books that were written by a given author
    //This works by first using the author(passed into the method's argument) to get the author id from the Author table in the Room database
    //it then proceeds to iterate through each book from the list of books(passed into the method's argument) and uses the book id along with the
    // the previously derived author id to get the BookAuthor entity from the Room database. If this entity is not null then this
    // implies that the book was written by that author(passed into the method's argument) and this book is added to the newly initialised list of books.
    //After iterating through all the books, the filtered/new list of books is returned.
    public List<Book> getLikedBooksWrittenByAuthor(String author, List<Book> books){
        List<Book> likedBooksWrittenByAuthor = new ArrayList<>();
        int authorId = db.authorDao().getAuthorId(author);
        for(Book book: books){
            BookAuthor bookAuthor = db.bookAuthorDao().getBookAuthor(authorId, book.getBookId());
            if(bookAuthor != null){
                likedBooksWrittenByAuthor.add(book);
            }
        }
        return likedBooksWrittenByAuthor;
    }

    //This enum was used to specify the types of books
    public enum BookListType{
        Liked, NeedsReviewing, Disliked
    }
    //This enum was used to specify the reasons for which a user would dislike a book
    public enum DislikedBookReason{
        Genre, LostInterests
    }

    //This method will filter the list of books(passed into the method's argument) by getting only the books that have high overall rating
    //This is done by it iterating through each book and for each book, it uses it book id to get the overall rating value from the Rating table in the room database. If that
    //overall rating is between 4.5 and 5 then added to the filtered list of books. After iterating through all the books, this method will return the filtered list of books

    public List<Book> getBooksWithHighOverallRating(List<Book> books){
        List<Book> topBooks = new ArrayList<>();
        for(Book book : books){
            int bookId = book.getBookId();
            Rating rating = db.ratingDao().getRating(bookId);
            try {
                double overallAverageRating = rating.getOverallAverageRating();
                if (overallAverageRating > 4.49) {
                    topBooks.add(book);
                }
            } catch(NullPointerException e){
                //Do nothing
            }
        }
        return topBooks;
    }

    //This was used to store the book and an integer value to tell the NewBooksAdpater on which books to display its thumbnail for.
    //I.e if the integer was 0 then the adapter would not display the books thumbnail and 1 does the opposite. This method sets the default integer values for all books
    public HashMap<Book, Integer> getBookIntegerHashMap(Object[] books){
        HashMap<Book, Integer> bookIntegerHashMap = new HashMap<>();

        for(int i = 0; i<books.length; i++){
            Book book = ((Map.Entry<Book, Integer>) books[i]).getKey();

            //Use Book with default integer value to insert record in hashmap
            bookIntegerHashMap.put(book,0);
        }
        return bookIntegerHashMap;
    }


}
