package com.example.fiszki;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import xdroid.toaster.Toaster;

public final class StorageFirebase extends AppCompatActivity {

    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Images");
    private static FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private static Uri uriImage;
    private static String extensionImg;


    public static String fileuploaderfromUri(Uri uri, String getExtensionImg) {
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

        public static void deleteImage(Uri nameImage){
            // Create a reference to the file to delete
            StorageReference desertRef = firebaseStorage.getReferenceFromUrl(String.valueOf(nameImage));
            // Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toaster.toast("Zdjęcie zostało podmienione");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });
        }

}

