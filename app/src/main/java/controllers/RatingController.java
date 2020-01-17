package controllers;

import java.util.HashMap;

public class RatingController {

    public double computeOverallAverageRating(HashMap<BookController.BookData, Double> averageRatingMap) {
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
        return Math.round((averageRating*10)/10.0);
    }
}
