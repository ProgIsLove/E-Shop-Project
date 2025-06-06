package com.example.shopmebe.setting.state;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {

    Long countById(Integer id);

    List<State> findByCountryIdOrderByNameAsc(Integer countryId);
}
