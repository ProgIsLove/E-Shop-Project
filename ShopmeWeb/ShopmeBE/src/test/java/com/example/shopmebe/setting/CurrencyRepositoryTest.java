package com.example.shopmebe.setting;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.testcontainers.AbstractIntegrationTest;
import com.shopme.common.entity.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeBeApplication.class)
public class CurrencyRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setup() {
        List<Currency> currencies = Arrays.asList(
                new Currency("United States Dollar", "$", "USD"),
                new Currency("British Pound", "£", "GPB")
        );

        currencyRepository.saveAll(currencies);
    }

    @Test
    public void testCreateCurrencies() {
        Iterable<Currency> iterable = currencyRepository.findAll();

        assertThat(iterable).size().isEqualTo(2);
    }

    @Test
    public void testListAllOrderByNameAsc() {
        List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();

        assertThat(currencies.size()).isGreaterThan(0);
    }
}
