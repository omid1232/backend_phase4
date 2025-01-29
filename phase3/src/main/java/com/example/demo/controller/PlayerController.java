package com.example.demo.controller;

import com.example.demo.dto.FollowedDesignerDTO;
import com.example.demo.model.Designer;
import com.example.demo.model.Player;
import com.example.demo.model.Question;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.AnswerQuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private QuestionRepository questionRepository;
    @PostMapping("/{playerId}/follow")
    public ResponseEntity<Player> followDesigner(
            @PathVariable String playerId,
            @RequestBody FollowedDesignerDTO followedDesignerDTO) {
        Player updatedPlayer = playerService.followDesigner(playerId, followedDesignerDTO);
        return ResponseEntity.ok(updatedPlayer);
    }


    @PostMapping("/{playerId}/answer")
    public ResponseEntity<Map<String, Object>> answerQuestion(
            @PathVariable String playerId,
            @RequestBody AnswerQuestionDTO answerDTO) {
        return playerService.answerQuestion(playerId, answerDTO);
    }
    @GetMapping("/{playerId}/answered-questions")
    public ResponseEntity<List<String>> getAnsweredQuestions(@PathVariable String playerId) {
        List<String> answeredQuestionIds = playerService.getAnsweredQuestionIds(playerId);
        return ResponseEntity.ok(answeredQuestionIds);
    }


    @GetMapping("/{playerId}/answered")
    public ResponseEntity<List<Player.AnsweredQuestion>> answeredQuestions(
            @PathVariable String playerId) {

        List<Player.AnsweredQuestion> answeredQuestions = playerService.getAnsweredQuestions(playerId);
        return ResponseEntity.ok(answeredQuestions);
    }
    @GetMapping("/{playerId}")
    public ResponseEntity<Player> getPlayerDetails(@PathVariable String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        return ResponseEntity.ok(player);
    }
    @GetMapping("/{playerId}/questions/random")
    public ResponseEntity<?> getRandomUnansweredQuestion(@PathVariable String playerId) {
        try {
            Question randomQuestion = playerService.getRandomUnansweredQuestion(playerId);
            if (randomQuestion != null) {
                return ResponseEntity.ok(randomQuestion);
            } else {
                return ResponseEntity.status(404).body(Map.of("message", "No unanswered questions available."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error fetching random question.", "error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Player>> getSortedPlayers() {
        List<Player> players = playerRepository.findAll(Sort.by(Sort.Direction.DESC, "score"));
        return ResponseEntity.ok(players);
    }
}

