package com.example.shopmebe.setting.country;

import com.shopme.common.entity.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Integer> {

    Long countById(Integer id);

    List<Country> findAllByOrderByNameAsc();

    boolean existsByName(String name);


}
