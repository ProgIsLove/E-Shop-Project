package com.example.shopmefe.customer;

import com.shopme.common.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer>, CrudRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Optional<Customer> findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Customer c WHERE c.verificationCode = :code")
    Optional<Customer> findByVerificationCode(@Param("code") String code);

    @Query("UPDATE Customer c SET c.enabled = :enabled WHERE c.id = :customerId")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void updateEnabledStatus(@Param("customerId") Integer customerId, @Param("enabled") boolean enabled);
}
