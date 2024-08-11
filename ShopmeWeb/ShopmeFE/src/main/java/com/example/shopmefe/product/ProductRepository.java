package com.example.shopmefe.product;

import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = true "
            + "AND (p.category.id = :categoryId OR p.category.allParentIDs LIKE %:categoryIDMatch%) "
            + "ORDER BY p.name ASC")
    Page<Product> listByCategory(@Param("categoryId") Integer categoryId,
                                 @Param("categoryIDMatch") String categoryIDMatch,
                                 Pageable pageable);
}
