package com.example.demo.dto;


import lombok.Data;

@Data
public class AnswerQuestionDTO {
    private String questionId; // ID of the question being answered
    private String yourAnswer; // The player's answer

    public String getQuestionId() {
        return questionId;
    }

    public void setYourAnswer(String yourAnswer) {
        this.yourAnswer = yourAnswer;
    }

    public String getYourAnswer() {
        return yourAnswer;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}

