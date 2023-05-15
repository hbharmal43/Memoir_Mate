package vukan.com.fftd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.os.Handler;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import vukan.com.fftd.ui.my_ads.MyAdsViewModel;

public class MainActivity extends AppCompatActivity {
    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mFirebaseUser;
    private MyAdsViewModel myAdsViewModel;
    private BottomNavigationView navView;
    private SharedPreferences sharedPref;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if user is logged in
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // continue with normal activity setup
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK)
                        myAdsViewModel.addUser();
                    if (IdpResponse.fromResultIntent(result.getData()) == null) finish();
                });

        navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        myAdsViewModel = new ViewModelProvider(this).get(MyAdsViewModel.class);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_proizvodSlika) {
                Objects.requireNonNull(getSupportActionBar()).hide();
                navView.setVisibility(View.GONE);
            } else {
                Objects.requireNonNull(getSupportActionBar()).show();
                navView.setVisibility(View.VISIBLE);
            }
        });

        myAdsViewModel = new ViewModelProvider(this).get(MyAdsViewModel.class);

        mAuthStateListener = firebaseAuth -> {
            mFirebaseUser = firebaseAuth.getCurrentUser();

            if (mFirebaseUser == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        };
        navView.getMenu().findItem(R.id.navigation_pocetna).setTitle(getString(R.string.title_pocetna));
        navView.getMenu().findItem(R.id.navigation_omiljeni).setTitle(getString(R.string.omiljeni));
        navView.getMenu().findItem(R.id.navigation_novioglas).setTitle(getString(R.string.title_create));
        navView.getMenu().findItem(R.id.navigation_poruke).setTitle(getString(R.string.title_pages));
        navView.getMenu().findItem(R.id.navigation_mojioglasi).setTitle(getString(R.string.title_mojioglasi));

        // Add the following code block to set the locale to English always
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        conf.setLocale(Locale.ENGLISH);
        res.updateConfiguration(conf, res.getDisplayMetrics());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(Objects.requireNonNull(mAuthStateListener));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.sign_out) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(task ->
                    Toast.makeText(this, R.string.signed_out, Toast.LENGTH_SHORT).show()
            );
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        else if (itemId == R.id.change_language) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.change_language)
                    .setMessage(R.string.choose_language)
                    .setNegativeButton(R.string.english, (dialog, which) -> setLocale("en", true))
                    .setIcon(R.drawable.ic_language)
                    .show();
        } else if (itemId == R.id.change_theme) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.change_theme)
                    .setMessage(R.string.choose_theme)
                    .setPositiveButton(R.string.dark, (dialog, which) -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        saveTheme("dark");
                    })
                    .setNegativeButton(R.string.light, (dialog, which) -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        saveTheme("light");
                    })
                    .setIcon(R.drawable.ic_color)
                    .show();
        } else if (itemId == R.id.privacy_policy) {
            openWebPage("https://www.youtube.com/midtrac");
        } else if (itemId == R.id.terms_and_conditions) {
            openWebPage("https://www.youtube.com/midtrac");
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void saveTheme(String data) {
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            if (editor != null) {
                editor.putString("theme", data);
                editor.apply();
                new Handler().postDelayed(() -> {
                    recreate();
                }, 500); // Delay for 500 milliseconds (0.5 seconds)
            }
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setLocale(String langCode, boolean flag) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("language", langCode);
        editor.apply();
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(langCode));
        res.updateConfiguration(conf, res.getDisplayMetrics());
        if (flag) recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}