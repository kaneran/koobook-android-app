package fragments;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.koobookandroidapp.R;

import activities.LoginActivity;
import controllers.UserController;
import dataaccess.setup.AppDatabase;
import entities.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    LoginActivity loginActivity;
    TextView textview_welcome_msg;
    UserController userController;
    AppDatabase db;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userController = new UserController();
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "production").allowMainThreadQueries().build();
        int userId = userController.getUserIdFromSharedPreferneces(getContext());
        String firstName = db.userDao().getUsersName(userId);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textview_welcome_msg = view.findViewById(R.id.textview_welcome_msg);

        textview_welcome_msg.setText("Welcome " + firstName);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}
