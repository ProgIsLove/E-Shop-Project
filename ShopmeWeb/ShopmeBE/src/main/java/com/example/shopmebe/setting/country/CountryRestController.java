package com.example.shopmebe.setting.country;

import com.example.shopmebe.exception.ConflictException;
import com.example.shopmebe.exception.CountryNotFoundException;
import com.shopme.common.entity.Country;
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
    public ResponseEntity<List<Country>> country() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newCountry(@RequestBody Country country) throws ConflictException {
        countryService.addCountry(country);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Country> updateCountry(@PathVariable Integer id, @RequestBody Country country) throws CountryNotFoundException {
        Country countryUpdated = countryService.updateCountry(country, id);
        return ResponseEntity.ok(countryUpdated);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Integer id) throws CountryNotFoundException {
        countryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
