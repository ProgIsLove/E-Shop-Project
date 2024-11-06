package com.example.shopmebe.setting.state;

import com.example.shopmebe.fileutil.FileUtil;
import com.example.shopmebe.fileutil.MapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.State;
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

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StateRestController.class)
public class StateRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StateService stateServiceMock;

    @Autowired
    ObjectMapper objectMapper;

    List<StateResponse> states;

    @BeforeEach
    void setup() {

        states = List.of(
                new StateResponse(1, "Paris", new CountryDTO(1, "France")),
                new StateResponse(2, "Marseille", new CountryDTO(1, "France")));
    }

    @Test
    @WithMockUser(username = "nam", password = "something", roles = "Admin")
    public void testListByStates() throws Exception {
        int countryId = 2;
        String url = "/v1/states/country/" + countryId;
        when(stateServiceMock.getStateByCountry(countryId)).thenReturn(states);

        String statesJson = FileUtil.readFromJSONFileToString("json/state/statesResponse.json");
        String jsonResponse = MapperUtil.deserializeJSON(statesJson);

        ResultActions resultActions = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse));

        assertThat(states).hasSizeGreaterThan(0);
        JSONAssert.assertEquals(jsonResponse, resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser(username = "nam", password = "something", roles = "Admin")
    public void testCreateNewState() throws Exception {
        String url = "/v1/states";

        String statesJson = FileUtil.readFromJSONFileToString("json/state/stateRequest.json");

        when(stateServiceMock.addState(any(StateRequest.class))).thenReturn(new StateResponse(1, "Paris", new CountryDTO(1, "France")));

        ResultActions resultActions = mockMvc.perform(post(url).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(statesJson))
                .andExpect(status().isCreated());

        assertThat(resultActions.andReturn().getResponse().getStatus()).isEqualTo(201);
    }
}
