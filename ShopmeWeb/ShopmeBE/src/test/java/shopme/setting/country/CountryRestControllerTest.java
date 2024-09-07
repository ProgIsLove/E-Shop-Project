package shopme.setting.country;

import com.example.shopmebe.setting.country.CountryRepository;
import com.example.shopmebe.setting.country.CountryRestController;
import com.shopme.common.entity.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shopme.fileutil.FileUtil;
import shopme.fileutil.MapperUtil;
import shopme.testcontainers.AbstractIntegrationTest;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
                new Country(1, "China", "CN"),
                new Country(2, "Sweden", "SWE"),
                new Country(3, "France", "FRA")
        );

        // Configure the mock repository to return the list of countries
        when(countryRepository.findAllByOrderByNameAsc()).thenReturn(countries);
    }


    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testListCountries() throws Exception {
        String url = "/v1/countries";

        String countriesJson = FileUtil.readFromJSONFileToString("response/countriesResponse.json");
        String jsonResponse = MapperUtil.deserializeCountries(countriesJson);

        ResultActions resultActions = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        JSONAssert.assertEquals(jsonResponse, resultActions.andReturn().getResponse().getContentAsString(), false);
    }
}
