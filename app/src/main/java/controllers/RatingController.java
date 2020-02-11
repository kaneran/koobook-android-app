package controllers;

import java.text.DecimalFormat;
import java.util.HashMap;

public class RatingController {

    //Credit to Elaice for the solution on converting a double to 1 decimal place
    //This method works by iterating through each value from the hashmap(passed into the method's argument) and checks if it equals 0. If it does equal 0 then do nothing, else incrmeent the counter
    //which is used to record the number of book sources that has a non-zero average rating. After iterating through each value, it then proceeds to calculate the overall average rating by summing all values
    //and divding it by the counter which will be a double. I then rounded this double to 1 decimal place which consequentially returned a string. It then converts the string to a double and Returns it.
    public double computeOverallAverageRating(HashMap<BookController.BookData, Double> averageRatingMap) {
        DecimalFormat df = new DecimalFormat("#.#");
        int count = 0;
        for(double averageRating : averageRatingMap.values()){
            if(averageRating != 0){
                count++;
            }
        }
        double amazonAverageRating = averageRatingMap.get(BookController.BookData.AmazonAverageRating);
        double googleBooksAverageRating = averageRatingMap.get(BookController.BookData.GoogleBooksAverageRating);
        double goodreadsAverageRating = averageRatingMap.get(BookController.BookData.GoodreadsAverageRating);
        double averageRating = (amazonAverageRating + googleBooksAverageRating + goodreadsAverageRating)/count;
        String averageRatingFormatted = df.format(averageRating);
        return Double.parseDouble(averageRatingFormatted);
    }
}
