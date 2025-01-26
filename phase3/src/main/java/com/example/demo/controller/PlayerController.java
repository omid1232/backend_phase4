package com.example.demo.controller;

import com.example.demo.dto.FollowedDesignerDTO;
import com.example.demo.model.Player;
import com.example.demo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.AnswerQuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/{playerId}/follow")
    public ResponseEntity<Player> followDesigner(
            @PathVariable String playerId,
            @RequestBody FollowedDesignerDTO followedDesignerDTO) {
        Player updatedPlayer = playerService.followDesigner(playerId, followedDesignerDTO);
        return ResponseEntity.ok(updatedPlayer);
    }

    @PostMapping("/{playerId}/answer")
    public ResponseEntity<Player> answerQuestion(
            @PathVariable String playerId,
            @RequestBody AnswerQuestionDTO answerDTO) {
        Player updatedPlayer = playerService.answerQuestion(playerId, answerDTO);
        return ResponseEntity.ok(updatedPlayer);
    }

    @GetMapping("/{playerId}/answered")
    public ResponseEntity<List<Player.AnsweredQuestion>> answeredQuestions(
            @PathVariable String playerId) {

        List<Player.AnsweredQuestion> answeredQuestions = playerService.getAnsweredQuestions(playerId);
        return ResponseEntity.ok(answeredQuestions);
    }
}
