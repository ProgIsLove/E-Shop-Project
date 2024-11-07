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

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StateResponse> saveState(@RequestBody @Valid StateRequest state) {
        StateResponse stateResponse = stateService.addState(state);
        return ResponseEntity.status(HttpStatus.CREATED).body(stateResponse);
    }

    @PutMapping(value = "/{stateId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StateResponse> updateState(@PathVariable("stateId") Integer stateId,
                                        @RequestBody @Valid StateRequest state) throws StateNotFoundException {

        StateResponse stateUpdated = stateService.updateState(state, stateId);
        return ResponseEntity.ok(stateUpdated);
    }

    @DeleteMapping(value = "/{stateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteState(@PathVariable("stateId") Integer stateId) throws StateNotFoundException {
        stateService.delete(stateId);
        return ResponseEntity.noContent().build();
    }

}
