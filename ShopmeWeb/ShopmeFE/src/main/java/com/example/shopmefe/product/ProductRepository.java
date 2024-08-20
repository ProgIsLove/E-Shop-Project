package com.example.shopmefe.product;

import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = true "
            + "AND (p.category.id = :categoryId OR p.category.allParentIDs LIKE %:categoryIDMatch%) "
            + "ORDER BY p.name ASC")
    Page<Product> listByCategory(@Param("categoryId") Integer categoryId,
                                 @Param("categoryIDMatch") String categoryIDMatch,
                                 Pageable pageable);

    Optional<Product> findByAlias(String alias);

    @Query(value = "SELECT * FROM products WHERE enabled = true AND "
            + "MATCH(name, short_description, full_description) AGAINST (:keyword)",
          nativeQuery = true)
    Page<Product> search(@Param("keyword")String keyword, Pageable pageable);
}
