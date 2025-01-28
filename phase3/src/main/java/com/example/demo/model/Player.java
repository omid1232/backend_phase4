package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "players")
public class Player {
    @Id
    private String id;
    private String username;
    private String password;
    private int score;

    private List<String> followedDesignerIds = new ArrayList<>();
    private List<AnsweredQuestion> answeredQuestions = new ArrayList<>();

    @Data
    public static class AnsweredQuestion {
        private String questionId;
        private String yourAnswer;
        private boolean isCorrect;

        public AnsweredQuestion(String questionId, String yourAnswer, boolean isCorrect) {
            this.setYourAnswer(yourAnswer);
            this.setQuestionId(questionId);
            this.setIsCorrect(isCorrect);
        }

        public String getQuestionId() {
            return questionId;
        }

        public String getYourAnswer() {
            return yourAnswer;
        }

        public boolean getIsCorrect() {
            return isCorrect;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public void setYourAnswer(String yourAnswer) {
            this.yourAnswer = yourAnswer;
        }

        public void setIsCorrect(boolean correct) {
            isCorrect = correct;
        }
    }


    public List<String> getFollowedDesignerIds() {
        return followedDesignerIds;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<AnsweredQuestion> getAnsweredQuestions() {
        return answeredQuestions;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAnsweredQuestions(List<AnsweredQuestion> answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public void setFollowedDesignerIds(List<String> followedDesignerIds) {
        this.followedDesignerIds = followedDesignerIds;
    }

}

