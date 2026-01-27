package com.egrevs.project.lanittest.repository;

import com.egrevs.project.lanittest.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    long count();

    @Query(value = "select count(distinct vendor) from Car")
    long countByVendor();
}
