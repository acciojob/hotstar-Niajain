package com.driver.repository;

import com.driver.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription,Integer> {


    Optional<Subscription> findById(Integer userId);
    List<Subscription> findAll();
}
