package com.svnit.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText  email, password, username;
    Button signup;
    ProgressBar bar;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference().child("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.inputEmailforget);
        password = findViewById(R.id.inputPassword);
        signup = findViewById(R.id.buttonResetForget);
        bar = findViewById(R.id.progressBar);
        username = findViewById(R.id.inputUsernameforget);

        bar.setVisibility(View.INVISIBLE);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                String userPass = password.getText().toString();
                String userName = username.getText().toString();

                signupFirebase(userEmail,userPass,userName);
            }
        });


    }

    public void signupFirebase(String userEmail, String userPass, String userName){
        if (userName.isEmpty()) {
            username.setError(getString(R.string.input_error_email));
            username.requestFocus();
            return;
        }



        if (userEmail.isEmpty()) {
            email.setError(getString(R.string.input_error_email));
            email.requestFocus();
            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError(getString(R.string.input_error_email_invalid));
            email.requestFocus();
            return;
        }

        if (userPass.isEmpty()) {
            password.setError(getString(R.string.input_error_password));
            password.requestFocus();
            return;
        }

        if (userPass.length() < 6) {
            password.setError(getString(R.string.input_error_password_length));
            password.requestFocus();
            return;
        }
        signup.setVisibility(View.INVISIBLE);
        signup.setClickable(false);
        bar.setVisibility(View.VISIBLE);

        ref.child(userName).child("Email").setValue(userEmail);
        ref.child(userName).child("Score").setValue(0);

        auth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Your account is created", Toast.LENGTH_SHORT).show();
                    finish();
                    bar.setVisibility(View.INVISIBLE);
                }
                else{
                    Toast.makeText(SignUpActivity.this, "A problem occurred Try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}