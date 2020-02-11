package fragments;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import java.util.ArrayList;
import java.util.List;

import adapters.RecentBooksAdapter;
import controllers.BookController;
import controllers.UserController;
import dataaccess.setup.AppDatabase;
import entities.Book;


/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    TextView[] dots;
    LinearLayout dots_layout;
    RecyclerView recyclerView;
    LinearLayout layout_books_that_needs_reviewing;
    LinearLayout layout_books_liked;
    RecentBooksAdapter recentlyScannedBooksAdapter;
    BookController bookController;
    UserController userController;
    AppDatabase db;
    BookListByStatusFragment bookListByStatusFragment;
    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookController = new BookController(view.getContext());
        userController = new UserController();
        bookListByStatusFragment = new BookListByStatusFragment();
        layout_books_liked = view.findViewById(R.id.layout_liked_books);
        dots_layout = view.findViewById(R.id.library_dot_layout);
        layout_books_that_needs_reviewing = view.findViewById(R.id.layout_books_that_needs_reviewing);
        db = Room.databaseBuilder(view.getContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        recyclerView = view.findViewById(R.id.recyclerview_recently_scanned_books);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view);

        int userId = userController.getUserIdFromSharedPreferneces(view.getContext());
        List<Integer> auditIds = db.auditDao().getAuditIds(userId);
        List<Book> books = bookController.getBooksFromAuditIds(auditIds);
        List<Book> recentBooks = new ArrayList<>();
        addDotsIndicator();

        if(books.size()>10) {
            for (int i = 0; i < 10; i++) {
                recentBooks.add(books.get(i));
            }
        }
        recentlyScannedBooksAdapter = new RecentBooksAdapter(recentBooks, view.getContext(), bottomNavigationView);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.setAdapter(recentlyScannedBooksAdapter);

        layout_books_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookController.storeBookListType(v.getContext(), BookController.BookListType.Liked);
                getParentFragment().getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, bookListByStatusFragment).commit();
                bottomNavigationView.getMenu().setGroupCheckable(0,false, true);
            }
        });

        layout_books_that_needs_reviewing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookController.storeBookListType(v.getContext(), BookController.BookListType.NeedsReviewing);
                getParentFragment().getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, bookListByStatusFragment).commit();
                bottomNavigationView.getMenu().setGroupCheckable(0,false, true);
            }
        });
    }

    //This method creates three horizontal dots and adds that to the view of the dot layout(linear layout). One of the dots is color differently to create the impression that the user can slide between the different pages.
    public void addDotsIndicator(){
        dots = new TextView[3];
        for(int i =0; i< dots.length; i++){
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            if(i==0) {
                dots[i].setTextColor(getResources().getColor(R.color.lightBlue));
            } else{
                dots[i].setTextColor(getResources().getColor(R.color.grey));
            }
            dots_layout.addView(dots[i]);
        }
    }
}
