package shopme.user;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.repository.CategoryRepository;
import com.shopme.common.entity.Category;
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

        assertThat(categoryById).isPresent();
        assertThat(categoryById.get().getId()).isPositive();


        Set<Category> expectedChildren = new HashSet<>();
        expectedChildren.add(Category.builder().id(6).name("Cameras").enabled(true).build());
        expectedChildren.add(Category.builder().id(7).name("Smartphones").enabled(true).build());

        Category expectedParent = Category.builder()
                .id(1)
                .name("Electronics")
                .enabled(true)
                .children(expectedChildren)
                .build();

        Set<Category> actualChildren = new HashSet<>(categoryById.get().getChildren());

        assertThat(actualChildren.size()).isPositive();
        assertThat(actualChildren).size().isEqualTo(2);
        assertThat(actualChildren).isEqualTo(expectedChildren);

        assertThat(categoryById.get().getName()).isEqualTo(expectedParent.getName());

        for (Category category : actualChildren) {
            assertThat(expectedChildren).contains(category);
        }

        for (Category category : expectedChildren) {
            assertThat(actualChildren).contains(category);
        }

//        assertThatCode(() -> assertThat(categoryById.get()).usingRecursiveComparison().isEqualTo(expectedParent))
//                .doesNotThrowAnyException();
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
}
