package shopme.category;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeBeApplication.class)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("shopmedbTest")
            .withUsername("root")
            .withPassword("rootTest");


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

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
    void equalsAndHashCodeTest() {
        Set<Category> categories = new HashSet<>();

        Category expectedParent = new Category("Notebook");
        expectedParent.setId(2);
        expectedParent.setEnabled(true);

        Category subCategory = new Category("Notebook");
        subCategory.setId(3);
        subCategory.setEnabled(true);

        categories.add(expectedParent);
        categories.add(subCategory);

        Assertions.assertEquals(1, categories.size());
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
        Category categoryByName = categoryRepository.findByName(name);

        assertThat(categoryByName).isNotNull();
        assertThat(categoryByName.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByNameNotFound() {
        String name = "test";
        Category categoryByName = categoryRepository.findByName(name);

        assertThat(categoryByName).isNull();
    }

    @Test
    public void testFindByAlias() {
        String name = "Computers";
        Category categoryByAlias = categoryRepository.findByAlias(name);

        assertThat(categoryByAlias).isNotNull();
    }

    @Test
    public void testFindByAliasNotFound() {
        String name = "test";
        Category categoryByAlias = categoryRepository.findByAlias(name);

        assertThat(categoryByAlias).isNull();
    }
}
