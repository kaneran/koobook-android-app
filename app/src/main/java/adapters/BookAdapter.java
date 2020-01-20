package adapters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.koobookandroidapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dataaccess.setup.AppDatabase;
import entities.Author;
import entities.Book;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    List<Book> books;
    AppDatabase db;
    double overallAverageRating;
    List<String> authors;
    Context context;
    public BookAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_row, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        List<Integer> authorIds;
        String authorName;
        List<String> authorNames;
        String authorsNamesConcatanated;
        StringBuilder sb;
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        //Get overall rating based on the book id which was retrieved by using the index from this method's argument to get the book entity from the book list
        overallAverageRating = db.ratingDao().getRating(books.get(position).getBookId()).getOverallAverageRating();
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
        Picasso.with(context).load(books.get(position).getThumbnailUrl()).into(holder.imageview_book_thumbnail);
        holder.textview_author.setText("Author: "+ authorsNamesConcatanated);
        holder.ratingbar_overall_average_rating.setRating((float)overallAverageRating);
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
