package com.example.quizforkids;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

public class Login extends AppCompatActivity {

    EditText inputUsername, inputPassword;
    Button loginbtn;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        loginbtn = findViewById(R.id.loginbtn);

        dbHelper = new DatabaseHelper(this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please type in username and password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean loginSuccess = dbHelper.loginUser(username, password);
                if (loginSuccess) {
                    Toast.makeText(Login.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();

                    //save login status
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("username", username);
                    editor.apply();

                    // Move to menu page
                    Intent intent = new Intent(Login.this, Menu.class);
                    startActivity(intent);
                    finish(); // 로그인 창 종료
                } else {
                    Toast.makeText(Login.this, "Failed to log in. Please check username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}