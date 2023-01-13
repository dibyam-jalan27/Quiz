package com.svnit.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    TextView time,left;
    TextView question, a, b, c, d;
    ImageView heart1,heart2,heart3;
    Button next;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("Questions");
    DatabaseReference userReference = database.getReference().child("Users");
    final Query userQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Email");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String quizQuestion,quizAnswerA,quizAnswerB,quizAnswerC,quizAnswerD,quizCorrectAnswer,userAns;
    int questionCount = 10,userCorrect = 0,userWrong = 0,questionNumber = 0,questionLeft,life = 3,highScore = 0,timeOuts = 0;
    Integer[] arr= new Integer[questionCount];

    CountDownTimer countDownTimer;
    private static final long TOTAL_TIME = 25000;
    Boolean timerContinue = true,clickable = true,exit = false;
    long timeLeft = TOTAL_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        left = findViewById(R.id.textViewleftquestion);
        time = findViewById(R.id.textViewTime);
        question = findViewById(R.id.textViewQuestion);
        a = findViewById(R.id.textViewOption1);
        b = findViewById(R.id.textViewOption2);
        c = findViewById(R.id.textViewOption3);
        d = findViewById(R.id.textViewOption4);
        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);
        heart3 = findViewById(R.id.heart3);
        next = findViewById(R.id.Next);

        left.setText(""+questionLeft);
        questionLeft=questionCount;

        for(int i = 0; i < questionCount;i++){
            arr[i] = i+1;
        }
        List<Integer> intList = Arrays.asList(arr);
        Collections.shuffle(intList);
        intList.toArray(arr);
        game();

        next.setOnClickListener(view -> {
            if (exit){
                sendScore();
                Intent i = new Intent(QuizActivity.this, LoadingActivity.class);
                startActivity(i);
                finish();
            }
            pauseTimer();
            left.setText(String.valueOf(questionLeft-1));
            resetTimer();
            if (questionLeft==0){
                sendScore();
                Intent i = new Intent(QuizActivity.this, LoadingActivity.class);

                startActivity(i);

                finish();
            }
            else {
                game();
            }

        });

        a.setOnClickListener(view -> {

            userAns = "a";
            if (clickable && timerContinue) {
                pauseTimer();
                if (quizCorrectAnswer.equals(userAns)) {
                    a.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                } else {
                    a.setBackgroundColor(Color.RED);
                    userWrong++;
                    reduceLife();
                }
            }
            findAns();
            clickable = false;
        });

        b.setOnClickListener(view -> {
            userAns = "b";

            if (clickable && timerContinue) {
                pauseTimer();
                if (quizCorrectAnswer.equals(userAns)) {
                    b.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                } else {
                    b.setBackgroundColor(Color.RED);
                    userWrong++;
                    reduceLife();
                }
            }
            findAns();
            clickable = false;
        });

        c.setOnClickListener(view -> {
            userAns = "c";


            if (clickable && timerContinue) {
                pauseTimer();
                if (quizCorrectAnswer.equals(userAns)) {
                    c.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                } else {
                    c.setBackgroundColor(Color.RED);
                    userWrong++;
                    reduceLife();
                }
            }
            findAns();
            clickable = false;
        });

        d.setOnClickListener(view -> {
            userAns = "d";

            if (clickable && timerContinue) {
                pauseTimer();
                if (quizCorrectAnswer.equals(userAns)) {
                    d.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                } else {
                    d.setBackgroundColor(Color.RED);
                    userWrong++;
                    reduceLife();
                }
            }
            findAns();
            clickable = false;
        });




    }

    public void game(){
        clickable = true;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                questionNumber = arr[questionCount-questionLeft];
                questionLeft--;

                quizQuestion = dataSnapshot.child(String.valueOf(questionNumber)).child("q").getValue(String.class);
                quizAnswerA = dataSnapshot.child(String.valueOf(questionNumber)).child("a").getValue(String.class);
                quizAnswerB = dataSnapshot.child(String.valueOf(questionNumber)).child("b").getValue(String.class);
                quizAnswerC = dataSnapshot.child(String.valueOf(questionNumber)).child("c").getValue(String.class);
                quizAnswerD = dataSnapshot.child(String.valueOf(questionNumber)).child("d").getValue(String.class);
                quizCorrectAnswer = dataSnapshot.child(String.valueOf(questionNumber)).child("ans").getValue(String.class);

                startTimer();

                question.setText(quizQuestion);
                question.setBackgroundColor(Color.WHITE);
                a.setText(quizAnswerA);
                a.setBackgroundColor(Color.WHITE);
                b.setText(quizAnswerB);
                b.setBackgroundColor(Color.WHITE);
                c.setText(quizAnswerC);
                c.setBackgroundColor(Color.WHITE);
                d.setText(quizAnswerD);
                d.setBackgroundColor(Color.WHITE);




            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(QuizActivity.this, "Sorry, there is a problem", Toast.LENGTH_LONG).show();

            }
        });


    }

    public void findAns(){
        switch (quizCorrectAnswer) {
            case "a":
                a.setBackgroundColor(Color.GREEN);
                break;
            case "b":
                b.setBackgroundColor(Color.GREEN);
                break;
            case "c":
                c.setBackgroundColor(Color.GREEN);
                break;
            case "d":
                d.setBackgroundColor(Color.GREEN);
                break;
        }
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long l) {
                timeLeft= l;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timerContinue = false;
                pauseTimer();
                timeOuts++;
                question.setText("Sorry, time is up!!");
                findAns();
            }
        }.start();
        timerContinue = true;
    }

    public void resetTimer(){
        timeLeft = TOTAL_TIME;
        updateCountdownText();
    }

    public void updateCountdownText(){
        int seconds = (int) ((timeLeft/1000)%60);
        time.setText(""+seconds);
    }

    public void pauseTimer(){
        countDownTimer.cancel();
        timerContinue = false;
    }

    public void sendScore(){

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
                            highScore = dataSnapshot.child(key).child("Score").getValue(Integer.class);

                        }
                        userReference.child(key).child("UserCorrect").setValue(userCorrect);
                        userReference.child(key).child("UserIncorrect").setValue(userWrong);
                        userReference.child(key).child("UserTimeout").setValue(timeOuts);
                        userReference.child(key).child("Questions").setValue(questionCount);

                        Toast.makeText(QuizActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        if (highScore<userCorrect){
                            userReference.child(key).child("Score").setValue(userCorrect);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );



    }

    public void reduceLife(){
        life--;
        switch (life){
            case 2:
                heart3.setImageResource(R.drawable.img_2);
                break;
            case 1:
                heart2.setImageResource(R.drawable.img_2);
                break;
            case 0:
                heart1.setImageResource(R.drawable.img_2);
                exit = true;

        }
    }
}
