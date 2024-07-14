package shopme.product;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.product.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import shopme.testcontainers.AbstractIntegrationTest;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeBeApplication.class)
public class ProductRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;


    private Product expectedProduct;

    @BeforeEach
    public void setUp() {
        Brand brand = entityManager.find(Brand.class, 3);
        Category category = entityManager.find(Category.class, 3);

        this.expectedProduct = new Product();
        expectedProduct.setId(2);
        expectedProduct.setName("Acer Aspire Desktop");
        expectedProduct.setAlias("acer_aspire_desktop");
        expectedProduct.setShortDescription("Short description for Acer Aspire");
        expectedProduct.setFullDescription("Full description for Acer Aspire");
        expectedProduct.setEnabled(true);
        expectedProduct.setInStock(true);

        expectedProduct.setBrand(brand);
        expectedProduct.setCategory(category);

        expectedProduct.setPrice(678);
        expectedProduct.setCost(600);
        expectedProduct.setMainImage(null);
    }

    @Test
    public void testCreateProduct() {
        Brand brand = entityManager.find(Brand.class, 3);
        Category category = entityManager.find(Category.class, 7);

        Product product = new Product();
        product.setName("Samsung Galaxy A31");
        product.setAlias("samsung_galaxy_a31");
        product.setShortDescription("A good smartphone from Samsung");
        product.setFullDescription("This is a very good smartphone full description");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(456);
        product.setCreatedTime(new Date());
        product.setModifiedTime(new Date());
        product.setMainImage(null);

        Product saveProduct = productRepository.save(product);

        assertNotNull(saveProduct);
        assertThat(saveProduct.getId()).isGreaterThan(0);
        assertThat(saveProduct.getName()).isEqualTo("Samsung Galaxy A31");
    }

    @Test
    public void testListAllProducts() {
        Iterable<Product> iterableProducts = productRepository.findAll();

        assertThat(iterableProducts).isNotNull();
        assertThatCode(() -> assertThat(iterableProducts.iterator().next())
                .usingRecursiveComparison().isEqualTo(this.expectedProduct))
                .doesNotThrowAnyException();
    }

    @Test
    public void testFindProductById() {
        Integer id = 2;
        Optional<Product> productById = productRepository.findById(id);

        assertThat(productById.isPresent()).isTrue();
        assertThatCode(() -> assertThat(productById.get())
                .usingRecursiveComparison().isEqualTo(this.expectedProduct))
                .doesNotThrowAnyException();
    }

    @Test
    public void testUpdateProduct() {
        Integer id = 2;
        Optional<Product> productById = productRepository.findById(id);

        assertThat(productById.isPresent()).isTrue();

        Product product = productById.get();
        product.setCost(499);

        productRepository.save(product);

        Product updatedProduct = entityManager.find(Product.class, id);

        assertThat(updatedProduct.getCost()).isEqualTo(499);
    }

    @Test
    public void testDeleteProduct() {
        Integer id = 2;

        productRepository.deleteById(id);

        Optional<Product> productById = productRepository.findById(id);

        assertThat(productById.isPresent()).isFalse();
    }

    @Test
    public void testSaveProductWithImages() {
        Integer productId = 1;
        Product product = productRepository.findById(productId).get();

        product.setMainImage("main image-1.jpg");
        product.addExtraImage("main image-1.jpg");
        product.addExtraImage("main image-2.png");
        product.addExtraImage("main image-3.png");

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }

    @Test
    public void testSaveProductWithDetails() {
        Integer productId = 1;
        Product product = productRepository.findById(productId).get();

        product.addDetail("Device memory", "128gb");
        product.addDetail("CPU Model", "MediaTek");
        product.addDetail("OS", "Android 10");

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getDetails()).isNotEmpty();
        assertThat(savedProduct.getDetails().size()).isEqualTo(3);
    }
}
