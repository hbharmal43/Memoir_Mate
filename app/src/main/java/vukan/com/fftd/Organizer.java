package vukan.com.fftd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//import kotlin.Suppress;


public class Organizer extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword,fullName,phoneNumber;
    private Button signupButton;
    private TextView loginRedirectText,accessCode;

    FirebaseFirestore fstore;
    String organizerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_Oemail);
        signupPassword = findViewById(R.id.signup_Opassword);
        signupButton = findViewById(R.id.signup_Obutton);
        loginRedirectText = findViewById(R.id.loginRedirectOText);
        accessCode = findViewById(R.id.access_code);
        fullName = findViewById(R.id.full_Oname);
        phoneNumber = findViewById(R.id.phone_Onum);
        fstore = FirebaseFirestore.getInstance();

        Random rand = new Random();
        int val = rand.nextInt(1000000);
        accessCode.setText("Access Code = "+Integer.toString(val));



        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String full_name = fullName.getText().toString().trim();
                String user_name = phoneNumber.getText().toString().trim();
                String access_code = accessCode.getText().toString().trim();
                String access_coded_db = Integer.toString(val);



                if (email.isEmpty())
                {
                    signupEmail.setError("Email cannot be empty");
                }
                if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                }
                if(full_name.isEmpty())
                {
                    fullName.setError("Name cannot be empty");
                }
                if(user_name.isEmpty())
                {
                    phoneNumber.setError("User Name cannot be empty");
                }
                else{
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Organizer.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                organizerID = auth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("OrganizerInputs").document(organizerID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("fName",full_name);
                                user.put("email",email);
                                user.put("access",access_coded_db);
                                user.put("uName",user_name);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                       //Log.d(TAG,"onSuccess: user profile is created for"+userID);
                                    }
                                });
                                startActivity(new Intent(Organizer.this, LoginActivity.class));
                            } else {
                                Toast.makeText(Organizer.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Organizer.this, LoginActivity.class));
            }
        });

    }
}