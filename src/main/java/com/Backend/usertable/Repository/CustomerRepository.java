package com.Backend.usertable.Repository;

import com.Backend.usertable.Models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public Customer findByEmail(String email);
    Page<Customer> findAll(Pageable pageable);


    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstname) LIKE LOWER(CONCAT('%', :category, '%')) OR " +
            "LOWER(c.lastname) LIKE LOWER(CONCAT('%', :category, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :category, '%')) " +
            "OR LOWER(c.state) LIKE LOWER(CONCAT('%', :category, '%')) OR LOWER(c.city) LIKE " +
            "LOWER(CONCAT('%', :category, '%')) OR LOWER(c.street) LIKE LOWER(CONCAT('%', :category, '%'))")
    List<Customer> searchByCategory(@Param("category") String category);

}
