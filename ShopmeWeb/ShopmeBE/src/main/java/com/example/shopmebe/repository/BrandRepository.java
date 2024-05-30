package com.example.shopmebe.repository;

import com.shopme.common.entity.Brand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>, CrudRepository<Brand, Integer> {

    Long countById(Integer id);

    Brand findByName(String name);
}
