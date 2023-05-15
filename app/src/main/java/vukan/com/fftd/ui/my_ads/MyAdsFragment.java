package vukan.com.fftd.ui.my_ads;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import vukan.com.fftd.GlideApp;
import vukan.com.fftd.R;
import vukan.com.fftd.adapters.CommentsAdapter;
import vukan.com.fftd.adapters.PostRecyclerViewAdapter;
import vukan.com.fftd.models.Comment;
import vukan.com.fftd.models.User;

import static android.app.Activity.RESULT_OK;

public class MyAdsFragment extends Fragment implements PostRecyclerViewAdapter.ListItemClickListener {
    private TextView username;
    private TextView location;
    private TextView phone;
    private ImageView avatar;
    private Button edit;
    private Button rate;
    private Button report;
    private EditText edit_username;
    private EditText edit_location;
    private EditText edit_phone;
    private ConstraintLayout edit_layout;
    private Button save;
    private Button cancel;
    private User current_user;
    private RatingBar starGrade;
    private String userID = "";
    private ConstraintLayout comment_layout;
    private Button commentBtn;
    private EditText comment;
    private Button butCanRate;
    private PostRecyclerViewAdapter adapter;
    private CommentsAdapter adapter2;
    private RecyclerView recikler;
    private RecyclerView recyclerView;
    private Animation mAnimation;
    private MyAdsViewModel myAdsViewModel;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    private Bitmap selectedImageCamera;
    private Uri selectedImageGallery;

    private TextView user_name;
    FirebaseFirestore fStore;
    String user_ID;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_ads, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle((getString(R.string.my_profile)));

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Glide.with(this).load((Bitmap) Objects.requireNonNull(result.getData().getExtras()).get("data")).into(avatar);
                        selectedImageCamera = result.getData().getParcelableExtra("data");
                        selectedImageGallery = null;
                    }
                });

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        try {
                            GlideApp.with(avatar.getContext()).load(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), result.getData().getData())).into(avatar);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        selectedImageGallery = result.getData().getData();
                        selectedImageCamera = null;
                    }
                });

        mAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade);
        mAnimation.setDuration(150);
        myAdsViewModel = new ViewModelProvider(this).get(MyAdsViewModel.class);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PostRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        recikler = view.findViewById(R.id.recikler);
        recikler.setLayoutManager(layoutManager2);
        adapter2 = new CommentsAdapter(new ArrayList<>());
        recikler.setAdapter(adapter2);
        recyclerView.setVisibility(View.VISIBLE);
        recikler.setVisibility(View.INVISIBLE);
        avatar = view.findViewById(R.id.imageAvatar);

        if (getArguments() != null)
            userID = MyAdsFragmentArgs.fromBundle(getArguments()).getUserId();

        if (userID.equals("0")) {
            FirebaseUser fire_user = FirebaseAuth.getInstance().getCurrentUser();
            userID = Objects.requireNonNull(fire_user).getUid();
        }

        avatar.setOnClickListener(view1 -> {
            view1.startAnimation(mAnimation);
            showPopUpMenu(view1);
        });

        avatar.setClickable(false);
        user_name = view.findViewById(R.id.username);
        location = view.findViewById(R.id.location);
        phone = view.findViewById(R.id.phone);
        edit = view.findViewById(R.id.editButton);
        edit_username = view.findViewById(R.id.username_input);
        edit_location = view.findViewById(R.id.location_input);
        edit_phone = view.findViewById(R.id.phone_input);
        edit_layout = view.findViewById(R.id.edit_layout);
        save = view.findViewById(R.id.buttonSave);
        rate = view.findViewById(R.id.rateButton);
        report = view.findViewById(R.id.report_user);
        cancel = view.findViewById(R.id.buttonCancel);
        starGrade = view.findViewById(R.id.starGrades);
        comment_layout = view.findViewById(R.id.commentLayout);
        comment = view.findViewById(R.id.comment);
        butCanRate = view.findViewById(R.id.buttonCancelRate);
        commentBtn = view.findViewById(R.id.commentButton);


