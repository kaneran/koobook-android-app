package extras;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controllers.UserController;
import dataaccess.setup.AppDatabase;

public class ContentBasedRecommender {
    AppDatabase db;
    Context context;
    public ContentBasedRecommender(Context context) {
        this.context = context;
        db = Room.databaseBuilder(context, AppDatabase.class, "production").allowMainThreadQueries().build();
    }

    public void recommendBooks(int userId){
        List<Integer> bookIds = new ArrayList<>();
        List<String> uniqueGenres = new ArrayList<>();
        int[][] bookGenreArr;
        int[][] bookUserArr;
        int[] bookTotalAttributeArr;
        HashMap<Integer,Integer> bookIdRowIndexHashmap = new HashMap<>();
        HashMap<String, Integer> genreColumnIndexHashmap = new HashMap<>();

        //Get all books that all users have interacted with

        //Get all user ids from Room database
        List<Integer> userIds = db.userDao().getUserIds();

        //For each user id, Use user id to get all audit ids
        for(int uid: userIds){
            List<Integer> auditIds = db.auditDao().getAuditIds(uid);

            //For each audit id, use it to get book id and store it in book id list IFF it does not exist in the generated list of book ids
            for(int auditId: auditIds){
                int bookId = db.auditBookDao().getBookId(auditId);

                if(!bookIds.contains(bookId)){
                    bookIds.add(bookId);
                }

            }
        }

        //Go through each book and get the genre labels, add it to the list if it does not exist in the generated list of genres
        for(int bookId: bookIds){
            List<Integer> genreIds  = db.bookGenreDao().getGenreIdsOfBook(bookId);

            //For each genre, get the genre label and add to list IFF it does not exist in list
            for(int genreId : genreIds){
                String genre = db.genreDao().getGenreLabel(genreId);
                if(!uniqueGenres.contains(genre)){
                    uniqueGenres.add(genre);
                }
            }
        }

        bookGenreArr = new int[bookIds.size()][uniqueGenres.size()];
        bookUserArr = new int[bookIds.size()][userIds.size()];
        bookTotalAttributeArr = new int[bookIds.size()];

        //Populate Hashmap which maps each book id to a row in the Book Genre 2d array
        int count= 1;
        for(int bId: bookIds){
            bookIdRowIndexHashmap.put(bId, count);
            count++;
        }

        //Populate Hashmap which maps each unique genre to a column in the Book Genre 2d array
        count= 1;
        for(String uniqueGenre: uniqueGenres){
            genreColumnIndexHashmap.put(uniqueGenre, count);
            count++;
        }


        //Populate the book genre array
        for(int bookId: bookIds){
            int bookIndex = bookIdRowIndexHashmap.get(bookId);
            bookIndex--;

            //Get genre ids using book id
            List<Integer> bookGenresIds = db.bookGenreDao().getGenreIdsOfBook(bookId);

            //Iterate through each genre in the entire list of unqiue genres
            for(String uniqueGenre : uniqueGenres){
                int genreIndex = genreColumnIndexHashmap.get(uniqueGenre);
                genreIndex--;
                //Check if given genre is contained in given book
                if(bookGenresIds.contains(genreIndex)){
                    bookGenreArr[bookIndex][genreIndex] = 1;
                } else{
                    bookGenreArr[bookIndex][genreIndex] = 0;
                }

            }

        }

        //Populate the book user array
        for(int bookId: bookIds){
            List<Integer> mUserIds = new ArrayList<>();

            int bookIndex = bookIdRowIndexHashmap.get(bookId);
            bookIndex--;

            //Get all audit ids
            List<Integer> auditIds = db.auditBookDao().getAuditIds(bookId);

            //Use audits id to get user ids and add it to list
            for(int auditId: auditIds){
                int mUserId = db.auditDao().getUserId(auditId);
                mUserIds.add(mUserId);

                //Check if user liked/dislike or planned to review it later
                String status = db.statusDao().getStatus(auditId);
                mUserId--;
                if(status.equals("Liked")){
                    bookUserArr[bookIndex][mUserId] = 1;
                } else if(status.equals("Disliked")){
                    bookUserArr[bookIndex][mUserId] = -1;
                } else if(status.equals("NeedsReviewing")){
                    bookUserArr[bookIndex][mUserId] = 0;
                }
            }

            //Now deal with the user who did not interact with the given book
            for(int nUserId: userIds){

                //Check if user itneracted with book( i.e there is no audit record for user id/book id pair_
                if(!mUserIds.contains(nUserId)){
                    nUserId--;
                    bookUserArr[bookIndex][nUserId] = 0;
                }
            }

        }

        //Populate Total attributes array
        //For each row in the BookGenre array
        for(int bookId: bookIds){
            int totalAttributeValue = 0;

            int bookIndex = bookIdRowIndexHashmap.get(bookId);

            bookIndex--;

            //For each column in the BookGenre array
            for(String genre: uniqueGenres){
                int genreIndex = genreColumnIndexHashmap.get(genre);
                genreIndex--;
                totalAttributeValue += bookGenreArr[bookIndex][genreIndex];
            }
            bookTotalAttributeArr[bookIndex] = totalAttributeValue;
        }
    }

}
