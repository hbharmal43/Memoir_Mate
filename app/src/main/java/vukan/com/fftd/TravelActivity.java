package vukan.com.fftd;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TravelActivity extends AppCompatActivity {

    private Button travelButton;
    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        travelButton = findViewById(R.id.travelButton);
        welcomeMessage = findViewById(R.id.welcome_message);
    }

    public void onTravelButtonClick(View view) {
        welcomeMessage.setText("Welcome to the Travel page");
    }
}
