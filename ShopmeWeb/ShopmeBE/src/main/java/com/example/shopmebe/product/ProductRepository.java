package com.example.shopmebe.product;

import com.shopme.common.entity.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>, CrudRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    @Query("UPDATE Product p SET p.enabled = :enabled WHERE p.id = :productId")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void updateEnabledStatus(@Param("productId") Integer id, @Param("enabled") boolean enabled);

    Long countById(Integer id);
}
