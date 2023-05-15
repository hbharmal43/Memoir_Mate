package vukan.com.fftd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddRelation extends AppCompatActivity {

    private EditText user_email;
    private EditText relative_email;
    private EditText relation;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_relation);

        user_email = findViewById(R.id.user_em_relation);
        relative_email = findViewById(R.id.rel_em_relation);
        relation = findViewById(R.id.rel_relation);
        save = findViewById(R.id.add_relation);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUsEm = user_email.getText().toString();
                String getRelEm = relative_email.getText().toString();
                String getRel = relation.getText().toString();

                Map<String, String> info = new HashMap<>();
                info.put("user_email", getUsEm);
                info.put("relative_email", getRelEm);
                info.put("relation", getRel);
                FirebaseFirestore.getInstance().collection("FamilyRelations").document().set(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Added new relation successfully!", Toast.LENGTH_SHORT).show();
                        goback();
                    }
                });
            }
        });
    }

    public void goback(){
        Intent intent = new Intent(this, FamilyAncestryActivity.class);
        startActivity(intent);
    }
}
