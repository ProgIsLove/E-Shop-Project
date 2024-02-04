package com.example.shopmebe.service;

import com.example.shopmebe.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository repo;

    public List<Category> listALl() {
        return (List<Category>) repo.findAll();
    }
}
