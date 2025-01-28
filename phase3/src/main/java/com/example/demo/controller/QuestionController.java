package com.example.demo.controller;

import com.example.demo.model.Player;
import com.example.demo.model.Question;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    QuestionService questionService;
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String categoryId) {
        List<Question> questions = questionRepository.findByCategoryId(categoryId);
        return ResponseEntity.ok(questions);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions() {
        System.out.println("fetch all questions");
        List<Question> questions = questionRepository.findAll();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{designerId}/questions")
    public ResponseEntity<List<Question>> getQuestionsByDesigner(@PathVariable String designerId) {
        List<Question> questions = questionService.getQuestionsByDesigner(designerId);
        return ResponseEntity.ok(questions);
    }


    @GetMapping("/designer/{designerId}/player/{playerId}")
    public ResponseEntity<List<Map<String, Object>>> getQuestionsByDesignerForPlayer(
            @PathVariable String designerId,
            @PathVariable String playerId) {

        System.out.println("fetch questions of id:   "+designerId);
        // Fetch questions created by the designer
        List<Question> allQuestions = questionRepository.findByDesignerId(designerId);

        // Fetch answered questions for the player
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        List<String> answeredQuestionIds = player.getAnsweredQuestions().stream()
                .map(Player.AnsweredQuestion::getQuestionId)
                .toList();

        // Prepare response: add answered status and correct answer
        List<Map<String, Object>> questionsWithStatus = allQuestions.stream().map(question -> {
            Map<String, Object> questionMap = new HashMap<>();
            questionMap.put("id", question.getId());
            questionMap.put("questionText", question.getQuestionText());
            questionMap.put("options", question.getOptions());
            questionMap.put("correctAnswer", question.getCorrectAnswer());
            questionMap.put("difficulty", question.getDifficulty());
            questionMap.put("categoryId", question.getCategoryId());
            questionMap.put("answered", answeredQuestionIds.contains(question.getId())); // Mark if answered
            return questionMap;
        }).toList();

        return ResponseEntity.ok(questionsWithStatus);
    }
}