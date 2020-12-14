package com.example.fiszki;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageFirebase extends AppCompatActivity {

    private StorageReference mStorageRef;
    Uri uriImage;
    String extensionImg;

    public StorageFirebase() {
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
    }

    public String fileuploaderfromUri(Uri uri, String getExtensionImg) {
        if (uri != null && !getExtensionImg.equals(" ")) {
            uriImage = uri;
            extensionImg = getExtensionImg;
            String imageId;
            imageId = System.currentTimeMillis() + "." + getExtensionImg;
            StorageReference riversRef = mStorageRef.child(imageId);

            riversRef.putFile(uriImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
            return imageId;
        } else
            return " ";
        }

}

