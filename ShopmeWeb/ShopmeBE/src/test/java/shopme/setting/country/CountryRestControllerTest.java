package shopme.setting.country;

import com.example.shopmebe.setting.country.CountryRepository;
import com.example.shopmebe.setting.country.CountryRestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpStatus;
import com.shopme.common.entity.Country;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shopme.fileutil.FileUtil;
import shopme.fileutil.MapperUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@ContextConfiguration(classes = CountryRestController.class)
public class CountryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryRepository countryRepositoryMock;

    List<Country> countries;

    @BeforeEach
    void setup() {
        countries = List.of(
                new Country(1, "China", "CN"),
                new Country(2, "Sweden", "SWE"),
                new Country(3, "France", "FRA")
        );
    }

    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testListCountries() throws Exception {
        String url = "/v1/countries";
        when(countryRepositoryMock.findAllByOrderByNameAsc()).thenReturn(countries);

        String countriesJson = FileUtil.readFromJSONFileToString("json/country/countriesResponse.json");
        String jsonResponse = MapperUtil.deserializeCountries(countriesJson);

        ResultActions resultActions = mockMvc.perform(get(url).contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(jsonResponse));

        JSONAssert.assertEquals(jsonResponse, resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testCreateNewCountry() throws Exception {
        String url = "/v1/countries/new-country";

        String countryJson = FileUtil.readFromJSONFileToString("json/country/countryRequest.json");
        Country country = MapperUtil.deserializeCountry(countryJson);

        when(countryRepositoryMock.save(any(Country.class))).thenReturn(country);

        ResultActions resultActions = mockMvc.perform(post(url).with(csrf())
                .contentType("application/json")
                .content(countryJson))
                .andExpect(status().isCreated());

        assertThat(resultActions.andReturn().getResponse().getStatus()).isEqualTo(201);
    }
}
