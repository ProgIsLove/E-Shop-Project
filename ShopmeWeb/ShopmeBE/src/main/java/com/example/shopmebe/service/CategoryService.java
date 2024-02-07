package com.example.shopmebe.service;

import com.example.shopmebe.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository repo;

    public List<Category> listALl() {
        return (List<Category>) repo.findAll();
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categories = repo.findAll();

        for(Category category : categories) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.builder()
                        .name(category.getName()).build());

                listChildren(categoriesUsedInForm, category.getChildren(), 0);
            }
        }

        return categoriesUsedInForm;
    }

    private void listChildren(List<Category> categoriesUsedInForm, Set<Category> children, int subLevel) {
        int newSubLevel = subLevel + 1;
        StringBuilder sb = new StringBuilder();

        for (Category subCategory : children) {
            sb.delete(0, sb.length()); //clear StringBuilder
            sb.append("--".repeat(Math.max(0, newSubLevel)));

            categoriesUsedInForm.add(Category.builder()
                    .name(sb + subCategory.getName()).build());
            listChildren(categoriesUsedInForm, subCategory.getChildren(), newSubLevel);
        }
    }
}
