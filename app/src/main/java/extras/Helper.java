package extras;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controllers.ColorController;
import controllers.UserController;
import dataaccess.setup.AppDatabase;
import entities.Audit;
import entities.Book;
import entities.BookAuthor;
import entities.BookGenre;
import entities.Rating;
import entities.Review;

public class Helper {
    AppDatabase db;
    ColorController colorController;

    public Helper() {
        colorController = new ColorController();
    }

    //As some of the string will be empty, converting it to null will thrown an exception and hence why I created this method to handle this. If such an exception is thrown then it will return a 0. Otherwise it
    //will returns the double value that was parsed from the string(passed into the method's argument)
    public double convertStringToDouble(String str){
        try{
            return Double.parseDouble(str);
        } catch(Exception e){
            return 0;
        }
    }

    //This method works by converting a string to an integer. However, because the string may be null which would lead to an exception to be thrown, this method handles this by simply returning a 0.
    //Hence why I created this method.
    public int convertStringToInt(String str){
        try{
            return Integer.parseInt(str);
        } catch(Exception e){
            return 0;
        }
    }

    //Some of the data fields will be empty and this was marked by "*"
    // if it does contain this symbol then return an empty string. Otherwise, it will return the string(passed into the method's argument)
    public String checkIfBookDataAttributeNull(String str){
        if (str.equals("*")){
            return "";
        } else{
            return str;
        }
    }

    //Goes through each unique string in the list(passed into the method's arguments) and gets the number of occurences within the string and both the string and number are put into the hashmap
    //which is was is returned by this method.
    //Credit to Jahroy for solution on getting uniques values from string list https://stackoverflow.com/questions/13429119/get-unique-values-from-arraylist-in-java
    //Credit to Lars A for solution on getting the number occurence of a string in the list  https://stackoverflow.com/questions/505928/how-to-count-the-number-of-occurrences-of-an-element-in-a-list
    public HashMap<String, Integer> getOccurencesOfStringList(List<String> stringList){
        HashMap<String, Integer> hashMap = new HashMap<>();
        Set<String> uniqueStrings= new HashSet<>(stringList);
        for(String uniqueString: uniqueStrings){
            int occurences = Collections.frequency(stringList, uniqueString);
            hashMap.put(uniqueString, occurences);
        }
        return hashMap;
    }

