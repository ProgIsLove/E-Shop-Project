package com.example.shopmebe.setting.country;

import com.example.shopmebe.exception.ConflictException;
import com.example.shopmebe.exception.CountryNotFoundException;
import com.shopme.common.entity.Country;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country getCountryById(Integer id) throws CountryNotFoundException {
        return countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("Country not found"));
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public void addCountry(Country country) throws ConflictException {
        Optional.of(country).filter(c -> !countryRepository.existsByName(country.getName()))
                .map(countryRepository::save)
                .orElseThrow(() -> new ConflictException(country.getName() + " already exists"));
    }

    public Country updateCountry(Country country, Integer id) throws CountryNotFoundException {
        return Optional.ofNullable(this.getCountryById(id)).map(oldCountry -> {
            oldCountry.setName(country.getName());
            oldCountry.setCode(country.getCode());
            return countryRepository.save(oldCountry);
        }).orElseThrow(() -> new CountryNotFoundException("Country not found"));
    }

    @Transactional
    public void delete(Integer id) throws CountryNotFoundException{
        Long countById = countryRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new CountryNotFoundException("Country not found");
        }

        countryRepository.deleteById(id);
    }
}
