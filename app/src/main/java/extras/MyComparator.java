package extras;

import java.util.Comparator;

import entities.Review;

//Credit to Barend from https://stackoverflow.com/questions/7575761/sort-arraylist-of-strings-by-length for the solution of being able to sort a list by the string length
public class MyComparator implements Comparator<Review> {



    public MyComparator() {
        super();
    }

    //This works by compare the length of two given reviews which are passed into the method's arguments and returns the difference between the two in terms of length.
    public int compare(Review r1, Review r2) {
        int dist1 = Math.abs(r1.getReview().length());
        int dist2 = Math.abs(r2.getReview().length());

        return dist1 - dist2;
    }
}
