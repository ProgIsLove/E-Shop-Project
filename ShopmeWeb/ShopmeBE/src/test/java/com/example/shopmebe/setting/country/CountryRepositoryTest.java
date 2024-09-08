package com.example.shopmebe.setting.country;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.testcontainers.AbstractIntegrationTest;
import com.shopme.common.entity.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeBeApplication.class)
public class CountryRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    void setup() {
        countryRepository.save(new Country("China", "CN"));
        countryRepository.save(new Country("Sweden", "SWE"));
        countryRepository.save(new Country("France", "FRA"));

    }

    @Test
    public void testListCountries() {
        List<Country> countries = countryRepository.findAllByOrderByNameAsc();

        assertThat(countries.size()).isGreaterThan(0);
        assertThat(countries.stream().map(Country::getName))
                .hasSize(3)
                .containsExactlyInAnyOrder("China", "France", "Sweden");
    }
}
