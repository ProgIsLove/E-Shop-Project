package com.example.shopmebe.brand;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.testcontainers.AbstractIntegrationTest;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeBeApplication.class)
public class BrandRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void testCreateBrand1() {
        Category laptop = new Category(6);
        Brand acer = new Brand(1, "Acer");

        acer.getCategories().add(laptop);

        Brand savedBrand = brandRepository.save(acer);

        assertThat(savedBrand.getId()).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
        assertThat(savedBrand.getName()).isEqualTo("Acer");
        assertThat(savedBrand.getCategories().size()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand2() {
        Category cellphones = new Category(4);
        Category tablets = new Category(6);

        Brand apple = new Brand(2, "Apple");

        apple.getCategories().add(cellphones);
        apple.getCategories().add(tablets);

        Brand savedBrand = brandRepository.save(apple);

        assertThat(savedBrand.getId()).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
        assertThat(savedBrand.getName()).isEqualTo("Apple");
    }

    @Test
    public void testCreateBrand3() {
        Optional<Brand> brandById = brandRepository.findById(4);

        assertThat(brandById).isPresent();
        assertThat(brandById.get().getId()).isPositive();
        assertThat(brandById.get().getName()).isEqualTo("Huawei");
        assertThat(brandById.get().getCategories().size()).isEqualTo(2);
    }

    @Test
    public void testFindAll() {
        Iterable<Brand> brands = brandRepository.findAll();

        assertThat(brands).isNotNull();
    }

    @Test
    public void testGetById() {
        Brand brand = brandRepository.findById(3)
                .orElseThrow(() -> new NoSuchElementException("Brand with ID 3 not found"));

        assertNotNull(brand, "Brand with ID 3 not found");

        assertEquals("Samsung", brand.getName(), "Brand name does not match expected value");
    }

    @Test
    public void testUpdateName() {
        String newName = "Samsung Electronics";
        Brand samsung = brandRepository.findById(3)
                .orElseThrow(() -> new NoSuchElementException("Brand with ID 3 not found"));
        samsung.setName(newName);

        Brand saveBrand = brandRepository.save(samsung);
        assertThat(saveBrand.getName()).isEqualTo(newName);
    }

    @Test
    public void testDelete() {
        Integer id = 2;
        brandRepository.deleteById(id);

        Optional<Brand> brand = brandRepository.findById(id);

        assertThat(brand.isPresent()).isFalse();
    }
}
