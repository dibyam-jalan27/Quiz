package com.svnit.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ResultActivity extends AppCompatActivity {

    TextView questions,incorrect,correct,timeouts,highScore;
    final Query userQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Email");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    int Questions,Incorrect,Correct,Timeouts,HighScore;

    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        questions = findViewById(R.id.textViewNoQuestions);
        incorrect = findViewById(R.id.textViewIncorrect);
        correct = findViewById(R.id.textViewCorrect);
        timeouts = findViewById(R.id.textViewTimeOut);
        highScore = findViewById(R.id.textViewHighScore);

        String emailData = user.getEmail();

        // Read from the database
        userQuery.equalTo(emailData).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = null;
                        for (DataSnapshot scoreSnapshot: dataSnapshot.getChildren()) {
                            // result
                            key = scoreSnapshot.getKey();
                            HighScore = dataSnapshot.child(key).child("Score").getValue(Integer.class);
                            Questions = dataSnapshot.child(key).child("Questions").getValue(Integer.class);
                            Incorrect = dataSnapshot.child(key).child("UserIncorrect").getValue(Integer.class);
                            Correct = dataSnapshot.child(key).child("UserCorrect").getValue(Integer.class);
                            Timeouts = dataSnapshot.child(key).child("UserTimeout").getValue(Integer.class);
                            questions.setText(String.valueOf(Questions));
                            highScore.setText(String.valueOf(HighScore));
                            correct.setText(String.valueOf(Correct));
                            incorrect.setText(String.valueOf(Incorrect));
                            timeouts.setText(String.valueOf(Timeouts));

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }
}