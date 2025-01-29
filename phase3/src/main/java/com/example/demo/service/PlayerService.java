package com.example.demo.service;

import com.example.demo.dto.AnswerQuestionDTO;
import com.example.demo.dto.FollowedDesignerDTO;
import com.example.demo.model.Player;
import com.example.demo.model.Question;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Cache the list of answered questions for a player
     */
    @Cacheable(value = "answeredQuestions", key = "#playerId")
    public List<Player.AnsweredQuestion> getAnsweredQuestions(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        return player.getAnsweredQuestions();
    }

    /**
     * Cache the followed designers list for a player
     */
    @Cacheable(value = "followedDesigners", key = "#playerId")
    public List<String> getFollowedDesigners(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        return player.getFollowedDesignerIds();
    }

    /**
     * When a player follows a designer, clear the cache
     */
    @CacheEvict(value = "followedDesigners", key = "#playerId")
    public Player followDesigner(String playerId, FollowedDesignerDTO followedDesignerDTO) {
        System.out.println("Player ID: " + playerId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        String designerId = followedDesignerDTO.getDesignerId();
        if (!player.getFollowedDesignerIds().contains(designerId)) {
            player.getFollowedDesignerIds().add(designerId);
        } else {
            throw new IllegalArgumentException("Designer is already followed");
        }

        return playerRepository.save(player);
    }

    /**
     * When a player answers a question, clear the cache
     */
    @CacheEvict(value = {"answeredQuestions", "randomUnansweredQuestion"}, key = "#playerId")
    public ResponseEntity<Map<String, Object>> answerQuestion(String playerId, AnswerQuestionDTO answerDTO) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new IllegalArgumentException("Player not found"));
        Question question = questionRepository.findById(answerDTO.getQuestionId()).orElseThrow(() -> new IllegalArgumentException("Question not found"));

        System.out.println("Correct Answer: " + question.getCorrectAnswer() + " | Player's Answer: " + answerDTO.getYourAnswer());

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

    /**
     * Cache the IDs of answered questions for a player
     */
    @Cacheable(value = "answeredQuestionIds", key = "#playerId")
    public List<String> getAnsweredQuestionIds(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        return player.getAnsweredQuestions().stream()
                .map(Player.AnsweredQuestion::getQuestionId)
                .collect(Collectors.toList());
    }

    /**
     * Cache a random unanswered question for a player
     */
    @Cacheable(value = "randomUnansweredQuestion", key = "#playerId")
    public Question getRandomUnansweredQuestion(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        List<String> answeredQuestionIds = player.getAnsweredQuestions().stream()
                .map(Player.AnsweredQuestion::getQuestionId)
                .collect(Collectors.toList());

        List<Question> unansweredQuestions = questionRepository.findAll().stream()
                .filter(q -> !answeredQuestionIds.contains(q.getId()))
                .collect(Collectors.toList());

        if (!unansweredQuestions.isEmpty()) {
            Random random = new Random();
            return unansweredQuestions.get(random.nextInt(unansweredQuestions.size()));
        }

        return null;
    }
}
