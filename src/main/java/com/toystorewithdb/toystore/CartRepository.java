package com.toystorewithdb.toystore;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


public interface CartRepository extends JpaRepository<CartRecord, Integer> {

    CartRecord findByUsername(String username);
    Optional<CartRecord> findByUsernameAndToyid(String username, int toyid);
    List<CartRecord> findAllByUsername(String username);
    int deleteByCartid(int cartid);
    long countByUsername(String username);
}
