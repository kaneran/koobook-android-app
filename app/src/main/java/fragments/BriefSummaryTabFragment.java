package fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.koobookandroidapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BriefSummaryTabFragment extends Fragment {

    TextView textview_reviews;
    String[] reviews;

    public BriefSummaryTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_brief_summary_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviews = new String[20];
        reviews[0]= "Amazing";
        reviews[1]= "Sick plays";

        textview_reviews = getView().findViewById(R.id.textview_reviews);
        textview_reviews.setText("\""+reviews[0]+"\"\n\n\""+reviews[1]+"\"");
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
