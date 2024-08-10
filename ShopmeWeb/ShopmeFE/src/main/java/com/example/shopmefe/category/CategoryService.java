package com.example.shopmefe.category;

import com.shopme.common.entity.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listNoChildrenCategories() {
        ArrayList<Category> listNoChildrenCategories = new ArrayList<>();

        List<Category> allEnabledCategories = categoryRepository.findAllEnabled();

        allEnabledCategories.forEach(category -> {
            Set<Category> children = category.getChildren();
            if (children == null || children.isEmpty()) {
                listNoChildrenCategories.add(category);
            }
        });

        return listNoChildrenCategories;
    }
}
