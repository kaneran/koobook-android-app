package adapters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.koobookandroidapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import controllers.BookController;
import dataaccess.setup.AppDatabase;
import entities.Book;
import fragments.BookReviewFragment;

public class NewBooksAdapter extends RecyclerView.Adapter<NewBooksAdapter.ViewHolder>{
    List<Book> books;
    Book book;
    AppDatabase db;
    Context context;
    BookController bookController;
    FragmentManager fragmentManager;
    String isbn;
    BookReviewFragment bookReviewFragment;
    BottomNavigationView bottomNavigationView;

    public NewBooksAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context = context;
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
    }

    @NonNull
    @Override
    public NewBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_book, viewGroup,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NewBooksAdapter.ViewHolder holder, final int position) {
        book = books.get(position);

        int page_count = book.getPageCount();

        //scale the image view based on the number of pages
        holder.imageview_book_thumbnail.getLayoutParams().height += (page_count *1);

        holder.imageview_book_thumbnail.getLayoutParams().width += (page_count *1);

        //When one of the cardviews(rows) is clicked, the isbn is stored in the preference file and then checked to see if the book information has already been saved in the database
        // if its not then it work with the desktop application to collect all the relevent book information and then retirieving and displaying into the Book review screen
        // "Book review" page will be loaded and will use the stored isbn to display the relevent book information
        holder.imageview_book_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageview_book_thumbnail = v.findViewById(R.id.imageview_new_book);

                book = books.get(position);
                //Check if the book has a valid thumbnail url, if it does not then override the thumbnail url string to be that of the default thumbnail url
                String bookThumbnailUrl = book.getThumbnailUrl();
                if(bookThumbnailUrl.matches("")){
                    bookThumbnailUrl = "https://i.gyazo.com/a1b02a68b87056cb4469a6bcb6785932.png";
                }
                //Use picasso to download the image using the url and load it into the image view
                Picasso.with(context).load(bookThumbnailUrl).resize(200,400).centerInside().into(imageview_book_thumbnail, new Callback() {
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
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageview_book_thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            imageview_book_thumbnail = itemView.findViewById(R.id.imageview_new_book);
        }
    }
}
