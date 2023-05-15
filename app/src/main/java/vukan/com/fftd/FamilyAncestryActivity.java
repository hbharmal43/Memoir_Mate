package vukan.com.fftd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FamilyAncestryActivity extends AppCompatActivity {

    private Button familyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familyancestry);

        familyButton = findViewById(R.id.familyButton);
        familyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openaddpage();
            }
        });
    }

    public void openaddpage(){
        Intent intent = new Intent(this, AddRelation.class);
        startActivity(intent);
    }
}
