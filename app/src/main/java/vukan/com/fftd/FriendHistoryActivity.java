package vukan.com.fftd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FriendHistoryActivity extends AppCompatActivity {

    private Button friendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendhistory);

        friendButton = findViewById(R.id.friendButton);
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openaddpage();
            }
        });
    }

    public void openaddpage(){
        Intent intent = new Intent(this, AddFriend.class);
        startActivity(intent);
    }
}
