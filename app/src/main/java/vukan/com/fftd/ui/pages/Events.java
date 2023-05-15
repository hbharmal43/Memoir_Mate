//package vukan.com.fftd;
//
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//import android.app.Dialog;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//public class Events extends AppCompatActivity {
//
//    private static final int PReqCode = 2 ;
//    private Uri imageUri = null;
//    FirebaseAuth mAuth;
//    FirebaseUser current_user;
//    ImageView popupPostImage;
//    EditText popupTitle;
//    EditText popupDescription;
//    CheckBox popupCheckBox;
//    Button popupButton;
//    Dialog popAddPost;
//    ActivityResultLauncher<String> galleryActivityLauncher;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mAuth = FirebaseAuth.getInstance();
//        current_user = mAuth.getCurrentUser();
//        popUp();
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popAddPost.show();
//            }
//
//        });
//        galleryActivityLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
//                new ActivityResultCallback<Uri>() {
//                    @Override
//                    public void onActivityResult(Uri result) {
//                        if(result!=null)
//                            imageUri = result;
//                    }
//                });
//
//    }
//    private void checkAndRequestForPermission() {
//        if (
//                ContextCompat.checkSelfPermission(Events.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Events.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                Toast.makeText(Events.this,"Requesting permission to access photos",Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                ActivityCompat.requestPermissions(Events.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        PReqCode);
//            }
//
//        }
//        else
//            // everything goes well : we have permission to access user gallery
//            openGallery();
//
//    }
//    private void openGallery() {
//        galleryActivityLauncher.launch("image/*");
//    }
//
//    private void popUp() {
//        popAddPost = new Dialog(this);
//        popAddPost.setContentView(R.layout.popup_add_post);
//        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));;
//
//        // ini popup widgets
//        popupPostImage = popAddPost.findViewById(R.id.popupIMG);
//        popupTitle = popAddPost.findViewById(R.id.popupTitle);
//        popupDescription = popAddPost.findViewById(R.id.popupDescription);
//        popupCheckBox = popAddPost.findViewById(R.id.popupCheckBox);
//        popupButton = popAddPost.findViewById(R.id.popupButton);
//
//        popupPostImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkAndRequestForPermission();
//                popupPostImage.setImageURI(imageUri);
//            }
//        });
//        popupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popupButton.setVisibility(View.INVISIBLE);
//
//                if(!popupTitle.getText().toString().isEmpty()
//                        &&!popupDescription.getText().toString().isEmpty()
//                        &&popupPostImage!=null){
//                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Event_Image");
//                    final StorageReference imagePath = storageReference.child(imageUri.getLastPathSegment());
//                    imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    String imageDLLink = uri.toString();
//                                    Post newPost = new Post(popupTitle.getText().toString(),
//                                            popupDescription.getText().toString(),
//                                            imageDLLink,
//                                            current_user.getUid(),
//                                            null,
//                                            popupCheckBox.isChecked());
//                                    addPost(newPost);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(Events.this,e.getMessage(),Toast.LENGTH_LONG).show();
//                                    popupButton.setVisibility(View.VISIBLE);
//                                }
//                            });
//                        }
//                    });
//                }
//                else{
//                    Toast.makeText(Events.this,"Please fill in all inputs",Toast.LENGTH_LONG).show();
//                    popupButton.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//    }
//    private void addPost(Post post){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("Posts").push();
//
//        String key = ref.getKey();
//        post.setPostKey(key);
//
//
//        // add post data to firebase database
//
//        ref.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(Events.this,"Post Added successfully",Toast.LENGTH_LONG).show();
//                popupButton.setVisibility(View.VISIBLE);
//                popAddPost.dismiss();
//            }
//        });
//    }
//}