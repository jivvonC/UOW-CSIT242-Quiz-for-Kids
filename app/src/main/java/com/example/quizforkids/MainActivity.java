package com.example.quizforkids;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_title), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(clickListener);

        Button signupBtn = (Button) findViewById(R.id.signUpBtn);
        signupBtn.setOnClickListener(clickListener);

        DatabaseHelper db = new DatabaseHelper(this);
        db.insertInitialQuestionsIfNeededA();
        db.insertInitialQuestionsIfNeededB();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false); // automatic logout
        editor.apply();

    }

    private final View.OnClickListener clickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.loginBtn:
                            Intent intent = new Intent(MainActivity.this, Login.class);
                            startActivity(intent);
                            break;
                        case R.id.signUpBtn:
                            Intent intentA = new Intent(MainActivity.this, SignUp.class);
                            startActivity(intentA);
                            break;

                    }
                }
            };

}
