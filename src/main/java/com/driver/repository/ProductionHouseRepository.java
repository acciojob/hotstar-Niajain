package com.driver.repository;

import com.driver.model.ProductionHouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductionHouseRepository extends JpaRepository<ProductionHouse,Integer> {
    @Override
    Optional<ProductionHouse> findById(Integer id);
}
