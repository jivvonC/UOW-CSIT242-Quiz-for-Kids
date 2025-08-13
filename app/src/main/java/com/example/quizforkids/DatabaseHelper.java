package com.example.quizforkids;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "game.db";
    public static final String QUESTION_A_TABLE = "questionA";
    public static final String QUESTION_B_TABLE = "questionB";
    public static final String USERS_TABLE = "users";

    // Users 테이블 컬럼
    public static final String U_COL_1 = "ID";
    public static final String U_COL_2 = "EMAIL";
    public static final String U_COL_3 = "USERNAME";
    public static final String U_COL_4 = "PASSWORD";


    // Question A 테이블 컬럼
    public static final String QA_COL_1 = "ID";
    public static final String QA_COL_2 = "QUESTION_TEXT";
    public static final String QA_COL_3 = "ANSWER";
    public static final String QA_COL_4 = "PHOTO";

    // Question B 테이블 컬럼
    public static final String QB_COL_1 = "ID";
    public static final String QB_COL_2 = "QUESTION_TEXT";
    public static final String QB_COL_3 = "ANSWER";
    public static final String QB_COL_4 = "WRONG1";
    public static final String QB_COL_5 = "WRONG2";
    public static final String QB_COL_6 = "PHOTO";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users 테이블 생성
        db.execSQL("CREATE TABLE " + USERS_TABLE + " (" +
                U_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                U_COL_2 + " TEXT, " +
                U_COL_3 + " TEXT, " +
                U_COL_4 + " TEXT)");

        // Question A 테이블 생성
        db.execSQL("CREATE TABLE " + QUESTION_A_TABLE + " (" +
                QA_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QA_COL_2 + " TEXT, " +
                QA_COL_3 + " TEXT, " +
                QA_COL_4 + " INTEGER)");

        // Question B 테이블 생성
        db.execSQL("CREATE TABLE " + QUESTION_B_TABLE + " (" +
                QB_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QB_COL_2 + " TEXT, " +
                QB_COL_3 + " TEXT, " +
                QB_COL_4 + " TEXT, " +
                QB_COL_5 + " TEXT, " +
                QB_COL_6 + " INTEGER)");

        // scoreboard용 점수 저장 테이블
        db.execSQL("CREATE TABLE IF NOT EXISTS quiz_attempts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "quiz_area TEXT, " +
                "date_time TEXT, " +
                "points INTEGER)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + QUESTION_A_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + QUESTION_B_TABLE);
        onCreate(db);
    }

    // 회원가입 - 유저 삽입
    public boolean registerUser(String email, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 중복 이메일 체크
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + U_COL_2 + " = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            return false; // 이미 존재
        }

        ContentValues values = new ContentValues();
        values.put(U_COL_2, email);
        values.put(U_COL_3, username);
        values.put(U_COL_4, password);
        long result = db.insert(USERS_TABLE, null, values);
        return result != -1;
    }

    // login-check username, pw
    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + USERS_TABLE + " WHERE " + U_COL_3 + " = ? AND " + U_COL_4 + " = ?",
                new String[]{username, password});
        boolean loginSuccess = cursor.getCount() > 0;
        cursor.close();
        return loginSuccess;
    }

    //change pw
    public boolean updatePasswordByUsername(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(U_COL_4, newPassword);
        int rows = db.update(USERS_TABLE, values, U_COL_3 + " = ?", new String[]{username});
        return rows > 0;
    }

    // Insert one question for question A table
    public boolean insertQuestionA(String questionText, String answer, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QA_COL_2, questionText);
        values.put(QA_COL_3, answer);
        values.put(QA_COL_4, photo);
        long result = db.insert(QUESTION_A_TABLE, null, values);
        return result != -1;
    }

    // View all questions A
    public List<QuestionA> getAllQuestionsA() {
        List<QuestionA> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ QUESTION_A_TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                String question = cursor.getString(cursor.getColumnIndexOrThrow(QA_COL_2));
                String answer = cursor.getString(cursor.getColumnIndexOrThrow(QA_COL_3));
                String photoPath = cursor.getString(cursor.getColumnIndexOrThrow(QA_COL_4));

                questionList.add(new QuestionA(question, answer, photoPath));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return questionList;
    }

    // Insert one question for question B table
    public boolean insertQuestionB(String questionText, String answer , String wrong1, String wrong2, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QB_COL_2, questionText);
        values.put(QB_COL_3, answer);
        values.put(QB_COL_4, wrong1);
        values.put(QB_COL_5, wrong2);
        values.put(QB_COL_6, photo);
        long result = db.insert(QUESTION_B_TABLE, null, values);
        return result != -1;
    }



    public void insertInitialQuestionsIfNeededA() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + QUESTION_A_TABLE, null);

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count == 0) {
                // 질문이 없으면 삽입
                insertQuestionA("What is the name of this animal?", "Zebra", "zebra");
                insertQuestionA("What is the name of this animal?", "Elephant", "elephant");
                insertQuestionA("What is the name of this animal?", "Lion", "lion");
                insertQuestionA("What is the name of this animal?", "Giraffe", "giraffe");
                insertQuestionA("What is the name of this animal?", "Tiger", "tiger");
                insertQuestionA("What is the name of this animal?", "Kangaroo", "kangaroo");
                insertQuestionA("What is the name of this animal?", "Koala", "koala");
                insertQuestionA("What is the name of this animal?", "Emu", "emu");
                insertQuestionA("What is the name of this animal?", "Quokka", "quokka");
                insertQuestionA("What is the name of this animal?", "Wombat", "wombat");
            }
        }
        cursor.close();
    }

    public void insertInitialQuestionsIfNeededB() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + QUESTION_B_TABLE, null);

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count == 0) {
                // 질문이 없으면 삽입
                insertQuestionB("What is the name of this character?", "Garfield", "Snoopy", "Luigi", "garfield");
                insertQuestionB("What is the name of this character?", "Toad", "Koopa", "Princess Peach", "toad");
                insertQuestionB("What is the name of this character?", "Kuromi", "My melody", "Hello Kitty", "kuromi");
                insertQuestionB("What is the name of this character?", "Simba", "Scar", "Mufasa", "simba");
                insertQuestionB("What is the name of this character?", "Elsa", "Anna", "Hans", "elsa");
                insertQuestionB("What is the name of this character?", "Jerry", "Tom", "Remi", "jerry");
                insertQuestionB("What is the name of this character?", "Spongebob", "Patrick star", "Squidward", "spongebob");
                insertQuestionB("What is the name of this character?", "Snoopy", "Charlie Brown", "Lucy van Pelt", "snoopy");
                insertQuestionB("What is the name of this character?", "Homer Simpson", "Maggie Simpson", "Bart Simpson", "homersimpson");
                insertQuestionB("What is the name of this character?", "Dora", "Diego", "Map", "dora");
            }
        }
        cursor.close();
    }



    // Question B get random 4 questions
    public List<QuestionB> getRandomQuestionsB(int count) {
        List<QuestionB> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + QUESTION_B_TABLE + " ORDER BY RANDOM() LIMIT " + count, null);
        if (cursor.moveToFirst()) {
            do {
                String questionText = cursor.getString(cursor.getColumnIndexOrThrow(QB_COL_2));
                String answer = cursor.getString(cursor.getColumnIndexOrThrow(QB_COL_3));
                String wrong1 = cursor.getString(cursor.getColumnIndexOrThrow(QB_COL_4));
                String wrong2 = cursor.getString(cursor.getColumnIndexOrThrow(QB_COL_5));
                String photo = cursor.getString(cursor.getColumnIndexOrThrow(QB_COL_6));

                questions.add(new QuestionB(questionText, answer, wrong1, wrong2, photo));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return questions;
    }

    //scoreboard용 점수 저장
    public void insertAttempt(String username, String quizArea, String dateTime, int points) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("quiz_area", quizArea);
        values.put("date_time", dateTime);
        values.put("points", points);
        db.insert("quiz_attempts", null, values);
        db.close();
    }

    public List<QuizAttempt> getAllAttempts() {
        List<QuizAttempt> attemptList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "quiz_attempts",
                new String[]{"username","quiz_area", "date_time", "points"},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String username= cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String quizArea = cursor.getString(cursor.getColumnIndexOrThrow("quiz_area"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date_time"));
                int points = cursor.getInt(cursor.getColumnIndexOrThrow("points"));

                attemptList.add(new QuizAttempt(username, quizArea, date, points));
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return attemptList;
    }

    public int getTotalPointsByUsername(String username) {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(points) FROM quiz_attempts WHERE username = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);  // 첫 번째 컬럼의 총합
        }
        cursor.close();
        return total;
    }

    public List<QuizAttempt> getAttemptsByUsername(String username) {
        List<QuizAttempt> attemptList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "quiz_attempts",
                new String[]{"username", "quiz_area", "date_time", "points"},
                "username = ?",
                new String[]{username},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String quizArea = cursor.getString(cursor.getColumnIndexOrThrow("quiz_area"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date_time"));
                int points = cursor.getInt(cursor.getColumnIndexOrThrow("points"));

                attemptList.add(new QuizAttempt(username, quizArea, date, points));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return attemptList;
    }

}
