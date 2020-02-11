package dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.example.koobookandroidapp.R;

import controllers.BookController;

public class WhyDislikeBookDialog extends AppCompatDialogFragment {

    BookController bookController;
    String[] choices;
    String selectedChoice;

    //This methods creates the dialog to prompt the user to provide a reason for disliking a book. If the user selects an option and clicks "OK" on the dialog then the Book controller is used to
    //execute workflow for disliking the book which involves passing the selected option into the method that executes this workflow. The execution of the workflow involves create/updating certain tables in
    //the Room database. After it executes the workflow then a toast message is displayed to notify the user that the book has been disliked.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        choices = new String[]{"Didn't like the genre","Lost interest"};
        bookController = new BookController(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        builder.setTitle("Why dislike this book? \n Choose one option")
                .setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedChoice = choices[which];
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Create/Update new audit record
                        bookController.dislikeBook(selectedChoice);
                        dialog.dismiss();
                        Toast.makeText(getContext(), "This book has been disliked", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }
}
