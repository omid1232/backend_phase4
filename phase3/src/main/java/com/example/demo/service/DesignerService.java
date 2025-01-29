package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Designer;
import com.example.demo.model.Question;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.DesignerRepository;
import com.example.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignerService {

    @Autowired
    private DesignerRepository designerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Cache all questions by designer ID
     */
    @Cacheable(value = "questionsByDesigner", key = "#designerId")
    public List<Question> getQuestionsByDesigner(String designerId) {
        List<Question> questions = questionRepository.findByDesignerId(designerId);

        questions.forEach(question -> {
            if (question.getCategoryId() != null) {
                Category category = categoryRepository.findById(question.getCategoryId())
                        .orElse(null);
                if (category != null) {
                    question.setCategoryId(category.getName());
                }
            }
        });

        return questions;
    }

    /**
     * Cache all questions by category ID
     */
    @Cacheable(value = "questionsByCategory", key = "#categoryId")
    public List<Question> getQuestionsByCategory(String categoryId) {
        return questionRepository.findByCategoryId(categoryId);
    }

    /**
     * When creating a new question, remove cached data for the affected designer & category
     */
    @CacheEvict(value = {"questionsByDesigner", "questionsByCategory"}, allEntries = true)
    public Question createQuestion(Question question, String designerId) {
        // Validate the designer
        Designer designer = designerRepository.findById(designerId)
                .orElseThrow(() -> new IllegalArgumentException("Designer not found"));

        // Validate the categoryId
        if (question.getCategoryId() == null || question.getCategoryId().isEmpty()) {
            throw new IllegalArgumentException("Category ID cannot be null or empty");
        }

        // Ensure the category exists
        categoryRepository.findById(question.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        // Set the designerId and save the question
        question.setDesignerId(designerId);
        designer.getCreatedQuestions().add(question);
        designerRepository.save(designer);

        return questionRepository.save(question);
    }
}
