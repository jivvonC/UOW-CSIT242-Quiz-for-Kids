package com.example.quizforkids;

import android.content.Context;
import android.content.SharedPreferences;

public class ScoreManager {
    public static void addPoints(Context context, int pointsToAdd) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int totalPoints = prefs.getInt("totalPoints", 0);
        totalPoints += pointsToAdd;
        prefs.edit().putInt("totalPoints", totalPoints).apply();
    }

    public static int getTotalPoints(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return prefs.getInt("totalPoints", 0);
    }
}
