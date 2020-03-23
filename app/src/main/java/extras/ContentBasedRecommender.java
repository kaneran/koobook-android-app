package extras;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import controllers.BookController;
import controllers.UserController;
import dataaccess.setup.AppDatabase;
import entities.Audit;
import entities.Book;
import entities.Status;

public class ContentBasedRecommender {
    AppDatabase db;
    Context context;
    public ContentBasedRecommender(Context context) {
        this.context = context;
        db = Room.databaseBuilder(context, AppDatabase.class, "production").allowMainThreadQueries().build();
    }

    public void insertDummyData(){
        List<Integer> userIds = db.userDao().getUserIds();
        Random r = new Random();

        for(int userId : userIds){
            if(userId != 4){
                int x = r.nextInt(43);
                String[] options = new String[3];
                options[0]= "Liked";
                options[1]= "NeedsReviewing";
                options[2]= "Disliked";

                int y = x % 3;
                db.auditDao().insertAudit(new Audit(0, userId, x));

                int auditId = db.auditDao().getAuditId(userId, x);
                db.statusDao().insertStatus(new Status(0, auditId,options[y],"",""));

                int xy = r.nextInt(43);

                if(x != xy){
                    db.auditDao().insertAudit(new Audit(0, userId, xy));
                    y = x % 3;
                    auditId = db.auditDao().getAuditId(userId, x);
                    db.statusDao().insertStatus(new Status(0, auditId,options[y],"",""));
                }

                x = r.nextInt(43);
                if(x != xy){
                    db.auditDao().insertAudit(new Audit(0, userId, x));
                    y = x % 3;
                    auditId = db.auditDao().getAuditId(userId, x);
                    db.statusDao().insertStatus(new Status(0, auditId,options[y],"",""));
                }

            }

        }

    }

    public void insertData(){
        List<Integer> userIds = db.userDao().getUserIds();
        Random r = new Random();

        int userId = 1;
         int x = r.nextInt(43);
         String[] options = new String[3];
         options[0]= "Liked";
         options[1]= "NeedsReviewing";
         options[2]= "Disliked";

         int y = x % 3;
         db.auditDao().insertAudit(new Audit(0, userId, x));

         int auditId = db.auditDao().getAuditId(userId, x);
         db.statusDao().insertStatus(new Status(0, auditId,options[0],"",""));

         int xy = r.nextInt(43);

         if(x != xy){
             db.auditDao().insertAudit(new Audit(0, userId, xy));
             y = x % 3;
             auditId = db.auditDao().getAuditId(userId, x);
                    db.statusDao().insertStatus(new Status(0, auditId,options[0],"",""));
                }

                x = r.nextInt(43);
                if(x != xy){
                    db.auditDao().insertAudit(new Audit(0, userId, x));
                    y = x % 3;
                    auditId = db.auditDao().getAuditId(userId, x);
                    db.statusDao().insertStatus(new Status(0, auditId,options[0],"",""));
                }
    }

