package com.toystorewithdb.toystore;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ToyRepository extends JpaRepository<Toy, Integer> {
}
