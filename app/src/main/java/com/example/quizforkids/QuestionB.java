package com.example.quizforkids;

public class QuestionB {
    private String questionText;
    private String answer;
    private String wrong1;
    private String wrong2;
    private String photoPath;

    public QuestionB(String questionText, String answer, String wrong1, String wrong2, String photoPath) {
        this.questionText = questionText;
        this.answer = answer;
        this.wrong1 = wrong1;
        this.wrong2 = wrong2;
        this.photoPath = photoPath;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public String getWrong1() {return wrong1;}

    public String getWrong2() {return  wrong2;}

    public String getPhotoPath() {
        return photoPath;
    }

}
