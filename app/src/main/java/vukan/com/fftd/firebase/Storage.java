package vukan.com.fftd.firebase;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

public class Storage {
    private final StorageReference storage;
    private final FirebaseFirestore firestore;

    public Storage() {
        storage = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
    }

    public StorageReference getPostImage(String postID) {
        return storage.child(postID + ".jpg");
    }

    public void updateProfilePicture(String userID, Uri imageUrl) {
        StorageReference filePath = storage.child(userID + UUID.randomUUID().toString());

        filePath.putFile(imageUrl, new StorageMetadata.Builder().setContentType("image/jpg").build())
                .addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> update(userID, uri)));
    }

    public void updateProfilePictureBitmap(String userID, Bitmap imageBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        StorageReference filePath = storage.child(userID + UUID.randomUUID().toString());

        filePath.putBytes(baos.toByteArray(), new StorageMetadata.Builder().setContentType("image/jpg").build())
                .addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> update(userID, uri)));
    }

    private void update(String userID, Uri imageUrl) {
        firestore.collection("users").document(userID).update("imageUrl", imageUrl.toString());
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        builder.setPhotoUri(imageUrl);
        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updateProfile(builder.build());
    }
}