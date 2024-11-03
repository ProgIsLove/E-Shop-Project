package com.example.shopmebe.setting;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.setting.state.StateRepository;
import com.example.shopmebe.testcontainers.AbstractIntegrationTest;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeBeApplication.class)
public class StateRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setup() {
        Integer countryId = 1;
        testEntityManager.persistAndFlush(new Country("India", "IN"));
        Country country = testEntityManager.find(Country.class, countryId);
        stateRepository.save(new State("West Bengal", country));
        stateRepository.save(new State("Gujarat", country));
        stateRepository.save(new State("Karnataka", country));
    }

    @Test
    public void testListStatesByCountry() {
        Integer countryId = 1;

        List<State> states = stateRepository.findByIdOrderByNameAsc(countryId);

        assertThat(states.size()).isGreaterThan(0);
        assertThat(states.stream().map(State::getName))
                .hasSize(3)
                .containsExactlyInAnyOrder("Gujarat", "Karnataka", "West Bengal");
    }
}
