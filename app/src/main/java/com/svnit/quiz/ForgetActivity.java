package com.svnit.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    EditText mail;
    Button cancel,reset;
    ProgressBar bar;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        mail = findViewById(R.id.inputEmailforget);
        cancel = findViewById(R.id.buttonCancelForget);
        reset = findViewById(R.id.buttonResetForget);
        bar = findViewById(R.id.progressBar3);

        bar.setVisibility(View.INVISIBLE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mail.getText().toString();
                resetPassword(email);
            }
        });
    }

    public void resetPassword(String email){
        bar.setVisibility(View.VISIBLE);
        reset.setClickable(false);
        reset.setVisibility(View.INVISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgetActivity.this, "A mail has been sent to reset your password", Toast.LENGTH_SHORT).show();
                    bar.setVisibility(View.INVISIBLE);
                    finish();
                }
                else{
                    bar.setVisibility(View.INVISIBLE);
                    reset.setClickable(true);
                    reset.setVisibility(View.VISIBLE);
                    Toast.makeText(ForgetActivity.this, "Cheak your email and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}