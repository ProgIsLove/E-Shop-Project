package com.example.shopmebe.repository;

import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer>, CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByEmail(@Param("email") String email);

    Long countById(Integer id);

    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName,' ', u.lastName, u.email) LIKE %:keyword%")
    Page<User> findByFullnameOrEmail(@Param("keyword") String keyword, Pageable pageable);

    @Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :id")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void updateEnabledStatus(@Param("id") Integer id, @Param("enabled") boolean enabled);
}
