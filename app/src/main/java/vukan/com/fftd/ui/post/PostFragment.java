package vukan.com.fftd.ui.post;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import vukan.com.fftd.GlideApp;
import vukan.com.fftd.R;
import vukan.com.fftd.adapters.PostImageRecyclerViewAdapter;

public class PostFragment extends Fragment {
    private PostImageRecyclerViewAdapter adapter;
    private String postID = "0";
    private TextView nazivProizvoda;
    private TextView opisProizvoda;
    private TextView cenaProizvoda;
    private TextView vidjeno;
    private TextView lokacija;
    private TextView datumObjavljivanja;
    private Button poruke;
    private Button pozovi;
    private AppCompatImageButton delete;
    private AppCompatImageButton report;
    private AppCompatImageButton edit;
    private String phoneNumber;
    private String userID;
    private String fixPrice;
    private AdView mAdView;
    private SimpleDateFormat sfd;
    private TextView username;
    private CircleImageView userImage;
    private PostViewModel postViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        if (getArguments() != null) {
            postID = PostFragmentArgs.fromBundle(getArguments()).getPostId();
            if (!postID.equals("0")) postViewModel.incrementCounter(postID);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SliderView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new PostImageRecyclerViewAdapter(new ArrayList<>());
        sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        recyclerView.setSliderAdapter(adapter);
        recyclerView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        recyclerView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        recyclerView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        recyclerView.setIndicatorSelectedColor(R.color.primary_light);
        recyclerView.setIndicatorUnselectedColor(R.color.colorPrimaryDark);
        recyclerView.setScrollTimeInSec(4);
        recyclerView.startAutoCycle();
        nazivProizvoda = view.findViewById(R.id.naziv_proizvoda);
        opisProizvoda = view.findViewById(R.id.opis_proizvoda);
        cenaProizvoda = view.findViewById(R.id.cena_proizvoda);
        datumObjavljivanja = view.findViewById(R.id.datum_objavljivanja);
        vidjeno = view.findViewById(R.id.vidjeno);
        poruke = view.findViewById(R.id.poruke);
        pozovi = view.findViewById(R.id.pozovi);
        userImage = view.findViewById(R.id.userImage);
        lokacija = view.findViewById(R.id.lokacija);
        username = view.findViewById(R.id.userName);
        delete = view.findViewById(R.id.delete);
        report = view.findViewById(R.id.report_post);
        edit = view.findViewById(R.id.edit);
        Animation mAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade);
        mAnimation.setDuration(150);

        delete.setOnClickListener(view1 -> {
            view.startAnimation(mAnimation);

            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.delete_post)
                    .setMessage(R.string.confirm)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        Toast.makeText(requireActivity(), R.string.deleted, Toast.LENGTH_SHORT).show();
                        postViewModel.deletePost(postID);
                        requireActivity().onBackPressed();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.drawable.ic_delete)
                    .show();
        });

        report.setOnClickListener(view1 -> new AlertDialog.Builder(requireContext())
                .setTitle(R.string.report_ad)
                .setMessage(R.string.report_ad_confirm)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    postViewModel.reportAd(postID);
                    Toast.makeText(requireActivity(), R.string.ad_reported, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_report)
                .show());

        edit.setOnClickListener(view1 -> {
            view1.startAnimation(mAnimation);
            PostFragmentDirections.ProizvodToNoviOglasProzorFragmentAction action = PostFragmentDirections.proizvodToNoviOglasProzorFragmentAction();
            action.setPostId(postID);
            Navigation.findNavController(view1).navigate(action);
        });

        userImage.setOnClickListener(view1 -> {
            view1.startAnimation(mAnimation);
            PostFragmentDirections.ProizvodToMojiOglasiFragmentAction action = PostFragmentDirections.proizvodToMojiOglasiFragmentAction();
            action.setUserId(userID);
            Navigation.findNavController(view1).navigate(action);
        });

        MobileAds.initialize(requireContext(), initializationStatus -> {
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        poruke.setOnClickListener(view1 -> {
            PostFragmentDirections.ProizvodToPorukeFragmentAction action = PostFragmentDirections.proizvodToPorukeFragmentAction(null);
            action.setPostId(postID);
            Navigation.findNavController(requireView()).navigate(action);
        });

        pozovi.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));

            startActivity(intent, ActivityOptions.makeCustomAnimation(
                    requireContext(),
                    R.anim.fade_in,
                    R.anim.fade_out
            ).toBundle());
        });

        postViewModel.getPostDetails(postID).observe(getViewLifecycleOwner(), post -> {
            nazivProizvoda.setText(post.getName());
            opisProizvoda.setText(post.getDescription());
            postViewModel.getCategory(post.getCategoryID()).observe(getViewLifecycleOwner(), category -> requireActivity().setTitle(category.getName()));
            cenaProizvoda.setText(String.format(getString(R.string.cena) + ": %s", post.getPrice().toString()));
            datumObjavljivanja.setText(String.format(getString(R.string.published) + ": %s", sfd.format(post.getDatetime().toDate())));
            vidjeno.setText(String.format(getString(R.string.vidjeno) + ": %s " + getString(R.string.puta), post.getSeen().toString()));

            if (post.getUserID() != null) {
                userID = post.getUserID();

                postViewModel.getPostUser(userID).observe(getViewLifecycleOwner(), user -> {
                    username.setText(String.format(getString(R.string.objavio) + ": %s", user.getUsername()));
                    phoneNumber = user.getPhone();

                    if (user.getLocation() != null)
                        lokacija.setText(String.format(getString(R.string.lokacija) + ": %s", user.getLocation()));
                    else lokacija.setVisibility(View.INVISIBLE);

                    if (!user.getImageUrl().isEmpty()) {
                        GlideApp.with(userImage.getContext())
                                .load(user.getImageUrl())
                                .into(userImage);
                    }

                    if (phoneNumber == null) pozovi.setVisibility(View.INVISIBLE);
                });

                if (userID.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                    poruke.setVisibility(View.INVISIBLE);
                    pozovi.setVisibility(View.INVISIBLE);
                    report.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                }
            }
        });

        postViewModel.getPostImages(postID).observe(getViewLifecycleOwner(), posts -> {
            adapter = new PostImageRecyclerViewAdapter(posts);
            recyclerView.setSliderAdapter(adapter);
        });
    }

    @Override
    public void onPause() {
        if (mAdView != null) mAdView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) mAdView.resume();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) mAdView.destroy();
        super.onDestroy();
    }
}