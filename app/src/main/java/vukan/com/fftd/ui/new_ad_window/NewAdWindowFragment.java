package vukan.com.fftd.ui.new_ad_window;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import vukan.com.fftd.GlideApp;
import vukan.com.fftd.R;
import vukan.com.fftd.firebase.Storage;
import vukan.com.fftd.models.Post;
import vukan.com.fftd.models.PostImage;
import vukan.com.fftd.ui.post.PostViewModel;

import static android.app.Activity.RESULT_OK;

public class NewAdWindowFragment extends Fragment {
    private CheckBox fiksna;
    private RadioGroup radioValutaGroup;
    private RadioButton radioCurrentButton;
    private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private EditText naslov;
    private EditText cena;
    private EditText opis;
    private int counter = 0;
    private Uri filePath;
    private Uri filePath1;
    private Uri filePath2;
    private Animation mAnimation;
    private String Post_ID;
    private Uri filePath3;
    private Uri filePath4;
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Storage firebaseStorage;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private String uuid;
    private int category;
    private String PostID;
    private StorageReference storageReference;
    private Post newPost;
    private List<PostImage> PostImageList;
    private NewAdWindowViewModel newAdWindowViewModel;
    private Button btn_delete;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_ad_window, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle(getString(R.string.title_create));

        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getExtras() != null && result.getData().getExtras().get("data") != null) {
                        Bitmap slika = (Bitmap) result.getData().getExtras().get("data");

                        if (counter == 0) {
                            filePath = null;
                            bitmap = slika;
                            GlideApp.with(imageView.getContext()).load(bitmap).into(imageView);
                        } else if (counter == 1) {
                            filePath1 = null;
                            bitmap1 = slika;
                            GlideApp.with(imageView1.getContext()).load(bitmap1).into(imageView1);
                        } else if (counter == 2) {
                            filePath2 = null;
                            bitmap2 = slika;
                            GlideApp.with(imageView2.getContext()).load(bitmap2).into(imageView2);
                        } else if (counter == 3) {
                            filePath3 = null;
                            bitmap3 = slika;
                            GlideApp.with(imageView3.getContext()).load(bitmap3).into(imageView3);
                        } else if (counter == 4) {
                            filePath4 = null;
                            bitmap4 = slika;
                            GlideApp.with(imageView4.getContext()).load(bitmap4).into(imageView4);
                        }

                        if (counter < 5) {
                            counter++;
                            btn_delete.setEnabled(true);
                        }
                    }
                });

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        try {
                            if (counter == 0) {
                                filePath = result.getData().getData();
                                GlideApp.with(imageView.getContext()).load(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath)).into(imageView);
                            } else if (counter == 1) {
                                filePath1 = result.getData().getData();
                                GlideApp.with(imageView1.getContext()).load(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath1)).into(imageView1);
                            } else if (counter == 2) {
                                filePath2 = result.getData().getData();
                                GlideApp.with(imageView2.getContext()).load(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath2)).into(imageView2);
                            } else if (counter == 3) {
                                filePath3 = result.getData().getData();
                                GlideApp.with(imageView3.getContext()).load(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath3)).into(imageView3);
                            } else if (counter == 4) {
                                filePath4 = result.getData().getData();
                                GlideApp.with(imageView4.getContext()).load(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath4)).into(imageView4);
                            }

                            if (counter < 5) {
                                counter++;
                                btn_delete.setEnabled(true);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        firebaseStorage = new Storage();
        PostImageList = new ArrayList<>();
        newPost = new Post();
        FirebaseUser fire_user = FirebaseAuth.getInstance().getCurrentUser();
        newAdWindowViewModel = new ViewModelProvider(this).get(NewAdWindowViewModel.class);
        PostViewModel PostViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        mAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade);
        mAnimation.setDuration(150);
        Button btn_choose = view.findViewById(R.id.btn_choose);
        Button btn_choosecam = view.findViewById(R.id.btn_choosecam);
        Button btn_add_new_Post = view.findViewById(R.id.add_new_post);
        btn_delete = view.findViewById(R.id.btn_deletephoto);
        imageView = view.findViewById(R.id.myImage);
        imageView1 = view.findViewById(R.id.myImage1);
        imageView2 = view.findViewById(R.id.myImage2);
        imageView3 = view.findViewById(R.id.myImage3);
        imageView4 = view.findViewById(R.id.myImage4);
        naslov = view.findViewById(R.id.naslov);
        cena = view.findViewById(R.id.cena);
        opis = view.findViewById(R.id.opis);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (getArguments() != null) {
            Post_ID = NewAdWindowFragmentArgs.fromBundle(getArguments()).getPostId();

            if (!Post_ID.equals("0")) {
                PostViewModel.getPostDetails(Post_ID).observe(getViewLifecycleOwner(), Post -> {
                    naslov.setText(Post.getName());
                    cena.setText(String.format("%s", Post.getPrice().toString()));
                    opis.setText(Post.getDescription());
                    newPost.setHomePhotoUrl(Post.getHomePhotoUrl());
                    category = Integer.parseInt(Post.getCategoryID());


                    PostViewModel.getPostImages(Post_ID).observe(getViewLifecycleOwner(), PostImages -> {
                        counter = PostImages.size();
                        btn_delete.setEnabled(true);
                        PostImageList.addAll(PostImages);

                        for (int i = 0; i < PostImages.size(); i++) {
                            if (i == 0)
                                GlideApp.with(imageView.getContext()).load(firebaseStorage.getPostImage(PostImages.get(0).getImageUrl())).into(imageView);
                            else if (i == 1)
                                GlideApp.with(imageView1.getContext()).load(firebaseStorage.getPostImage(PostImages.get(1).getImageUrl())).into(imageView1);
                            else if (i == 2)
                                GlideApp.with(imageView2.getContext()).load(firebaseStorage.getPostImage(PostImages.get(2).getImageUrl())).into(imageView2);
                            else if (i == 3)
                                GlideApp.with(imageView3.getContext()).load(firebaseStorage.getPostImage(PostImages.get(3).getImageUrl())).into(imageView3);
                            else
                                GlideApp.with(imageView4.getContext()).load(firebaseStorage.getPostImage(PostImages.get(4).getImageUrl())).into(imageView4);
                        }
                    });
                });
            }
        }

        btn_add_new_Post.setOnClickListener(view3 -> {
            if (opis != null && opis.getText().toString().trim().length() > 0 && cena.getText().toString().trim().length() > 0 && naslov.getText().toString().trim().length() > 0 && counter > 0) {
                view3.startAnimation(mAnimation);

                if (!Post_ID.equals("0"))
                    Toast.makeText(getActivity(), R.string.azuriranje_proizvoda, Toast.LENGTH_SHORT).show();

                if (counter > 0) {
                    if (filePath == null) uploadImageBitmap(bitmap);
                    else uploadImage(filePath);
                    if (bitmap != null || filePath != null) newPost.setHomePhotoUrl(uuid);
                }

                Date date = new Date();
                newPost.setDatetime(new Timestamp(date));
                newPost.setCategoryID("2");
                newPost.setDescription(opis.getText().toString());
                newPost.setName(naslov.getText().toString());
                newPost.setPrice(Double.parseDouble(cena.getText().toString()));
                long l = 0;
                newPost.setSeen(l);
                newPost.setPostID("temp");
                newPost.setUserID(Objects.requireNonNull(fire_user).getUid());

                if (getArguments() != null) {
                    category = NewAdWindowFragmentArgs.fromBundle(getArguments()).getId();

                    if (category != 0)
                        newPost.setCategoryID(category + "");
                }

                PostID = newAdWindowViewModel.addPost(newPost, Post_ID);

                if (Post_ID.equals("0")) {
                    PostImage pi = new PostImage();
                    pi.setImageUrl(uuid);
                    pi.setPostID(PostID);
                    newAdWindowViewModel.addPostImage(pi);
                    PostImageList.add(pi);
                }

                if (counter == 2) {
                    if (filePath1 == null) uploadImageBitmap(bitmap1);
                    else uploadImage(filePath1);
                    newAdWindowViewModel.addPostImage(addPI(uuid, PostID));
                } else if (counter == 3) {
                    if (filePath1 == null) uploadImageBitmap(bitmap1);
                    else uploadImage(filePath1);
                    newAdWindowViewModel.addPostImage(addPI(uuid, PostID));
                    if (filePath2 == null) uploadImageBitmap(bitmap2);
                    else uploadImage(filePath2);
                    newAdWindowViewModel.addPostImage(addPI(uuid, PostID));
                } else if (counter == 4) {
                    if (filePath1 == null) uploadImageBitmap(bitmap1);
                    else uploadImage(filePath1);
                    newAdWindowViewModel.addPostImage(addPI(uuid, PostID));
                    if (filePath2 == null) uploadImageBitmap(bitmap2);
                    else uploadImage(filePath2);
                    newAdWindowViewModel.addPostImage(addPI(uuid, PostID));
                    if (filePath3 == null) uploadImageBitmap(bitmap3);
                    else uploadImage(filePath3);
                    newAdWindowViewModel.addPostImage(addPI(uuid, PostID));
                } else if (counter == 5) {
                    if (filePath1 == null) uploadImageBitmap(bitmap1);
                    else uploadImage(filePath1);
                    newAdWindowViewModel.addPostImage(addPI(uuid, PostID));
                    if (filePath2 == null) uploadImageBitmap(bitmap2);
                    else uploadImage(filePath2);
                    newAdWindowViewModel.addPostImage(addPI(uuid, PostID));
                    if (filePath3 == null) uploadImageBitmap(bitmap3);
                    else uploadImage(filePath3);
                    newAdWindowViewModel.addPostImage(addPI(uuid, PostID));
                    if (filePath4 == null) uploadImageBitmap(bitmap4);
                    else uploadImage(filePath4);
                    newAdWindowViewModel.addPostImage(addPI(uuid, PostID));
                }

                Navigation.findNavController(view3).navigate(NewAdWindowFragmentDirections.novioglasprozorToPocetnaFragmentAction());
            } else Toast.makeText(getActivity(), R.string.upozorenje, Toast.LENGTH_SHORT).show();
        });

        btn_delete.setEnabled(false);

        btn_delete.setOnClickListener(view4 -> {
            view4.startAnimation(mAnimation);
            deleteImage();
        });

        btn_choosecam.setOnClickListener(view5 -> {
            if (counter < 5) {
                view5.startAnimation(mAnimation);
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                cameraActivityResultLauncher.launch(camera_intent, ActivityOptionsCompat.makeCustomAnimation(
                        requireContext(),
                        R.anim.fade_in,
                        R.anim.fade_out
                ));
            }
        });

        btn_choose.setOnClickListener(view6 -> {
            if (counter < 5) {
                view6.startAnimation(mAnimation);
                chooseImage();
            }
        });
    }

    private PostImage addPI(String uuid, String PostID) {
        PostImage pi = new PostImage();
        pi.setImageUrl(uuid);
        pi.setPostID(PostID);
        PostImageList.add(pi);
        return pi;
    }

    private void deleteImage() {
        if (counter - 1 < PostImageList.size()) {
            newAdWindowViewModel.deletePostImage(PostImageList.get(counter - 1).getImageUrl());
            PostImageList.remove(counter - 1);
        }

        if (counter == 1) imageView.setImageBitmap(null);
        else if (counter == 2) imageView1.setImageBitmap(null);
        else if (counter == 3) imageView2.setImageBitmap(null);
        else if (counter == 4) imageView3.setImageBitmap(null);
        else if (counter == 5) imageView4.setImageBitmap(null);
        counter--;
        if (counter < 0) counter = 0;
        if (counter == 0) btn_delete.setEnabled(false);
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        galleryActivityResultLauncher.launch(Intent.createChooser(intent, getString(R.string.izaberite_sliku)), ActivityOptionsCompat.makeCustomAnimation(
                requireContext(),
                R.anim.fade_in,
                R.anim.fade_out
        ));
    }

    private void uploadImage(Uri fajl) {
        if (fajl != null) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(getString(R.string.oglas_naslov));
            progressDialog.show();
            uuid = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("" + uuid + ".jpg");

            ref.putFile(fajl)
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), R.string.oglas_postavljen, Toast.LENGTH_SHORT).show();
                            })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), getString(R.string.greska) + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage(getString(R.string.otpremljeno) + (int) progress + "%");
                            });
        }
    }

    private void uploadImageBitmap(Bitmap fajl) {
        if (fajl != null) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(getString(R.string.oglas_naslov));
            progressDialog.show();
            uuid = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("" + uuid + ".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            fajl.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            ref.putBytes(baos.toByteArray(), new StorageMetadata.Builder().setContentType("image/jpg").build())
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), getString(R.string.oglas_postavljen), Toast.LENGTH_SHORT).show();
                            })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), getString(R.string.greska) + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage(getString(R.string.otpremljeno) + (int) progress + "%");
                            });
        }
    }
}