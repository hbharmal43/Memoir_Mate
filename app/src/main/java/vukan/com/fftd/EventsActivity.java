package vukan.com.fftd;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventsActivity extends AppCompatActivity {

    private Button eventsButton;
    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        eventsButton = findViewById(R.id.eventsButton);
        welcomeMessage = findViewById(R.id.welcome_message);
    }

    public void onEventsButtonClick(View view) {
        welcomeMessage.setText("Welcome to the Events page");
    }
}