//
//        fStore = FirebaseFirestore.getInstance();
//        DocumentReference documentReference = fStore.collection("OrganizerInouts").document(userID);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                username.setText(documentSnapshot.getString("uName"));
//            }
//        });


        myAdsViewModel.getUserRating(userID).observe(getViewLifecycleOwner(), rating -> {
            starGrade.setRating(rating);

            if (current_user != null)
                current_user.setGrade(rating);
        });

        myAdsViewModel.getUser(userID).observe(getViewLifecycleOwner(), user -> {
            username.setText(user.getUsername());
            location.setText(user.getLocation());
            phone.setText(user.getPhone());
            user.setUserID(user.getUserID());

            GlideApp.with(avatar.getContext())
                    .load(user.getImageUrl())
                    .into(avatar);

            if (userID.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                edit.setVisibility(View.VISIBLE);
                report.setVisibility(View.INVISIBLE);
                rate.setVisibility(View.INVISIBLE);
            }

            current_user = user;
        });

        myAdsViewModel.getUserPosts(userID).observe(getViewLifecycleOwner(), posts -> {
            adapter = new PostRecyclerViewAdapter(this);
            adapter.setPosts(posts);
            recyclerView.setAdapter(adapter);
        });

        myAdsViewModel.getUserComments(userID).observe(getViewLifecycleOwner(), userComments -> {
            adapter2 = new CommentsAdapter(userComments);
            recikler.setAdapter(adapter2);
        });

        rate.setOnClickListener(view1 -> {
            rate.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

            if (userID.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()))
                starGrade.setIsIndicator(true);
            else {
                comment_layout.setVisibility(View.VISIBLE);
                comment.setVisibility(View.VISIBLE);
                commentBtn.setVisibility(View.VISIBLE);
                starGrade.setRating(0);
                starGrade.setIsIndicator(false);
            }

            butCanRate.setVisibility(View.VISIBLE);
            recikler.setVisibility(View.VISIBLE);
        });

        report.setOnClickListener(view1 -> new AlertDialog.Builder(requireContext())
                .setTitle(R.string.block_user)
                .setMessage(R.string.block_user_message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Toast.makeText(requireActivity(), R.string.user_blocked, Toast.LENGTH_SHORT).show();
                    myAdsViewModel.reportUser(userID);
                    Navigation.findNavController(view1).navigate(MyAdsFragmentDirections.mojioglasiToPocetnaFragmentAction());
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_report)
                .show());

        butCanRate.setOnClickListener(view1 -> {
            recikler.setVisibility(View.INVISIBLE);
            rate.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            starGrade.setIsIndicator(true);
            comment_layout.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
            commentBtn.setVisibility(View.GONE);
            butCanRate.setVisibility(View.INVISIBLE);
        });

        edit.setOnClickListener(view1 -> {
            recyclerView.setVisibility(View.INVISIBLE);
            username.setVisibility(View.INVISIBLE);
            phone.setVisibility(View.INVISIBLE);
            rate.setVisibility(View.INVISIBLE);
            location.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            starGrade.setVisibility(View.INVISIBLE);
            edit_username.setText(current_user.getUsername());
            edit_location.setText(current_user.getLocation());
            edit_phone.setText(current_user.getPhone());
            edit_layout.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            recikler.setVisibility(View.INVISIBLE);
            avatar.setClickable(true);
        });

        cancel.setOnClickListener(view1 -> {
            edit_layout.setVisibility(View.INVISIBLE);
            save.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            location.setVisibility(View.VISIBLE);
            username.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            starGrade.setVisibility(View.VISIBLE);
            rate.setVisibility(View.VISIBLE);
            avatar.setClickable(false);

            myAdsViewModel.getUser(userID).observe(getViewLifecycleOwner(), user -> GlideApp.with(avatar.getContext())
                    .load(user.getImageUrl())
                    .into(avatar));
        });

        save.setOnClickListener(view1 -> {
            myAdsViewModel.editUserInfo(new User(current_user.getUserID(), edit_username.getText().toString(), edit_location.getText().toString(), current_user.getGrade(), edit_phone.getText().toString(), current_user.getImageUrl()));

            if (selectedImageCamera != null) {
                myAdsViewModel.updateProfilePictureBitmap(selectedImageCamera);
                Toast.makeText(requireContext(), getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();
            }

            if (selectedImageGallery != null) {
                myAdsViewModel.updateProfilePicture(selectedImageGallery);
                Toast.makeText(requireContext(), getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();
            }

            edit_layout.setVisibility(View.INVISIBLE);
            save.setVisibility(View.INVISIBLE);
            starGrade.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            location.setVisibility(View.VISIBLE);
            username.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            avatar.setClickable(false);

            myAdsViewModel.getUser(userID).observe(getViewLifecycleOwner(), user -> {
                username.setText(user.getUsername());
                location.setText(user.getLocation());
                phone.setText(user.getPhone());
                user.setUserID(user.getUserID());

                GlideApp.with(avatar.getContext())
                        .load(user.getImageUrl())
                        .into(avatar);

                current_user = user;
            });
        });

        commentBtn.setOnClickListener(view1 -> {
            if (comment.getText().toString().trim().isEmpty())
                Toast.makeText(requireActivity(), R.string.comment_warning, Toast.LENGTH_SHORT).show();
            else {
                FirebaseUser fire_user = FirebaseAuth.getInstance().getCurrentUser();
                String fromUserID = Objects.requireNonNull(fire_user).getUid();
                Comment newComment = new Comment(fromUserID, userID, comment.getText().toString(), starGrade.getRating());
                myAdsViewModel.addNewUserComment(newComment);
                comment_layout.setVisibility(View.GONE);
                comment.setVisibility(View.GONE);
                commentBtn.setVisibility(View.GONE);
                starGrade.setIsIndicator(true);

                myAdsViewModel.getUserComments(userID).observe(getViewLifecycleOwner(), userComments -> {
                    adapter2 = new CommentsAdapter(userComments);
                    recikler.setAdapter(adapter2);
                });

                myAdsViewModel.getUserRating(userID).observe(getViewLifecycleOwner(), rating -> {
                    starGrade.setRating(rating);
                    if (current_user != null) current_user.setGrade(rating);
                });
            }
        });
    }

    private void showPopUpMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.camera_upload) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                cameraActivityResultLauncher.launch(
                        intent,
                        ActivityOptionsCompat.makeCustomAnimation(
                                requireContext(),
                                R.anim.fade_in,
                                R.anim.fade_out
                        )
                );
            } else if (item.getItemId() == R.id.file_upload) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                galleryActivityResultLauncher.launch(
                        Intent.createChooser(
                                intent,
                                getString(R.string.choose_picture)
                        ),
                        ActivityOptionsCompat.makeCustomAnimation(
                                requireContext(),
                                R.anim.fade_in,
                                R.anim.fade_out
                        )
                );
            }

            return true;
        });

        popupMenu.show();
    }

    @Override
    public void onListItemClick(String postID) {
        MyAdsFragmentDirections.MojioglasiToProizvodFragmentAction action = MyAdsFragmentDirections.mojioglasiToProizvodFragmentAction();
        action.setPostId(postID);
        Navigation.findNavController(requireView()).navigate(action);
    }


}