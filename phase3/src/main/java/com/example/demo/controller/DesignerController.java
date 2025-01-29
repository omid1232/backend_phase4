package com.example.demo.controller;

import com.example.demo.model.Designer;
import com.example.demo.model.Question;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.DesignerService;
import com.example.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.DesignerRepository;
import java.util.List;

@RestController
@RequestMapping("/api/designers")
public class DesignerController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private DesignerService designerService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private DesignerRepository designerRepository;

    @GetMapping("/{designerId}/questions")
    public ResponseEntity<List<Question>> getQuestionsByDesigner(@PathVariable String designerId) {
//        System.out.println("annnn");
        List<Question> questions = designerService.getQuestionsByDesigner(designerId);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/{designerId}/questions/create")
    public ResponseEntity<Question> createQuestion(
            @PathVariable String designerId,
            @RequestBody Question question) {
        System.out.println(designerId);
        System.out.println(question);
//        System.out.println("sexxx");
        Question createdQuestion = questionService.createQuestion(question, designerId);
        return ResponseEntity.ok(createdQuestion);
    }

        @GetMapping
        public ResponseEntity<List<Designer>> getAllDesigners() {
//            System.out.println("kiiiiiiir");
            List<Designer> designers = designerRepository.findAll();
            return ResponseEntity.ok(designers);
        }


}



