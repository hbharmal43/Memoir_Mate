package vukan.com.fftd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class accessRights extends AppCompatActivity {

    private TextView member_btn, organizer_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_rights);
        member_btn = findViewById(R.id.member_button);
        organizer_btn = findViewById(R.id.organizer_button);

        member_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(accessRights.this, "Member Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(accessRights.this, Member.class));
                finish();
            }
        });

        organizer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(accessRights.this, "Organizer selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(accessRights.this,Organizer.class));
                finish();
            }
        });



//
    }
}