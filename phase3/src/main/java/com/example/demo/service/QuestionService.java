package com.example.demo.service;

import com.example.demo.model.Question;
import com.example.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getQuestionsByDesigner(String designerId) {
        return questionRepository.findByDesignerId(designerId);
    }

    public Question createQuestion(Question question,String designerId) {
        question.setDesignerId(designerId);
        return questionRepository.save(question);
    }
}

