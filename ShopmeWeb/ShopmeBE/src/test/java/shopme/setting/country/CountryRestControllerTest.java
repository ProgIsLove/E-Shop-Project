package shopme.setting.country;

import com.example.shopmebe.setting.country.CountryRepository;
import com.example.shopmebe.setting.country.CountryRestController;
import com.shopme.common.entity.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import shopme.testcontainers.AbstractIntegrationTest;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@ContextConfiguration(classes = CountryRestController.class)
public class CountryRestControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryRepository countryRepository;

    @BeforeEach
    void setup() {
        List<Country> countries = List.of(
                new Country("China", "CN"),
                new Country("Sweden", "SWE"),
                new Country("France", "FRA")
        );

        // Configure the mock repository to return the list of countries
        when(countryRepository.findAllByOrderByNameAsc()).thenReturn(countries);
    }


    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testListCountries() throws Exception {
        String url = "/v1/countries";
        MvcResult mvcResult = mockMvc.perform(get(url))
                .andExpect(status().isOk()).andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(contentAsString);
    }
}
