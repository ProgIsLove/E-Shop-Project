package com.example.shopmebe.setting.state;


import com.example.shopmebe.exception.StateNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/states")
public class StateRestController {

    private final StateService stateService;

    public StateRestController(StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping(value = "/country/{countryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StateResponse>> listStateByCountry(@PathVariable("countryId") Integer countryId) {
        return ResponseEntity.ok(stateService.getStateByCountry(countryId));
    }

    @PostMapping
    public ResponseEntity<StateResponse> saveState(@RequestBody @Valid StateRequest state) {
        StateResponse stateResponse = stateService.addState(state);
        return ResponseEntity.status(HttpStatus.CREATED).body(stateResponse);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Integer id) throws StateNotFoundException {
        stateService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
