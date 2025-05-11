package com.example.shopmefe.state;

import com.example.shopmefe.api.state.StateAPI;
import com.example.shopmefe.api.state.StateResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/bcstates")
public class StateController {

    private final StateAPI stateAPI;

    public StateController(StateAPI stateAPI) {
        this.stateAPI = stateAPI;
    }

    @GetMapping(value = "/{countryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StateResponse>> listStatesByCountry(@PathVariable("countryId") Integer countryId) {
        return ResponseEntity.ok(stateAPI.statesByCountry(countryId).block());
    }
}
