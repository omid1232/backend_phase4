package com.example.demo.controller;
import com.example.demo.model.Player;
import com.example.demo.model.Designer;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.DesignerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginRegisterController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private DesignerRepository designerRepository;

    @PostMapping("/player")
    public ResponseEntity<?> loginPlayer(@RequestBody Player loginRequest) {
        System.out.println("kiiiiir");
        Optional<Player> player = playerRepository.findByUsername(loginRequest.getUsername());

        if (player.isPresent() && player.get().getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.ok().body("{\"message\":\"Player login successful\"}");
        }
        return ResponseEntity.status(401).body("{\"message\":\"Invalid player credentials\"}");
    }

    @PostMapping("/designer")
    public ResponseEntity<?> loginDesigner(@RequestBody Designer loginRequest) {
        Designer designer = designerRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid designer credentials"));

        if (designer.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "designerId", designer.getId()
            ));
        }

        return ResponseEntity.status(401).body(Map.of("message", "Invalid designer credentials"));
    }

    @PostMapping("/register/player")
    public ResponseEntity<?> registerPlayer(@RequestBody Player playerRequest) {
        System.out.println("Registering player: " + playerRequest.getUsername());
        Optional<Player> existingPlayer = playerRepository.findByUsername(playerRequest.getUsername());

        if (existingPlayer.isPresent()) {
            return ResponseEntity.status(400).body("{\"message\":\"Username already exists for a player.\"}");
        }

        Player newPlayer = new Player();
        newPlayer.setUsername(playerRequest.getUsername());
        newPlayer.setPassword(playerRequest.getPassword());
        newPlayer.setScore(0);

        playerRepository.save(newPlayer);
        return ResponseEntity.ok().body("{\"message\":\"Player registered successfully.\"}");
    }
    @PostMapping("/register/designer")
    public ResponseEntity<?> registerDesigner(@RequestBody Designer designerRequest) {
        System.out.println("Registering designer: " + designerRequest.getUsername());
        Optional<Designer> existingDesigner = designerRepository.findByUsername(designerRequest.getUsername());

        if (existingDesigner.isPresent()) {
            return ResponseEntity.status(400).body("{\"message\":\"Username already exists for a designer.\"}");
        }

        Designer newDesigner = new Designer();
        newDesigner.setUsername(designerRequest.getUsername());
        newDesigner.setPassword(designerRequest.getPassword());

        designerRepository.save(newDesigner);
        return ResponseEntity.ok().body("{\"message\":\"Designer registered successfully.\"}");
    }
}

