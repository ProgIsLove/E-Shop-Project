package com.example.shopmebe.repository;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>, CrudRepository<Brand, Integer> {

    Long countById(Integer id);

    Brand findByName(String name);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %:keyword%")
    Page<Brand> findAll(@Param("keyword") String keyword, Pageable pageable);
}
