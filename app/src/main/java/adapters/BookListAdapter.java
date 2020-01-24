package adapters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.koobookandroidapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import controllers.BookController;
import dataaccess.setup.AppDatabase;
import entities.Book;
import entities.Rating;
import fragments.BookReviewFragment;

//Acquired the knowledge to create this adapter class from https://www.youtube.com/watch?v=CTBiwKlO5IU&feature=youtu.be&t=2160
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    List<Book> books;
    AppDatabase db;
    double overallAverageRating;
    Context context;
    List<Integer> authorIds;
    String authorName;
    List<String> authorNames;
    String authorsNamesConcatanated;
    StringBuilder sb;
    BookController bookController;
    FragmentManager fragmentManager;
    String isbn;
    BookReviewFragment bookReviewFragment;
    ImageView imageView;
    int imageHeight;
    int imageWidth;

    public BookListAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_row, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListAdapter.ViewHolder holder, final int position) {

        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        //Get overall rating based on the book id which was retrieved by using the index from this method's argument to get the book entity from the book list
        Rating rating = db.ratingDao().getRating(books.get(position).getBookId());
        if(rating != null) {
            overallAverageRating = rating.getOverallAverageRating();
        } else{
            overallAverageRating = 0;
        }
        //Get authors based on the book id which was retrieved by using the index from this method's argument to get the book entity from the book list
        authorIds = db.bookAuthorDao().getAuthorIdsOfBook(books.get(position).getBookId());
        authorNames = new ArrayList<>();
        //For each author id, use it to retrieve the author's name and store it in a list
        for(int authorId: authorIds){
            authorName = db.authorDao().getAuthorName(authorId);
            authorNames.add(authorName);
        }

        sb = new StringBuilder();
        //For each authors name, concat it into a single string and format it by adding commas between each author name
        //once it reaches the last name in the list or their was only one name, simply append it to the string builder without adding a commas
        //If there are not names in the list then set the default text of the author textview to be "Unavailable"
        if(authorNames.size() > 0){
            for(int i =0; i<authorNames.size(); i++){
                if(i == (authorNames.size()-1)){
                    sb.append(authorNames.get(i));
                } else{
                    sb.append(authorNames.get(i) + ", ");
                }
            }
            authorsNamesConcatanated = sb.toString();
        } else{
            authorsNamesConcatanated = "Unavailable";
        }

        holder.textview_book_title.setText(books.get(position).getTitle());
        if(!books.get(position).getThumbnailUrl().matches("")){
            Picasso.with(context).load(books.get(position).getThumbnailUrl()).resize(200,400).centerInside().into(holder.imageview_book_thumbnail, new Callback() {
                @Override
                public void onSuccess() {
                    Log.i("Picasso", "onSuccess: TRUE");
                }

                @Override
                public void onError() {
                    Log.i("Picasso", "onError: TRUE");
                }
            });
        } else{
            holder.imageview_book_thumbnail.getLayoutParams().height= 400;
            holder.imageview_book_thumbnail.getLayoutParams().width = 200;
            holder.imageview_book_thumbnail.requestLayout();

        }

        holder.textview_author.setText("Author: "+ authorsNamesConcatanated);
        holder.ratingbar_overall_average_rating.setRating((float)overallAverageRating);


        //When one of the cardviews(rows) is clicked, the isbn is stored in the preference file and then the
        // "Book review" page will be loaded and will use the stored isbn to display the relevent book information
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = ((FragmentActivity)v.getContext()).getSupportFragmentManager();
                bookReviewFragment = new BookReviewFragment();
                bookController = new BookController(v.getContext());
                isbn = books.get(position).getIsbnNumber();
                bookController.storeBookIsbn(v.getContext(), isbn);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, bookReviewFragment).commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textview_book_title, textview_author;
        ImageView imageview_book_thumbnail;
        RatingBar ratingbar_overall_average_rating;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            textview_book_title = itemView.findViewById(R.id.textview_book_row_title);
            textview_author = itemView.findViewById(R.id.textview_book_row_author);
            imageview_book_thumbnail = itemView.findViewById(R.id.imageview_bookthumbnail);
            ratingbar_overall_average_rating = itemView.findViewById(R.id.ratingbar_book_row_overall_average_rating);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }
}
