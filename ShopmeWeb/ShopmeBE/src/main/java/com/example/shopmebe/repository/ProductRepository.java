package com.example.shopmebe.repository;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>, CrudRepository<Product, Integer> {

    Product findByName(String name);
}
