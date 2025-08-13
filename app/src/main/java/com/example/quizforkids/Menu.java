package com.example.quizforkids;

import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;


public class Menu extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button playBtn = (Button) findViewById(R.id.playBtn);
        playBtn.setOnClickListener(clickListener);

        Button howtoBtn = (Button) findViewById(R.id.howtoBtn);
        howtoBtn.setOnClickListener(clickListener);

        Button scoreboardBtn = (Button) findViewById(R.id.scoreboardBtn);
        scoreboardBtn.setOnClickListener(clickListener);

        Button accountsBtn = (Button) findViewById(R.id.accountsBtn);
        accountsBtn.setOnClickListener(clickListener);


        TextView textView = (TextView) findViewById(R.id.textView);

    }

    private final View.OnClickListener clickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.playBtn:
                            Intent intent = new Intent(Menu.this, SelectGame.class);
                            startActivity(intent);
                            break;
                        case R.id.howtoBtn:
                            Intent intentA = new Intent(Menu.this, Instructions.class);
                            startActivity(intentA);
                            break;

                        case R.id.scoreboardBtn:
                            Intent intentB = new Intent(Menu.this, Scoreboard.class);
                            startActivity(intentB);
                            break;

                        case R.id.accountsBtn:
                            Intent intentC = new Intent(Menu.this, Accounts.class);
                            startActivity(intentC);
                            break;

                    }
                }
            };
}