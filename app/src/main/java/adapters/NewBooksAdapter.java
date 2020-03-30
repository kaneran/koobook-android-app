package adapters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.koobookandroidapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.Map;

import controllers.BookController;
import dataaccess.setup.AppDatabase;
import entities.Book;
import fragments.BookReviewFragment;



public class NewBooksAdapter extends RecyclerView.Adapter<NewBooksAdapter.ViewHolder>{
    Object[] books;
    Book book;
    AppDatabase db;
    Context context;
    View view;
    RecyclerView recyclerView;

    public NewBooksAdapter(Object[] books, Context context, View view, RecyclerView recyclerView) {
        this.books = books;
        this.context = context;
        this.view = view;
        this.recyclerView = recyclerView;
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
    }

    @NonNull
    @Override
    public NewBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommended_book, viewGroup,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NewBooksAdapter.ViewHolder holder, final int position) {

        final BookController bookController = new BookController(context);
        book = ((Map.Entry<Book, Integer>) books[position]).getKey();

        int page_count = book.getPageCount();

        String color = db.colorDao().getBackgroundColorString(book.getBookId());
        if(color != null){
            ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(color));
            ViewCompat.setBackgroundTintList(holder.imageview_book_thumbnail, colorStateList);
        }

        if(page_count>0) {
            holder.imageview_book_thumbnail.getLayoutParams().height = (page_count);
            holder.imageview_book_thumbnail.getLayoutParams().width = (page_count);
        } else{
            holder.imageview_book_thumbnail.getLayoutParams().height = 100;

            holder.imageview_book_thumbnail.getLayoutParams().width = 100;
           }

        //When one of the cardviews(rows) is clicked, the isbn is stored in the preference file and then
        // used it to display the brief information in the Book recommendations screen
        holder.imageview_book_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button button_view_more = view.findViewById(R.id.button_recommended_book_view_more);

                displayBookThumbnail(v, position);

                bookController.storeBookIsbn(context, book.getIsbnNumber());
                bookController.displayBookInformationInRecommendedBooksPage(view);


                //Assign on click listener to "View more" button in toast message
                //When the button is clicked, the book isbn would be used to display all of its information in the Book review page
                button_view_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = ((FragmentActivity)v.getContext()).getSupportFragmentManager();
                        BookReviewFragment bookReviewFragment = new BookReviewFragment();
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, bookReviewFragment).commit();
                    }
                });


            }
        });
    }

    public void displayBookThumbnail(View v, int position){

        ImageView imageview_book_thumbnail = v.findViewById(R.id.imageview_new_book);

        book = ((Map.Entry<Book, Integer>) books[position]).getKey();
        //Check if the book has a valid thumbnail url, if it does not then override the thumbnail url string to be that of the default thumbnail url
        String bookThumbnailUrl = book.getThumbnailUrl();
        if(bookThumbnailUrl.matches("")){
            bookThumbnailUrl = "https://i.gyazo.com/a1b02a68b87056cb4469a6bcb6785932.png";
        }
        //Use picasso to download the image using the url and load it into the image view
        Picasso.with(context).load(bookThumbnailUrl).resize(200,400).centerCrop().into(imageview_book_thumbnail, new Callback() {
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

    @Override
    public int getItemCount() {
        return books.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageview_book_thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            imageview_book_thumbnail = itemView.findViewById(R.id.imageview_new_book);
        }
    }

    public void insertColour(){
        db.colorDao().updateUserEmail(31,"#1C1913");
        db.colorDao().updateUserEmail(32,"#1D0A09");
        db.colorDao().updateUserEmail(108,"#171717");
        db.colorDao().updateUserEmail(64,"#787676");
        db.colorDao().updateUserEmail(55,"#CEE2C9");
        db.colorDao().updateUserEmail(5,"#4C3F3E");
        db.colorDao().updateUserEmail(59,"#BB2225");
        db.colorDao().updateUserEmail(25,"#1B3E3B");
        db.colorDao().updateUserEmail(42,"#A2AE9E");
        db.colorDao().updateUserEmail(22,"#E8D6A7");
        db.colorDao().updateUserEmail(33,"#E0E0E2");
        db.colorDao().updateUserEmail(12,"#22252B");
        db.colorDao().updateUserEmail(76,"#EDF3F7");
        db.colorDao().updateUserEmail(28,"#AEC2C0");

    }
}
