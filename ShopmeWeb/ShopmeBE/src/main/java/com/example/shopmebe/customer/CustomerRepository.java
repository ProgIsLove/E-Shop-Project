package com.example.shopmebe.customer;

import com.shopme.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer>, CrudRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, ' ', "
            + "c.addressLine1, ' ', c.addressLine2, ' ', c.city, ' ', c.state, "
            + "' ', c.postalCode, ' ', c.country.name) LIKE %:keyword%")
    Page<Customer> findAll(String keyword, Pageable pageable);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Integer id);

    @Query("UPDATE Customer c SET c.enabled = :enabled, c.verificationCode = null WHERE c.id = :customerId")
    @Modifying
    @Transactional
    void updateEnabledStatus(@Param("customerId") Integer customerId, @Param("enabled") boolean enabled);

    Long countById(Integer id);
}
