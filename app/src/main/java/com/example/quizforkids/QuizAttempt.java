package com.example.quizforkids;

public class QuizAttempt {

    private String username;
    private String quizArea;
    private String dateTime;
    private int points;

    public QuizAttempt(String username, String quizArea, String dateTime, int points) {
        this.username = username;
        this.quizArea = quizArea;
        this.dateTime = dateTime;
        this.points = points;
    }

    public String getUsername() { return username; }
    public String getQuizArea() { return quizArea; }
    public String getDateTime() { return dateTime; }
    public int getPoints() { return points; }
}
