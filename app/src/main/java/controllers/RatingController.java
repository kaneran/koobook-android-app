package controllers;

import java.text.DecimalFormat;
import java.util.HashMap;

public class RatingController {

    //Credit to Elaice for the solution on converting a double to 1 decimal place
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
