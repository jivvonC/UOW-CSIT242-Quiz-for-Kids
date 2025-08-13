package com.example.quizforkids;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AnimalQuizActivity extends AppCompatActivity {

    private List<QuestionA> questionList;
    private String[] userAnswers;
    private int currentIndex = 0;

    private ImageView imageView;
    private EditText answerEditText;
    private Button nextBtn, prevBtn, submitbtn;

    private DatabaseHelper dbHelper;

    private Animation fade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animal_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        questionList = dbHelper.getAllQuestionsA(); // 모든 질문 가져오기
        Collections.shuffle(questionList);          // 랜덤 섞기
        questionList = questionList.subList(0, 4);   // 앞 4개만 사용

        userAnswers = new String[questionList.size()];

        imageView = findViewById(R.id.imageView);
        answerEditText = findViewById(R.id.answerEditText);
        nextBtn = findViewById(R.id.nextBtn);
        prevBtn = findViewById(R.id.prevBtn);
        submitbtn = findViewById(R.id.submitbtn);
        fade = AnimationUtils.loadAnimation(this, R.anim.fade);

        showQuestion(currentIndex);

        nextBtn.setOnClickListener(v -> {
            saveCurrentAnswer();
            if (currentIndex < questionList.size() - 1) {
                currentIndex++;
                showQuestion(currentIndex);
            }
        });

        prevBtn.setOnClickListener(v -> {
            saveCurrentAnswer();

            if (currentIndex > 0) {
                currentIndex--;
                showQuestion(currentIndex);
            }
        });

        submitbtn.setOnClickListener(v -> {
            saveCurrentAnswer();
            calculateResult();
        });

    }

    private void showQuestion(int index) {
        QuestionA question = questionList.get(index);

        int resId = getResources().getIdentifier(
                question.getPhotoPath(), "drawable", getPackageName());
        imageView.setImageResource(resId);
        imageView.startAnimation(fade);

        answerEditText.setText(userAnswers[index] != null ? userAnswers[index] : "");

        // button visibility control
        if (index == 0) {
            prevBtn.setVisibility(Button.INVISIBLE);
        } else {
            prevBtn.setVisibility(Button.VISIBLE);
        }

        if (index == questionList.size() - 1) {
            nextBtn.setVisibility(Button.INVISIBLE);
            submitbtn.setVisibility(Button.VISIBLE);
        } else {
            nextBtn.setVisibility(Button.VISIBLE);
            submitbtn.setVisibility(Button.INVISIBLE);
        }
    }

    private void saveCurrentAnswer() {
        String userInput = answerEditText.getText().toString().trim();
        userAnswers[currentIndex] = userInput;
    }

    private void calculateResult() {
        int correct = 0;
        int incorrect = 0;

        for (int i = 0; i < questionList.size(); i++) {
            String userAnswer = userAnswers[i];
            String correctAnswer = questionList.get(i).getAnswer();

            if (userAnswer == null || userAnswer.isEmpty()) {
                incorrect++;
            } else if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                correct++;
            } else {
                incorrect++;
            }
        }

        int score = correct * 3 - incorrect;

        // ✅ 누적 점수 저장
        ScoreManager.addPoints(this, score);

        // ✅ 날짜/시간 문자열 구하기
        String dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());

        // ✅ 현재 로그인한 유저 가져오기
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "User");

        // ✅ 퀴즈 결과 DB에 저장 – username 추가
        dbHelper.insertAttempt(username, "Animals", dateTime, score);

        // ✅ 결과 화면으로 이동
        Intent intent = new Intent(AnimalQuizActivity.this, ResultActivity.class);
        intent.putExtra("correct", correct);
        intent.putExtra("incorrect", incorrect);
        intent.putExtra("score", score);
        intent.putExtra("quizType", "Animals");
        startActivity(intent);
        finish();
    }
}