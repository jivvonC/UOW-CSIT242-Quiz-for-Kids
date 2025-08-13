package com.example.quizforkids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;

public class ResultActivity extends AppCompatActivity {

    private Animation scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView resultText = findViewById(R.id.resultText);
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(clickListener);

        scale = AnimationUtils.loadAnimation(this, R.anim.scale);

        Intent intent = getIntent();
        int correct = intent.getIntExtra("correct", 0);
        int incorrect = intent.getIntExtra("incorrect", 0);
        int score = intent.getIntExtra("score", 0);
        String quizType = intent.getStringExtra("quizType");

        int totalPoints = ScoreManager.getTotalPoints(this);

        resultText.setText("Well done! You finished the \"" + quizType + "\" quiz with "
                + correct + " correct and " + incorrect + " incorrect answers.\n"
                + score + " points this time.\n"
                + "Total points: " + totalPoints);
        resultText.startAnimation(scale);
    }

    private final View.OnClickListener clickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.backBtn:
                            Intent intent = new Intent(ResultActivity.this, Menu.class);
                            startActivity(intent);
                            break;

                    }
                }
            };
}