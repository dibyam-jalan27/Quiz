package com.svnit.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView signup,forgot;
    Button signIn;
    EditText username, password;
    ProgressBar bar;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference().child("Users");

    String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        signIn = findViewById(R.id.buttonResetForget);
        username = findViewById(R.id.inputEmailforget);
        password = findViewById(R.id.inputPassword);
        signup = findViewById(R.id.signuptxt);
        forgot = findViewById(R.id.forgot);
        bar = findViewById(R.id.progressBar3);

        bar.setVisibility(View.INVISIBLE);
        signup.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(i);
        });

        signIn.setOnClickListener(view -> {
            bar.setVisibility(View.VISIBLE);
            signIn.setVisibility(View.INVISIBLE);
            signIn.setClickable(false);
            String userName = username.getText().toString();
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userEmail = dataSnapshot.child(userName).child("Email").getValue(String.class);
                    String userPass = password.getText().toString();
                    signInFirebase(userEmail,userPass);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "Sign In was unsuccessful.\nCheck email and password.", Toast.LENGTH_SHORT).show();
                    bar.setVisibility(View.INVISIBLE);
                    signIn.setVisibility(View.VISIBLE);
                    signIn.setClickable(true);
                }
            });


        });

        forgot.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, ForgetActivity.class);
            startActivity(i);
        });


    }

    public void signInFirebase(String email, String password){


        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                bar.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "Sign In was successful", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(LoginActivity.this, "Sign In was unsuccessful.\nCheck email and password.", Toast.LENGTH_SHORT).show();
                bar.setVisibility(View.INVISIBLE);
                signIn.setVisibility(View.VISIBLE);
                signIn.setClickable(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}