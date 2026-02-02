package com.egrevs.project.lanittest.repository;

import com.egrevs.project.lanittest.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    long count();

    @Query(value = "select count(*) from Person as p where p.cars is empty")
    long countPeopleWithoutCars();
}
