package com.example.quizforkids;

public class QuestionA {
    private String questionText;
    private String answer;
    private String photoPath;

    public QuestionA(String questionText, String answer, String photoPath) {
        this.questionText = questionText;
        this.answer = answer;
        this.photoPath = photoPath;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public String getPhotoPath() {
        return photoPath;
    }

}
