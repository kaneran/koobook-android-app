package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.koobookandroidapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import controllers.FeatureMatchingController;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;


public class CaptureBookThumbnailFragment extends Fragment {

    private StorageReference storage;
    ProgressDialog progressDialog;
    File photoFile = null;
    String path;
    Uri uri;
    FeatureMatchingController featureMatchingController;

    public CaptureBookThumbnailFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture_book_thumbnail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        storage = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(getContext());

        featureMatchingController = new FeatureMatchingController(getContext());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try{
            photoFile = createImageFile();
            path = photoFile.getAbsolutePath();
            uri = FileProvider.getUriForFile(getContext(),"com.example.koobookandroidapp.fileprovider",photoFile);
        } catch (IOException ex){
            ex.printStackTrace();
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 1);

    }

    private File createImageFile() throws IOException {
        String name = SimpleDateFormat.getDateInstance().toString();
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image =null;
        try {
            image = File.createTempFile(name,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("Error message", e);
        }
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){

            progressDialog.setMessage("Please wait while we try and find the best match");
            progressDialog.show();

            Bitmap bitmap = BitmapFactory.decodeFile(path);

            StorageReference filepath = storage.child("Photos").child(uri.getLastPathSegment());

            // Create file metadata including the content type
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();

            filepath.putFile(uri, metadata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    String fileName = uri.toString();
                    fileName = fileName.substring(71);
                    fileName = fileName.replace("%40","@");
                    featureMatchingController.storeUriDataString(getContext(), fileName);
                    featureMatchingController.execute();
                }
           });
        }
    }
}
