package com.example.shopmebe.repository;

import com.shopme.common.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>, CrudRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    List<Category> findRootCategories(Sort sort);

    Long countById(Integer id);
    Category findByName(String name);

    Category findByAlias(String name);

    @Query("UPDATE Category c SET c.enabled = :enabled WHERE c.id = :categoryId")
    @Modifying
    void updateEnabledStatus(@Param("categoryId") Integer id, @Param("enabled") boolean enabled);
}
