package com.example.demo.service;

import com.example.demo.model.Question;
import com.example.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Cache all questions by designer ID
     */
    @Cacheable(value = "questionsByDesigner", key = "#designerId")
    public List<Question> getQuestionsByDesigner(String designerId) {
        System.out.println("Fetching questions from database for designer ID: " + designerId);
        return questionRepository.findByDesignerId(designerId);
    }

    /**
     * When creating a new question, remove cached data for the affected designer
     */
    @CacheEvict(value = "questionsByDesigner", key = "#designerId")
    public Question createQuestion(Question question, String designerId) {
        System.out.println("Evicting cache for designer ID: " + designerId);

        question.setDesignerId(designerId);
        return questionRepository.save(question);
    }
}
