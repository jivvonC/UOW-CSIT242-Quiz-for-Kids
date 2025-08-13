package com.example.quizforkids;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;


public class SelectGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button animalBtn = (Button) findViewById(R.id.chgPwbtn);
        animalBtn.setOnClickListener(clickListener);

        Button cartoonBtn = (Button) findViewById(R.id.logoutBtn);
        cartoonBtn.setOnClickListener(clickListener);
    }

    private final View.OnClickListener clickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.chgPwbtn:
                            Intent intent = new Intent(SelectGame.this, AnimalQuizActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.logoutBtn:
                            Intent intentA = new Intent(SelectGame.this, CartoonQuizActivity.class);
                            startActivity(intentA);
                            break;
                    }
                }
            };
}