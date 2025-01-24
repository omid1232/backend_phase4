package com.example.demo.service;

import com.example.demo.dto.AnswerQuestionDTO;
import com.example.demo.dto.FollowedDesignerDTO;
import com.example.demo.model.Player;
import com.example.demo.model.Question;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Player followDesigner(String playerId, FollowedDesignerDTO followedDesignerDTO) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        String designerId = followedDesignerDTO.getDesignerId();

        // Ensure the designer is not already followed
        if (!player.getFollowedDesignerIds().contains(designerId)) {
            player.getFollowedDesignerIds().add(designerId);
        } else {
            throw new IllegalArgumentException("Designer is already followed");
        }

        return playerRepository.save(player);
    }



    public Player answerQuestion(String playerId, AnswerQuestionDTO answerDTO) {
        // Find the player
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        // Find the question
        Question question = questionRepository.findById(answerDTO.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        // Determine if the answer is correct
        boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(answerDTO.getYourAnswer());

        // Create an AnsweredQuestion object
        Player.AnsweredQuestion answeredQuestion = new Player.AnsweredQuestion();
        answeredQuestion.setQuestionId(question.getId());
        answeredQuestion.setYourAnswer(answerDTO.getYourAnswer());
        answeredQuestion.setIsCorrect(isCorrect);

        // Add to the player's answered questions
        player.getAnsweredQuestions().add(answeredQuestion);

        // Update the player's score if the answer is correct
        if (isCorrect) {
            player.setScore(player.getScore() + 10);
        }

        // Save the player
        return playerRepository.save(player);
    }
}
