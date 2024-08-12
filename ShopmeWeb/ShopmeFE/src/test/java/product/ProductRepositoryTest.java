package product;

import com.example.shopmefe.ShopmeFeApplication;
import com.example.shopmefe.category.CategoryRepository;
import com.example.shopmefe.product.ProductRepository;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import testcontainers.AbstractIntegrationTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeFeApplication.class)
public class ProductRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindCategoryByAliasNotFound() {
        String alias = "aliasNotExist";
        Optional<Product> product = productRepository.findByAlias(alias);

        assertThat(product).isNull();
    }

    @Test
    public void testFindCategoryByAlias() {
        String alias = "acer_aspire_desktop";
        Optional<Product> product = productRepository.findByAlias(alias);

        assertThat(product).isNotNull();
    }
}
