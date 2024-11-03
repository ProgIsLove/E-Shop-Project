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
    private final CountryMapper countryMapper;

    public CountryService(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    private Country getCountryById(Integer id) throws CountryNotFoundException {
        return countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("Country not found"));
    }

    public List<CountryResponse> getAllCountries() {
        return countryMapper.convertToDTO(countryRepository.findAllByOrderByNameAsc());
    }

    public CountryResponse addCountry(CountryRequest countryRequest) throws ConflictException {
        Country country = countryMapper.convertToEntity(countryRequest);
        return Optional.of(country)
                .filter(c -> !countryRepository.existsByNameOrCode(country.getName().trim(),
                        country.getCode().toUpperCase()))
                .map(countryRepository::save)
                .map(countryMapper::convertEntityToResponse)
                .orElseThrow(() -> new ConflictException("Country name or code already exists"));
    }

    @Transactional
    public CountryResponse updateCountry(CountryRequest country, Integer id) throws CountryNotFoundException {
        return Optional.ofNullable(this.getCountryById(id)).map(oldCountry -> {
            oldCountry.setName(country.name().trim());
            oldCountry.setCode(country.code().toUpperCase());
            Country updateCountry = countryRepository.save(oldCountry);
            return countryMapper.convertEntityToResponse(updateCountry);
        }).orElseThrow(() -> new CountryNotFoundException("Country not found"));
    }

    @Transactional
    public void delete(Integer id) throws CountryNotFoundException {
        Long countById = countryRepository.countById(id);
        if (countById == 0) {
            throw new CountryNotFoundException("Country not found");
        }

        countryRepository.deleteById(id);
    }
}
