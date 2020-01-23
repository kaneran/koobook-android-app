package adapters;

import android.arch.persistence.room.Room;
import android.content.Context;
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
import fragments.BookReviewFragment;
import fragments.LoadingScreenFragment;

//Acquired the knowledge to create this adapter class from https://www.youtube.com/watch?v=CTBiwKlO5IU&feature=youtu.be&t=2160
public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    List<Book> books;
    AppDatabase db;
    Context context;
    BookController bookController;
    FragmentManager fragmentManager;
    String isbn;
    LoadingScreenFragment loadingScreenFragment;


    public SearchResultsAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_book, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsAdapter.ViewHolder holder, final int position) {

        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();




        holder.textview_book_title.setText(books.get(position).getTitle());
        if(books.get(position).getThumbnailUrl() != ""){
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
        }
        String author = books.get(position).getSubtitle();
        if(!(author.length()>3)){
            author = "Author(s) unavailable";
        }
        holder.textview_author.setText(author);


        //When one of the cardviews(rows) is clicked, the isbn is stored in the preference file and then checked to see if the book information has already been saved in the databas
        // if its not then it work with the desktop application to collect all the relevent book information and then retirieving and displaying into the Book review screen
        // "Book review" page will be loaded and will use the stored isbn to display the relevent book information
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = ((FragmentActivity)v.getContext()).getSupportFragmentManager();
                loadingScreenFragment = new LoadingScreenFragment();
                bookController = new BookController(v.getContext());
                isbn = books.get(position).getIsbnNumber();
                bookController.storeBookIsbn(v.getContext(), isbn);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, loadingScreenFragment).commit();
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
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            textview_book_title = itemView.findViewById(R.id.textview_search_result_book_title);
            textview_author = itemView.findViewById(R.id.textview_search_result_book_authors);
            imageview_book_thumbnail = itemView.findViewById(R.id.imageview_search_result_book_thumbnail);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }
}
