package com.example.quizforkids;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Accounts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accounts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button logoutBtn = (Button) findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(clickListener);

        Button changePwBtn = (Button) findViewById(R.id.chgPwbtn);
        changePwBtn.setOnClickListener(clickListener);

    }

    private final View.OnClickListener clickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.logoutBtn:
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            String username1 = sharedPreferences.getString("username", null);

                            if (username1 != null) {
                                DatabaseHelper db = new DatabaseHelper(Accounts.this);
                                int totalPoints = db.getTotalPointsByUsername(username1);  // 이 메서드를 아래에서 정의할 예정

                                Toast.makeText(Accounts.this,
                                        username1 + ", you have overall " + totalPoints + " points",
                                        Toast.LENGTH_LONG).show();

                                // ⏳ 2초 후 로그아웃 처리
                                new android.os.Handler().postDelayed(() -> {
                                    // 로그인 상태 삭제
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.apply();

                                    // 메인으로 이동
                                    Intent intent = new Intent(Accounts.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }, 2000);  // 2초 (Toast.LENGTH_LONG이 약 3.5초지만, 2초면 충분)
                            }

                            // delete login status
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();  // 모든 값 제거
                            editor.apply();

                            // back to start page
                            Intent intent = new Intent(Accounts.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 백스택 초기화
                            startActivity(intent);
                            finish();
                            break;

                        case R.id.chgPwbtn:
                            EditText newpwField = findViewById(R.id.newpw);
                            String newPassword = newpwField.getText().toString();

                            if (newPassword.isEmpty()) {
                                newpwField.setError("Please type in new password");
                                return;
                            }

                            // 현재 로그인된 username 가져오기
                            SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            String username = sharedPreferences2.getString("username", null);

                            if (username != null) {
                                DatabaseHelper db = new DatabaseHelper(Accounts.this);
                                boolean success = db.updatePasswordByUsername(username, newPassword);
                                if (success) {
                                    newpwField.setText("");  // 입력 필드 초기화
                                    Toast.makeText(Accounts.this, "Password changed", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Accounts.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Accounts.this, "No login info found", Toast.LENGTH_SHORT).show();
                            }
                            break;

                    }
                }
            };
}