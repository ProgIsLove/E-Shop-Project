package customer;

import com.example.shopmefe.ShopmeFeApplication;
import com.example.shopmefe.customer.CustomerRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import testcontainers.AbstractIntegrationTest;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = ShopmeFeApplication.class)
public class CustomerRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager entityManager;

    Customer customer;

    Customer savedCustomer;

    @BeforeEach
    void setup() {
        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);

        customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Tom");
        customer.setLastName("Lucky");
        customer.setPassword("passw");
        customer.setEmail("tom.s.lucky@gmail.com");
//        customer.setPhoneNumber("312-462-756");
        customer.setAddressLine1("1972 West Drive");
        customer.setCity("Sacramento");
        customer.setState("California");
        customer.setPostalCode("9967");
        customer.setVerificationCode("code-543");
        customer.setCreatedTime(new Date());

        savedCustomer = customerRepository.save(customer);
    }

    @Test
    public void testCreateCustomer1() {
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);

    }

    @Test
    public void testUpdateCustomer() {
        Integer customerId = savedCustomer.getId();
        String lastName = "Stanfield";

        Optional<Customer> customerById = customerRepository.findById(customerId);

        assertThat(customerById).isPresent();

        customerById.get().setLastName(lastName);
        customerById.get().setEnabled(true);

        Customer updateCustomer = customerRepository.save(customerById.get());

        assertThat(updateCustomer.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void testGetCustomer() {
        Integer customerId = savedCustomer.getId();

        Optional<Customer> customerById = customerRepository.findById(customerId);

        assertThat(customerById).isPresent();
    }

    @Test
    public void testDeleteCustomer() {
        Integer customerId = savedCustomer.getId();

        customerRepository.deleteById(customerId);

        Optional<Customer> customerById = customerRepository.findById(customerId);
        assertThat(customerById).isNotPresent();
    }

    @Test
    public void testFindByEmail() {
        String email = savedCustomer.getEmail();

        Optional<Customer> customerByEmail = customerRepository.findByEmail(email);

        assertThat(customerByEmail).isPresent();
    }

    @Test
    public void testFindByVerificationCode() {
        String code = savedCustomer.getVerificationCode();

        Optional<Customer> byVerificationCode = customerRepository.findByVerificationCode(code);

        assertThat(byVerificationCode).isPresent();
    }

    @Test
    public void testEnabledCustomer() {
        Integer customerId = savedCustomer.getId();
        customerRepository.updateEnabledStatus(customerId);

        Optional<Customer> customerById = customerRepository.findById(customerId);

        assertThat(customerById.orElseThrow().isEnabled()).isTrue();
    }
}
