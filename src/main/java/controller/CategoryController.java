package com.jeancaio.financecontrol.controller;

import com.jeancaio.financecontrol.model.Category;
import com.jeancaio.financecontrol.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        return ResponseEntity.ok(categories);
    }
}