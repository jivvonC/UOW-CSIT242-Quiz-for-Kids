package com.example.quizforkids;

import android.os.Bundle;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

public class SignUp extends AppCompatActivity {

    EditText inputEmail, inputUserName, inputPW;
    Button signUpBtn;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputEmail = findViewById(R.id.inputEmail);
        inputUserName = findViewById(R.id.inputUserName);
        inputPW = findViewById(R.id.inputPW);
        signUpBtn = findViewById(R.id.signUpBtn);

        // DB helper 초기화
        dbHelper = new DatabaseHelper(this);

        // 4. 버튼 클릭 시 회원가입 로직
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력값 가져오기
                String email = inputEmail.getText().toString().trim();
                String username = inputUserName.getText().toString().trim();
                String password = inputPW.getText().toString().trim();

                // 간단한 유효성 검사
                if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please fill out the form", Toast.LENGTH_SHORT).show();
                    return;
                }

                // DB에 회원 등록 시도
                boolean success = dbHelper.registerUser(email, username, password);

                if (success) {
                    Toast.makeText(SignUp.this, "Sign up success!", Toast.LENGTH_SHORT).show();

                    // Move to login page
                    Intent intent = new Intent(SignUp.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUp.this, "Already existing email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    }
