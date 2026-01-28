package com.egrevs.project.lanittest.repository;

import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void setUp(){
        personRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save person functionality")
    public void givenPersonObject_whenSave_thenPersonIsCreated(){
        //given
        Person personToSave = DataUtils.getDmitriyPerson();
        //when
        Person savedPerson = personRepository.save(personToSave);
        //then
        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getId()).isNotNull();
        assertThat(savedPerson.getId()).isEqualTo(1L);
        assertThat(savedPerson.getCars()).isEmpty();
    }

    @Test
    @DisplayName("Test get person functionality")
    public void givenPersonCreated_whenGetById_thenPersonIsReturned(){
        //given
        Person personToSave = DataUtils.getDmitriyPerson();
        Person savedPerson = personRepository.save(personToSave);
        //when
        Person obtainedPerson = personRepository.findById(savedPerson.getId()).orElse(null);
        //then
        assertThat(obtainedPerson).isNotNull();
        assertThat(obtainedPerson.getId()).isEqualTo(savedPerson.getId());
    }

    @Test
    @DisplayName("Test count people functionality")
    public void givenThreePersonToSave_whenCount_thenPeopleCountIsReturned(){
        //given
        Person person1 = DataUtils.getDmitriyPerson();
        Person person2 = DataUtils.getSergeyPerson();
        Person person3 = DataUtils.getVladimirPerson();
        personRepository.saveAll(List.of(person1, person2, person3));
        //when
        Long counter = personRepository.count();
        //then
        assertThat(counter).isEqualTo(3L);
    }
}