package dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.example.koobookandroidapp.R;

import controllers.BookController;

//Credit to Coding in Flow for the tutorial- https://www.youtube.com/watch?v=Bsm-BlXo2SI
public class LikeBookDialog extends AppCompatDialogFragment {
    BookController bookController;

    //This methods creates the dialog to allow the user to confirm on whether the intentionally liked the book. If the user selects "Yes" from the dialog then the Book controller is used to
    //execute workflow for liking the book which involves create/updating certain tables in the Room database. After it executes the workflow then a toast message is displayed to notify the user
    //that the book has been liked. If user instead selected "No" then the dialog will close without any effects made in the Room database.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bookController = new BookController(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        builder.setTitle("Liked the book?")
                .setMessage("Are you sure you want to like the book?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Create/Update new audit record
                        bookController.likeBook();
                        dialog.dismiss();
                        Toast.makeText(getContext(), "This book has been liked", Toast.LENGTH_SHORT).show();
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
