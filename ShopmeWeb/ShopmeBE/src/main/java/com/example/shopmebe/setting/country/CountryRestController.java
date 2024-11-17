package com.example.shopmebe.setting.country;

import com.example.shopmebe.exception.ConflictException;
import com.example.shopmebe.exception.CountryNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/countries")
public class CountryRestController {

    private final CountryService countryService;

    public CountryRestController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CountryResponse>> countries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryResponse> newCountry(@RequestBody @Valid CountryRequest country) throws ConflictException {
        CountryResponse newCountry = countryService.addCountry(country);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCountry);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryResponse> updateCountry(@PathVariable Integer id,
                                                         @RequestBody @Valid CountryRequest country) throws CountryNotFoundException {

        CountryResponse countryUpdated = countryService.updateCountry(country, id);
        return ResponseEntity.ok(countryUpdated);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Integer id) throws CountryNotFoundException {
        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
