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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        Integer id = 2;
        Optional<Category> categoryById = categoryRepository.findById(id);

        assertThat(categoryById).isPresent();
        assertThat(categoryById.get().getId()).isPositive();
        assertThat(categoryById.get().getChildren().size()).isPositive();
        assertThat(categoryById.get().getChildren()).size().isEqualTo(3);
    }
}
