package com.example.shopmebe.service;

import com.example.shopmebe.exception.CategoryNotFoundException;
import com.example.shopmebe.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> listALl(String sortDir) {
        Sort sort = Sort.by("name");
        List<Category> category = categoryRepository.findRootCategories(sortDir.equals("asc") ? sort.ascending() : sort.descending());
        return listHierarchicalCategories(category, sortDir);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category subCategory : children) {
                sb.delete(0, sb.length()); //clear StringBuilder
                sb.append("--").append(subCategory.getName());
                hierarchicalCategories.add(Category.copyFull(subCategory, String.valueOf(sb)));

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                               Category parent,
                                               int subLevel,
                                               String sortDir) {
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;
        StringBuilder sb = new StringBuilder();

        for (Category subCategory : children) {
            sb.delete(0, sb.length()); //clear StringBuilder
            sb.append("--".repeat(Math.max(0, newSubLevel)));

            sb.append(subCategory.getName());
            hierarchicalCategories.add(Category.copyFull(subCategory, String.valueOf(sb)));

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
        }
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categories = categoryRepository.findRootCategories(Sort.by("name").ascending());

        for (Category category : categories) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                SortedSet<Category> sortedSubCategories = sortSubCategories(category.getChildren());

                listChildren(categoriesUsedInForm, sortedSubCategories, 0);
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
                    .id(subCategory.getId())
                    .name(String.valueOf(sb.append(subCategory.getName())))
                    .build());

            SortedSet<Category> sortedSubCategories = sortSubCategories(subCategory.getChildren());

            listChildren(categoriesUsedInForm, sortedSubCategories, newSubLevel);
        }
    }

    public Category categoryById(Integer id) throws CategoryNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        String.format("Could not find any category with ID: %d", id)));
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);

        Category categoryByName = categoryRepository.findByName(name);

        if (isCreatingNew) {
            if (categoryByName != null) {
                return "Duplicate Name";
            } else {
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "Duplicate Alias";
                }
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != null) {
                return "Duplicate Name";
            }

            Category categoryByAlias = categoryRepository.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != null) {
                return "Duplicate Alias";
            }
        }

        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        TreeSet<Category> sortedChildren = new TreeSet<>((cat1, cat2) -> {
            if (sortDir.equals("asc")) {
                return cat1.getName().compareTo(cat2.getName());
            }
            return cat2.getName().compareTo(cat1.getName());
        });

        sortedChildren.addAll(children);
        return sortedChildren;
    }
}
