package com.example.shopmebe.setting.country;

import com.shopme.common.entity.Country;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/countries")
public class CountryRestController {

    private final CountryRepository countryRepository;

    public CountryRestController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Country>> country() {
        return ResponseEntity.ok(countryRepository.findAllByOrderByNameAsc());
    }

    @PostMapping(value = "/new-country", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newCountry(@RequestBody Country country) {
        countryRepository.save(country);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
