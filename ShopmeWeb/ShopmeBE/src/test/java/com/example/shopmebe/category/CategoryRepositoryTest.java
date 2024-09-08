package com.example.shopmebe.category;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.testcontainers.AbstractIntegrationTest;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeBeApplication.class)
public class CategoryRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testGetCategoryWithSubCategories() {
        Integer id = 1;
        Optional<Category> categoryById = categoryRepository.findById(id);


        Category expectedParent = new Category("Electronics");
        expectedParent.setId(1);
        expectedParent.setImage("image-thumbnail.png");
        expectedParent.setEnabled(true);

        Category category1 = new Category("Cameras");
        category1.setId(6);
        category1.setImage("image-thumbnail.png");
        category1.setEnabled(true);

        Category category2 = new Category("Smartphones");
        category2.setId(7);
        category2.setImage("image-thumbnail.png");
        category2.setEnabled(true);

        expectedParent.addSubCategory(category1);
        expectedParent.addSubCategory(category2);

        assertThat(categoryById).isPresent();
        assertThat(categoryById.get().getId()).isPositive();
        assertThatCode(() -> assertThat(categoryById.get()).usingRecursiveComparison().isEqualTo(expectedParent))
                .doesNotThrowAnyException();
    }

    @Test
    void testDuplicateEntriesInSet() {
        Set<Category> categories = new HashSet<>();

        Category expectedParent = new Category(2, "Notebook", "notebook");
        expectedParent.setEnabled(true);

        Category subCategory = new Category(3, "Notebook", "abc");
        subCategory.setEnabled(false);

        categories.add(expectedParent);
        categories.add(subCategory);

        // Expecting only one entry in the set as both categories have the same name
        assertEquals(1, categories.size());
    }

    @Test
    public void testGetCategoryWithNestedSubCategories() {
        Integer id = 2;

        Optional<Category> categoryById = categoryRepository.findById(id);

        Category expectedParent = new Category("Computers");
        expectedParent.setId(2);
        expectedParent.setEnabled(true);

        Category subCategory = new Category("Notebook");
        subCategory.setId(3);
        subCategory.setEnabled(true);

        Category subCategory1 = new Category("Desktops");
        subCategory1.setId(4);
        subCategory1.setEnabled(true);

        Category subCategory2 = new Category("Computer Components");
        subCategory2.setId(5);
        subCategory2.setEnabled(true);

        expectedParent.addSubCategory(subCategory);
        expectedParent.addSubCategory(subCategory1);
        expectedParent.addSubCategory(subCategory2);

        Category category3 = new Category("Hard disk");
        category3.setId(8);
        category3.setEnabled(true);
        subCategory2.addSubCategory(category3);

        assertThat(categoryById).isPresent();
        assertThat(categoryById.get().getId()).isPositive();
        assertThat(categoryById.get().getChildren().size()).isPositive();
        assertThat(categoryById.get().getChildren()).size().isEqualTo(3);

        assertThatCode(() -> assertThat(categoryById.get()).usingRecursiveComparison().isEqualTo(expectedParent))
                .doesNotThrowAnyException();
    }

    @Test
    public void testFindByName() {
        String name = "Computers";
        Optional<Category> categoryByName = categoryRepository.findByName(name);

        assertThat(categoryByName).isNotNull();
        assertTrue(categoryByName.isPresent());
        assertThat(categoryByName.get().getName()).isEqualTo(name);
    }

    @Test
    public void testFindByNameNotFound() {
        String name = "test";
        Optional<Category> categoryByName = categoryRepository.findByName(name);

        assertThat(categoryByName).isEmpty();
    }

    @Test
    public void testFindByAlias() {
        String name = "Computers";
        Optional<Category> categoryByAlias = categoryRepository.findByAlias(name);

        assertThat(categoryByAlias).isNotNull();
    }

    @Test
    public void testFindByAliasNotFound() {
        String name = "test";
        Optional<Category> categoryByAlias = categoryRepository.findByAlias(name);

        assertThat(categoryByAlias).isEmpty();
    }
}
