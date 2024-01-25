package shopme.user;

import com.example.shopmebe.ShopmeBeApplication;
import com.example.shopmebe.repository.UserRepository;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeBeApplication.class)
public class UserRepositoryContainersTest {

    @Autowired
    private UserRepository userRepository;

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
    public void findUserById() {
        Optional<User> userById = userRepository.findById(1);

        User expectedUser = new User();
        expectedUser.setId(1);
        expectedUser.setFirstName("Nam");
        expectedUser.setLastName("Ha Minh");
        expectedUser.setEmail("nam@codejava.net");
        expectedUser.setEnabled(true);
        expectedUser.setPhotos("namhm.png");
        expectedUser.setPassword("$2a$10$bDqskP9Z/y6BIZnFLgJ8HuwMYaZXD9w2jVk2pYHXgn1k6N4nckleu");

        assertThat(userById).isPresent();
        assertThat(userById.get().getId()).isPositive();
        assertThatCode(() -> assertThat(userById.get()).usingRecursiveComparison().isEqualTo(expectedUser))
                .doesNotThrowAnyException();
    }

    @Test
    public void findUserByIdNotExists() {
        Integer id = 2000;
        Optional<User> userById = userRepository.findById(id);

        assertThat(userById).isEmpty();
    }

    @Test
    public void findAllUsersSortByName() {
        int size = 0;
        Iterable<User> usersIterable = userRepository.findAll(Sort.by("firstName").ascending());

        if (usersIterable instanceof Collection<User>) {
            size = ((Collection<User>) usersIterable).size();
        }

        assertNotNull(usersIterable);
        assertEquals(2, size);
        assertThat(convertIterableToList(usersIterable)).isSortedAccordingTo(Comparator.comparing(User::getFirstName));
    }

    @Test
    public void findAllUsersSortByEmail() {
        int size = 0;
        Iterable<User> usersIterable = userRepository.findAll(Sort.by("email").ascending());

        if (usersIterable instanceof Collection<User>) {
            size = ((Collection<User>) usersIterable).size();
        }

        assertNotNull(usersIterable);
        assertEquals(2, size);
        assertThat(convertIterableToList(usersIterable)).isSortedAccordingTo(Comparator.comparing(User::getEmail));
    }

    private <S> List<S> convertIterableToList(Iterable<S> iterableList) {
        return StreamSupport.stream(iterableList.spliterator(), false)
                .collect(Collectors.toList());
    }
}
