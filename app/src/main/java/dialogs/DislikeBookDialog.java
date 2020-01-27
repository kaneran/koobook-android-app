package dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.example.koobookandroidapp.R;

import controllers.BookController;

public class DislikeBookDialog extends AppCompatDialogFragment {

    BookController bookController;
    String[] choices;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        choices = new String[2];
        choices[0]= "Didn't like the genre";
        choices[1]= "Lost interest";
        bookController = new BookController(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        builder.setTitle("Disiked the book?")
                .setMessage("Are you sure you want to dislike the book?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Create/Update new audit record
                        dialog.dismiss();
                        WhyDislikeBookDialog whyDislikeBookDialog = new WhyDislikeBookDialog();
                        whyDislikeBookDialog.show(getFragmentManager(), "Why dislike book dialog");
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Close dialog

            }
        });
        return builder.create();
    }
}
