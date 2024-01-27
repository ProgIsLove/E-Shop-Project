package com.example.shopmebe.repository;

import com.shopme.common.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>, CrudRepository<Category, Integer> {

}
