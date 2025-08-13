package com.example.quizforkids;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Scoreboard extends AppCompatActivity {

    private List<QuizAttempt> attemptList;
    private AttemptAdapter adapter;
    private Spinner sortSpinner;
    private TextView scoreSummary;

    private Animation blink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scoreboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoreSummary = findViewById(R.id.scoreSummary);
        sortSpinner = findViewById(R.id.sortSpinner);
        RecyclerView recyclerView = findViewById(R.id.attemptsRecyclerView);
        TextView noRecordText = findViewById(R.id.noRecordText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);

        // get attempt data from db
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "User"); // 기본값 "User"

         // get attemptList for this user
        attemptList = dbHelper.getAttemptsByUsername(username);

        // calculate total points
        int totalPoints = 0;
        for (QuizAttempt a : attemptList) totalPoints += a.getPoints();

         // set text
        scoreSummary.setText(username + ", you have " + totalPoints + " points in total!");
        scoreSummary.startAnimation(blink);


        // if they have no record
        if (attemptList.isEmpty()) {
            noRecordText.setVisibility(View.VISIBLE);
        } else {
            noRecordText.setVisibility(View.GONE);
        }


        adapter = new AttemptAdapter(attemptList);
        recyclerView.setAdapter(adapter);


        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Collections.sort(attemptList, Comparator.comparing(QuizAttempt::getDateTime).reversed());
                } else if (position == 1) {
                    Collections.sort(attemptList, Comparator.comparing(QuizAttempt::getQuizArea));
                }
                adapter.updateList(new ArrayList<>(attemptList));
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

}