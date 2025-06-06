package com.example.shopmebe.category;

import com.example.shopmebe.exception.CategoryNotFoundException;
import com.example.shopmebe.exception.ConflictException;
import com.example.shopmebe.utils.CategoryPageInfo;
import com.shopme.common.entity.Category;
import com.shopme.common.request.CheckUniqueNameWithAliasRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class CategoryService {
    public  static final int ROOT_CATEGORIES_PER_PAGE = 4;
    private final CategoryRepository categoryRepository;

    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir,
                                     String keyword) {
        Sort sort = Sort.by("name");

        PageRequest pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sortDir.equals("asc") ? sort.ascending() : sort.descending());

        Page<Category> pageCategories;
        if (keyword != null && !keyword.isEmpty()) {
            pageCategories = categoryRepository.search(keyword, pageable);
        } else {
            pageCategories = categoryRepository.findRootCategories(pageable);
        }
        List<Category> rootCategories = pageCategories.getContent();

        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());

        if (keyword != null && !keyword.isEmpty()) {
            List<Category> searchResult = pageCategories.getContent();
            for (Category category : searchResult) {
                category.setHasChildren(!category.getChildren().isEmpty());
            }

            return searchResult;
        } else {
            return listHierarchicalCategories(rootCategories, sortDir);
        }
    }

    public Category save(Category category) {
        Category parent = category.getParent();
        if (parent != null) {
            String allParentIDs = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
            allParentIDs += parent.getId() + "-";
            category.setAllParentIDs(allParentIDs);
        }
        return categoryRepository.save(category);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category subCategory : children) {
                stringBuilder.delete(0, stringBuilder.length()); //clear StringBuilder
                stringBuilder.append("--").append(subCategory.getName());
                hierarchicalCategories.add(Category.copyFull(subCategory, String.valueOf(stringBuilder)));

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
        StringBuilder stringBuilder = new StringBuilder();

        for (Category subCategory : children) {
            stringBuilder.delete(0, stringBuilder.length()); //clear StringBuilder
            stringBuilder.append("--".repeat(Math.max(0, newSubLevel)));

            stringBuilder.append(subCategory.getName());
            hierarchicalCategories.add(Category.copyFull(subCategory, String.valueOf(stringBuilder)));

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
        StringBuilder stringBuilder = new StringBuilder();

        for (Category subCategory : children) {
            stringBuilder.delete(0, stringBuilder.length()); //clear StringBuilder
            stringBuilder.append("--".repeat(Math.max(0, newSubLevel)));

            Category copiedCategory = new Category();
            copiedCategory.setId(subCategory.getId());
            copiedCategory.setName(String.valueOf(stringBuilder.append(subCategory.getName())));

            categoriesUsedInForm.add(copiedCategory);

            SortedSet<Category> sortedSubCategories = sortSubCategories(subCategory.getChildren());

            listChildren(categoriesUsedInForm, sortedSubCategories, newSubLevel);
        }
    }

    public Category categoryById(Integer id) throws CategoryNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        String.format("Could not find any category with ID: %d", id)));
    }

    public void checkUnique(CheckUniqueNameWithAliasRequest checkUniqueNameWithAliasRequest) throws ConflictException {
        boolean isCreatingNew = (checkUniqueNameWithAliasRequest.id() == null || checkUniqueNameWithAliasRequest.id() == 0);

        Optional<Category> categoryByName = categoryRepository.findByName(checkUniqueNameWithAliasRequest.name());

        if (isCreatingNew) {
            if (categoryByName.isPresent()) {
                throw new ConflictException(
                        String.format("There is another category having same name %s",
                                checkUniqueNameWithAliasRequest.name()));
            } else {
                Optional<Category> categoryByAlias = categoryRepository.findByAlias(checkUniqueNameWithAliasRequest.alias());
                if (categoryByAlias.isPresent()) {
                    throw new ConflictException(
                            String.format("There is another category having same alias %s",
                                    checkUniqueNameWithAliasRequest.alias()));
                }
            }
        } else {
            if (categoryByName.isPresent() && !Objects.equals(categoryByName.get().getId(), checkUniqueNameWithAliasRequest.id())) {
                throw new ConflictException(
                        String.format("There is another category having same name %s", checkUniqueNameWithAliasRequest.name()));
            }
            Optional<Category> categoryByAlias = categoryRepository.findByAlias(checkUniqueNameWithAliasRequest.alias());
            if (categoryByAlias.isPresent() && !Objects.equals(categoryByAlias.get().getId(), checkUniqueNameWithAliasRequest.id())) {
                throw new ConflictException(
                        String.format(
                                "There is another category having same alias %s",
                                checkUniqueNameWithAliasRequest.alias()));
            }
        }
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

    @Transactional
    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    @Transactional
    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);
        if (countById == 0) {
            throw new CategoryNotFoundException(String.format("Could not find any category with ID %d", id));
        }

        categoryRepository.deleteById(id);
    }
}
