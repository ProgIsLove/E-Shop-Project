package category;

import com.example.shopmefe.ShopmeFeApplication;
import com.example.shopmefe.category.CategoryRepository;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import testcontainers.AbstractIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeFeApplication.class)
public class CategoryRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testListEnabledCategories() {
        List<Category> allEnabled = categoryRepository.findAllEnabled();

        assertThat(allEnabled).isNotEmpty();
    }

    @Test
    public void testFindCategoryByAliasNotFound() {
        String alias = "aliasNotExist";
        Optional<Category> enabledCategoryByAlias = categoryRepository.findEnabledCategoryByAlias(alias);

        assertThat(enabledCategoryByAlias).isNull();
    }

    @Test
    public void testFindCategoryByAlias() {
        String alias = "Electronics";
        Optional<Category> enabledCategoryByAlias = categoryRepository.findEnabledCategoryByAlias(alias);

        assertThat(enabledCategoryByAlias).isNotNull();
    }
}
