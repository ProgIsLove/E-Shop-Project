package com.example.shopmebe.setting.country;

import com.example.shopmebe.exception.CountryNotFoundException;
import com.example.shopmebe.fileutil.FileUtil;
import com.example.shopmebe.fileutil.MapperUtil;
import com.shopme.common.entity.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CountryRestController.class)
public class CountryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryServiceMock;

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
        when(countryServiceMock.getAllCountries()).thenReturn(countries);

        String countriesJson = FileUtil.readFromJSONFileToString("json/country/countriesResponse.json");
        String jsonResponse = MapperUtil.deserializeCountries(countriesJson);

        ResultActions resultActions = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse));

        JSONAssert.assertEquals(jsonResponse, resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testCreateNewCountry() throws Exception {
        String url = "/v1/countries";

        String countryJson = FileUtil.readFromJSONFileToString("json/country/countryRequest.json");

        doNothing().when(countryServiceMock).addCountry(any(Country.class));

        ResultActions resultActions = mockMvc.perform(post(url).with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(countryJson))
                .andExpect(status().isCreated());

        assertThat(resultActions.andReturn().getResponse().getStatus()).isEqualTo(201);
    }

    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testUpdateCountry() throws Exception {
        String url = "/v1/countries/1";

        String countryJson = FileUtil.readFromJSONFileToString("json/country/countryRequest.json");
        Country country = MapperUtil.deserializeCountry(countryJson);

        when(countryServiceMock.updateCountry(any(Country.class), any(Integer.class))).thenReturn(country);

        ResultActions resultActions = mockMvc.perform(put(url).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(countryJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(country.getId()))
                .andExpect(jsonPath("$.name").value(country.getName()))
                .andExpect(jsonPath("$.code").value(country.getCode()));

        verify(countryServiceMock, times(1)).updateCountry(any(Country.class), eq(1));

        assertThat(resultActions.andReturn().getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testUpdateCountryNotFound() throws Exception {
        String url = "/v1/countries/999";

        String countryJson = FileUtil.readFromJSONFileToString("json/country/countryRequest.json");

        when(countryServiceMock.updateCountry(any(Country.class), any(Integer.class)))
                .thenThrow(new CountryNotFoundException("Country not found"));

        MvcResult mvcResult = mockMvc.perform(put(url)
                        .with(csrf())
                        .content(countryJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        verify(countryServiceMock, times(1)).updateCountry(any(Country.class), any(Integer.class));

        String expectedResponse = FileUtil.readFromJSONFileToString("json/country/countryErrorResponse.json");

        JSONAssert.assertEquals(expectedResponse, responseBody, false);
    }

    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testDeleteCountryNotFound() throws Exception {
        String url = "/v1/countries/1";

        doNothing().when(countryServiceMock).delete(1);

        mockMvc.perform(delete(url)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(countryServiceMock, times(1)).delete(1);
    }
}
