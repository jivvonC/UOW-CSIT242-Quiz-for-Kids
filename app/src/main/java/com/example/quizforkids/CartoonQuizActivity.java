package com.example.quizforkids;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CartoonQuizActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView questionText;
    private RadioGroup optionsGroup;
    private Button nextBtn, prevBtn, submitBtn;

    private List<QuestionB> questionList;
    private int currentIndex = 0;
    private int[] userAnswers; // 0, 1, 2 for selected option, -1 for no selection

    private int score = 0;
    private int correctCount = 0;
    private int incorrectCount = 0;

    private DatabaseHelper dbHelper;

    private List<List<String>> shuffledOptionsList;
    private Animation scale;

    private RadioButton[] radioButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cartoon_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.imageView2);
        questionText = findViewById(R.id.questionText);
        optionsGroup = findViewById(R.id.optionsGroup);
        nextBtn = findViewById(R.id.nextbtn);
        prevBtn = findViewById(R.id.prevbtn);
        submitBtn = findViewById(R.id.submitBtn);
        scale = AnimationUtils.loadAnimation(this, R.anim.scale);

        dbHelper = new DatabaseHelper(this);
        questionList = dbHelper.getRandomQuestionsB(4); // 사용자 정의 메서드 필요
        shuffledOptionsList = new ArrayList<>();

        userAnswers = new int[questionList.size()];
        for (int i = 0; i < userAnswers.length; i++) userAnswers[i] = -1;

        radioButtons = new RadioButton[3];
        for (int i = 0; i < 3; i++) {
            radioButtons[i] = new RadioButton(this);
            radioButtons[i].setId(View.generateViewId());
            optionsGroup.addView(radioButtons[i]);
        }

        displayQuestion();

        nextBtn.setOnClickListener(v -> {
            saveAnswer();
            if (currentIndex < questionList.size() - 1) {
                currentIndex++;
                displayQuestion();
            }
        });

        prevBtn.setOnClickListener(v -> {
            saveAnswer();
            if (currentIndex > 0) {
                currentIndex--;
                displayQuestion();
            }
        });

        submitBtn.setOnClickListener(v -> {
            saveAnswer();
            calculateResults();
        });
    }

    private void displayQuestion() {
        QuestionB q = questionList.get(currentIndex);

        int resId = getResources().getIdentifier(q.getPhotoPath(), "drawable", getPackageName());
        imageView.setImageResource(resId);
        imageView.startAnimation(scale);
        questionText.setText(q.getQuestionText());

        List<String> options;

        // ✅ 이미 섞인 옵션이 있다면 재사용하고, 없다면 새로 섞어서 저장
        if (shuffledOptionsList.size() > currentIndex) {
            options = shuffledOptionsList.get(currentIndex);
        } else {
            options = new ArrayList<>();
            options.add(q.getAnswer());
            options.add(q.getWrong1());
            options.add(q.getWrong2());
            Collections.shuffle(options);
            shuffledOptionsList.add(options);
        }

        // ✅ 기존 RadioButton 재사용
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i].setText(options.get(i));
            radioButtons[i].setChecked(false);
        }

        // ✅ 이전 선택 복원
        int savedIndex = userAnswers[currentIndex];
        if (savedIndex != -1 && savedIndex < radioButtons.length) {
            radioButtons[savedIndex].setChecked(true);
        }

        prevBtn.setVisibility(currentIndex == 0 ? View.GONE : View.VISIBLE);
        nextBtn.setVisibility(currentIndex == questionList.size() - 1 ? View.GONE : View.VISIBLE);
        submitBtn.setVisibility(currentIndex == questionList.size() - 1 ? View.VISIBLE : View.GONE);
    }

    private void saveAnswer() {
        int count = optionsGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton rb = (RadioButton) optionsGroup.getChildAt(i);
            if (rb.isChecked()) {
                userAnswers[currentIndex] = i;  // 인덱스 저장
                return;
            }
        }
        userAnswers[currentIndex] = -1;  // 아무것도 선택 안 했을 경우
    }

    private void calculateResults() {
        correctCount = 0;
        incorrectCount = 0;

        for (int i = 0; i < questionList.size(); i++) {
            QuestionB q = questionList.get(i);
            List<String> options = shuffledOptionsList.get(i);

            int selectedIndex = userAnswers[i];
            if (selectedIndex != -1 && selectedIndex < options.size()) {
                String selectedAnswer = options.get(selectedIndex);
                if (selectedAnswer.equals(q.getAnswer())) {
                    correctCount++;
                } else {
                    incorrectCount++;
                }
            } else {
                incorrectCount++;
            }
        }

        score = correctCount * 3 - incorrectCount;

        // ✅ 누적 점수 저장
        ScoreManager.addPoints(this, score);

        // ✅ 날짜 및 시도 정보 저장
        String dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());

        // ✅ 현재 로그인한 유저 가져오기
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "User");

        // ✅ 퀴즈 결과 DB에 저장 – username 추가
        dbHelper.insertAttempt(username,"Cartoons", dateTime, score); // ← quiz_area를 "Cartoons"로 저장

        // ✅ 결과 화면으로 이동
        Intent intent = new Intent(CartoonQuizActivity.this, ResultActivity.class);
        intent.putExtra("correct", correctCount);
        intent.putExtra("incorrect", incorrectCount);
        intent.putExtra("score", score);
        intent.putExtra("quizType", "Cartoon");
        startActivity(intent);
        finish();
    }

}