    //This method works by creating an object array from the hashmap(passed into the method's argument). This array is then sorted by using a Comparator to sort the array based on the integer values.
    //This method then returns the sorted array.
    //Credit to Evgeniy D for solution on sorting the values in a hashmap https://stackoverflow.com/questions/21054415/how-to-sort-a-hashmap-by-the-integer-value
    public Object[] sortHashMapBasedOnKeyValue(HashMap<String, Integer> hashMap){
        Object[] a = hashMap.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }
        });
        return a;
    }


    //This method works by checking whether the length of the object array(passed into the method's arguments) is less than 6. If it is then it iterates through each object. Otherwise, it will iterate through the first 5 objects.
    //In either case, each object being access through the for loops are used to gets the author name and number of occurences. These two values are then used to create a new Pair which is then added to the list of pairs. After
    //iterating through all the objects then the list of pairs are returned by this method.
    public List<Pair<String,Integer>> getTopPairs(Object[] a){
        List<Pair<String, Integer>> topPairs = new ArrayList<>();
        if(a.length <6){
            for(int i=0; i<a.length; i++){
                String authorName = ((Map.Entry<String, Integer>) a[i]).getKey();
                int occurences = ((Map.Entry<String, Integer>) a[i]).getValue();
                Pair<String, Integer> pair = new Pair<>(authorName,occurences);
                topPairs.add(pair);
            }

        } else {
            for (int i = 0; i < 5; i++) {
                String authorName = ((Map.Entry<String, Integer>) a[i]).getKey();
                int occurences = ((Map.Entry<String, Integer>) a[i]).getValue();
                Pair<String, Integer> pair = new Pair<>(authorName, occurences);
                topPairs.add(pair);
            }
        }
        return topPairs;
    }


    //This will only return the list where all values from the list(passed into the method's arguments) are valid i.e not containing numbers.
    //This works by iterating through each string in the list and check whether it contains numbers, If it doesn't then add that to the newly initialised string list. Else the string is ignored. After
    //iterating through each string, the list containing all valid strings are returned.
    public List<String> getValidValuesFromList(List<String> list){
        List<String> validList = new ArrayList<>();
        for(String string: list){
            if(!string.matches(".*\\d.*")){
                validList.add(string);
            }
        }
        return validList;
    }

    //This method was only created to allow me to delete a certain book as part of the user testing and demonstration events. Has not be used during the development of this app.
    public void deleteBook(String isbn, Context context){
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        UserController userController = new UserController();
        Book book = db.bookDao().getBookBasedOnIsbnNumber(isbn);
        int bookId = book.getBookId();
        int userId = userController.getUserIdFromSharedPreferneces(context);
        Audit audit =  db.auditDao().getAudit(userId, bookId);
        //db.auditDao().deleteAudit(audit);
        //Status status = db.statusDao().getStatusUsingAuditId(audit.getAuditId());
        //db.statusDao().deleteStatus(status);
        List<BookGenre> bookGenres = db.bookGenreDao().getBookGenresUsingBookId(bookId);
        for(BookGenre bg: bookGenres){
            db.bookGenreDao().deleteBookGenre(bg);
        }

        List<BookAuthor> bookAuthors = db.bookAuthorDao().getBookAuthorsUsingBookId(bookId);
        for(BookAuthor ba: bookAuthors){
            db.bookAuthorDao().deleteBookAuthor(ba);
        }
        Rating rating = db.ratingDao().getRating(bookId);
        db.ratingDao().deleteRating(rating);
        List<Review> reviews = db.reviewDao().getReviews(bookId);
        for(Review review: reviews){
            db.reviewDao().deleteReview(review);

        }
        db.bookDao().deleteBook(book);

    }

    //Because the C# server console application used the "#" symbol to seperarate the data attribute which
    //have more than one value such as reviews, genres and authors. An example data string which could passed in the arugment
    //is "James Patterson#Peter King". This method splits the strings based on the "#" symbol which Returns a string list.
    //this allows the string containing more than one value(genre, author, review) to be split and managed as a list.
    public static String[] splitStringsAndPutIntoList(String str){
        String[] stringArray = str.split("#",100);
        return stringArray;
    }

    //Method is used to edit the properties of a TextView(passed into the method's arguments) including visibility and its text value. It also modifies the EditText element which includes setting
    //the colour of its background tint.
    public void editMessageProperties(EditText editText, TextView textView, String message, ColorController.Colors color, boolean isVisible) {

        //Access the error msg that is displayed beneath the edit text element and set its text value
        textView.setText(message);
        colorController.setBackgroundTint(editText, color);
        if (isVisible == false) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }


    //Regarding the isLoginActivity in the method's argument, this is used to check if the activity is the Login activity. If it is then I don't want the textview error message to be the default
    //message. This method works by getting the value entered into the EditText element(passed into the method's arguments). It then uses this value to check with it is empty. If it is then modify the background
    //tint color of the EditText element to be red. It then uses the boolean value passed into the method's argument to check whether the the TextView and EditText elements are in the Login acitivty. If it is
    //then modify the value to be displayed in the TextView(passed into the method's arguments) to "Email or password or both are empty". If it is not the Login activity then set the value of the TextView to be
    //"This field is required". I then makes the TextView element to be visible and the method then Returns false.

    //If the value typed into the EditText element was not empty then the background tint color of the EditText element is set to white and the TextView element is made invisible and the method Returns true.
    public boolean checkEditTextFieldNonEmpty(EditText editText, TextView textView_error_msg, boolean isLoginActivity) {
        String editText_value = editText.getText().toString();

        if (editText_value.matches("")) {
            colorController.setBackgroundTint(editText, ColorController.Colors.RED);
            if (isLoginActivity == true) {
                textView_error_msg.setText("Email or password or both are empty");
            } else {
                textView_error_msg.setText(R.string.this_field_is_required);
            }
            textView_error_msg.setVisibility(View.VISIBLE);

            return false;
        } else {
            colorController.setBackgroundTint(editText, ColorController.Colors.WHITE);
            textView_error_msg.setVisibility(View.INVISIBLE);
            return true;
        }
    }





}