    public List<Book> recommendBooks(int userId){
        List<Integer> bookIds = new ArrayList<>();
        List<String> uniqueGenres = new ArrayList<>();
        double[][] bookGenreArr;
        double[][] userProfileArr;
        int[][] bookUserArr;
        int[] bookTotalAttributeArr;
        double[] dfArr;
        double[] idfArr;
        double[][] predUserArr;
        HashMap<Integer,Integer> bookIdRowIndexHashmap = new HashMap<>();
        HashMap<String, Integer> genreColumnIndexHashmap = new HashMap<>();
        List<Book> recommendedBooks = new ArrayList<>();

        //insertDummyData();
        //insertDummyData();
        //insertDummyData();

        //Get all books that all users have interacted with

        //Get all user ids from Room database
        List<Integer> userIds = db.userDao().getUserIds();

        //insertDummyData();

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

        bookGenreArr = new double[bookIds.size()][uniqueGenres.size()];
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

                mUserId--;
                //Check if user liked/dislike or planned to review it later
                String status = db.statusDao().getStatus(auditId);
                if(status != null) {

                    if (status.equals("Liked")) {
                        bookUserArr[bookIndex][mUserId] = 1;
                    } else if (status.equals("Disliked")) {
                        bookUserArr[bookIndex][mUserId] = -1;
                    } else if (status.equals("NeedsReviewing")) {
                        bookUserArr[bookIndex][mUserId] = 0;
                    }
                } else{
                    int[] options = new int[3];
                    options[0]= 0;
                    options[1]= 1;
                    options[2]= -1;
                    Random random = new Random();
                    int x = random.nextInt(2);
                    bookUserArr[bookIndex][mUserId] = options[x];
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

            List<Integer> genreIds = db.bookGenreDao().getGenreIdsOfBook(bookId);
            bookTotalAttributeArr[bookIndex] = genreIds.size();
         }

        //Initialise the DF array
        dfArr = new double[uniqueGenres.size()];

        //For each unique genre
        for(String genre : uniqueGenres){
            double sum = 0;
            int columnIndex = genreColumnIndexHashmap.get(genre);
            columnIndex--;
            //for each row
            for(int bookId: bookIds){
                int bookIndex = bookIdRowIndexHashmap.get(bookId);
                bookIndex--;
                double cell_value = bookGenreArr[bookIndex][columnIndex];
                sum += cell_value;
            }
            dfArr[columnIndex] = sum;
            //DOES NOT WORK, ALL VALUES ARE 0...
        }





        //Normalise each cell value in the BookGenre array
        //For each row in the BookGenre array
        for(int bookId: bookIds){
            int bookIndex = bookIdRowIndexHashmap.get(bookId);
            bookIndex--;
            int total_attribute_value = bookTotalAttributeArr[bookIndex];

            //For each column in the row
            for(int i=0; i< uniqueGenres.size(); i++){

                //Get cell value
                double cell_value = bookGenreArr[bookIndex][i];
                double normalized_cell_value = (cell_value / Math.sqrt(total_attribute_value));
                bookGenreArr[bookIndex][i] = normalized_cell_value;
            }
        }

        //Initialise User Profiles
        userProfileArr = new double[userIds.size()][uniqueGenres.size()];

        //For each user row in UserProfileArray
        for(int mUserId : userIds){
            mUserId--;

            //For each unique genre
            for(String genre : uniqueGenres){
                double sum = 0;
                int columnIndex = genreColumnIndexHashmap.get(genre);
                columnIndex--;
                //for each row
                for(int bookId: bookIds){
                    int bookIndex = bookIdRowIndexHashmap.get(bookId);
                    bookIndex--;
                    double normalized_value = bookGenreArr[bookIndex][columnIndex];
                    double user_preference_value = bookUserArr[bookIndex][mUserId];
                    sum += (normalized_value * user_preference_value);

                }
                userProfileArr[mUserId][columnIndex] = sum;
            }

        }

        //Initialise IDF array
        idfArr = new double[uniqueGenres.size()];

        //For each column in the IDF array
        for(String genre: uniqueGenres){
            int columnIndex = genreColumnIndexHashmap.get(genre);
            columnIndex--;

            double dfValue = dfArr[columnIndex];
            double idfValue = Math.log10(bookIds.size()/dfValue);
            if(Double.isInfinite(idfValue)){
                idfValue = 0;
            }
            idfArr[columnIndex] = idfValue;
        }


        //Initialise PredUser array and populate it with the predictions
        predUserArr = new double[bookIds.size()][userIds.size()];
        //For each user
        for(int uId : userIds){
            uId--;
            //for each row in BookGenre array
            for(int bookId : bookIds){
                int bookIndex = bookIdRowIndexHashmap.get(bookId);
                bookIndex--;

                double weighted_score_for_book = 0;
                //for each column in a row in BookGenre array
                for(String genre: uniqueGenres){
                    int columnIndex = genreColumnIndexHashmap.get(genre);
                    columnIndex--;

                    //Get column cell value
                    double columnValue = bookGenreArr[bookIndex][columnIndex];

                    //Get IDF value
                    double idfValue = idfArr[columnIndex];

                    //Use user id to get value from user profile arr
                    double userProfileColumnValue = userProfileArr[uId][columnIndex];

                    weighted_score_for_book += (columnValue * idfValue * userProfileColumnValue);
                }

                predUserArr[bookIndex][uId] = weighted_score_for_book;
            }
        }

        //Get the most relevent books for user
        for(int bookId : bookIds){
            int bookIndex = bookIdRowIndexHashmap.get(bookId);
            bookIndex--;
            double weighted_score = predUserArr[bookIndex][userId];
            if(weighted_score>0){
                //Check if it is a book already seen by the user
                Audit audit = db.auditDao().getAudit(userId, bookId);

                if(audit == null){
                    Book book = db.bookDao().getBookBasedOnBookId(bookId);
                    recommendedBooks.add(book);
                }
            }
        }
        return recommendedBooks;
    }


}
