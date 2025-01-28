package com.example.demo.service;

import com.example.demo.dto.AnswerQuestionDTO;
import com.example.demo.dto.FollowedDesignerDTO;
import com.example.demo.model.Player;
import com.example.demo.model.Question;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private QuestionRepository questionRepository;
    public List<Player.AnsweredQuestion> getAnsweredQuestions(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        return player.getAnsweredQuestions();
    }


    public Player followDesigner(String playerId, FollowedDesignerDTO followedDesignerDTO) {
        System.out.println("player id is:  "+ playerId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        String designerId = followedDesignerDTO.getDesignerId();
      //  System.out.println("designer id is:   "+designerId);
        // Ensure the designer is not already followed
        if (!player.getFollowedDesignerIds().contains(designerId)) {
            player.getFollowedDesignerIds().add(designerId);
        } else {
            throw new IllegalArgumentException("Designer is already followed");
        }

        return playerRepository.save(player);
    }



    public ResponseEntity<Map<String, Object>> answerQuestion(String playerId, AnswerQuestionDTO answerDTO) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new IllegalArgumentException("Player not found"));
        Question question = questionRepository.findById(answerDTO.getQuestionId()).orElseThrow(() -> new IllegalArgumentException("Question not found"));
        System.out.println("answer is   "+question.getCorrectAnswer()+"your answer is  "+answerDTO.getYourAnswer());
        boolean isCorrect = question.getCorrectAnswer().equals(answerDTO.getYourAnswer());
        int pointsEarned = 0;

        if (isCorrect) {
            switch (question.getDifficulty().toLowerCase()) {
                case "easy":
                    pointsEarned = 10;
                    break;
                case "medium":
                    pointsEarned = 20;
                    break;
                case "hard":
                    pointsEarned = 30;
                    break;
            }
            player.setScore(player.getScore() + pointsEarned);
        }

        player.getAnsweredQuestions().add(new Player.AnsweredQuestion(answerDTO.getQuestionId(), answerDTO.getYourAnswer(), isCorrect));
        playerRepository.save(player);

        Map<String, Object> response = new HashMap<>();
        response.put("correct", isCorrect);
        response.put("message", isCorrect ? "Correct! You earned " + pointsEarned + " points." : "Wrong answer! Try again.");

        return ResponseEntity.ok(response);
    }
    public List<String> getAnsweredQuestionIds(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        return player.getAnsweredQuestions().stream()
                .map(Player.AnsweredQuestion::getQuestionId) // Extract only question IDs
                .collect(Collectors.toList());
    }

    public Question getRandomUnansweredQuestion(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        // Get IDs of all questions answered by the player
        List<String> answeredQuestionIds = player.getAnsweredQuestions().stream()
                .map(Player.AnsweredQuestion::getQuestionId)
                .collect(Collectors.toList());

        // Fetch all questions that are not answered by the player
        List<Question> unansweredQuestions = questionRepository.findAll().stream()
                .filter(q -> !answeredQuestionIds.contains(q.getId()))
                .collect(Collectors.toList());

        if (!unansweredQuestions.isEmpty()) {
            // Randomly select one unanswered question
            Random random = new Random();
            return unansweredQuestions.get(random.nextInt(unansweredQuestions.size()));
        }

        return null; // No unanswered questions left
    }



}
