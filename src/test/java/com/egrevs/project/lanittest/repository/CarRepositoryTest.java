package com.egrevs.project.lanittest.repository;

import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void setUp() {
        carRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save car functionality")
    public void givenCarObject_whenSave_thenCarIsCreated() {
        //given
        Car carToSave = DataUtils.getBmwCar();
        //when
        Car savedCar = carRepository.save(carToSave);
        //then
        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getId()).isNotNull();
        assertThat(savedCar.getHorsepower()).isEqualTo(249);
        assertThat(savedCar.getVendor()).isEqualTo("BMW");
        assertThat(savedCar.getModel()).isEqualTo("X5");
    }

    @Test
    @DisplayName("Test count cars functionality")
    public void givenThreePersonToSave_whenCount_thenCountIsReturned() {
        //given
        Person person = DataUtils.getDmitriyPerson();
        Car car1 = DataUtils.getBmwCar();
        Car car2 = DataUtils.getToyotaCamryCar();
        Car car3 = DataUtils.getToyotaMarkCar();
        car1.setOwner(person);
        car2.setOwner(person);
        car3.setOwner(person);
        personRepository.save(person);
        carRepository.saveAll(List.of(car1, car2, car3));
        //when
        Long counter = carRepository.count();
        //then
        assertThat(counter).isEqualTo(3L);
    }

    @Test
    @DisplayName("Test unique car's vendors functionality")
    public void givenThreeNotUniqueByVendorCarsToSave_whenCountByVendor_thenUniqueCountByVendorIsReturned() {
        //given
        Person person = DataUtils.getDmitriyPerson();
        Car car1 = DataUtils.getBmwCar();
        Car car2 = DataUtils.getToyotaCamryCar();
        Car car3 = DataUtils.getToyotaMarkCar();
        car1.setOwner(person);
        car2.setOwner(person);
        car3.setOwner(person);
        personRepository.save(person);
        carRepository.saveAll(List.of(car1, car2, car3));
        //when
        Long counter = carRepository.countByVendor();
        //then
        assertThat(counter).isEqualTo(2L);
    }

    @Test
    @DisplayName("Test invalid data - no owner functionality")
    public void givenCarWithoutOwnerToSave_whenSave_thenExceptionIsReturned() {
        //given
        Car car = DataUtils.getToyotaMarkCar();
        //when
        carRepository.save(car);
        //then
        assertThrows(DataIntegrityViolationException.class, () -> carRepository.flush());
    }

    @Test
    @DisplayName("Test transient person in context functionality")
    public void givenCarAndTransientPerson_whenSaveOnlyCar_thenPersistExceptionIsReturned() {
        //given
        Person person = DataUtils.getVladimirPerson();
        Car car = DataUtils.getToyotaMarkCar();
        car.setOwner(person);
        //when
        carRepository.save(car);
        //then
        assertThrows(DataIntegrityViolationException.class, () -> carRepository.flush());
        /*
        carRepository.flush() синхронизация persistence context с БД,
        flush() отправляет запрос в БД на INSERT и вот тут возникает ошибка на уровне БД,
        а до тех пор все хранится в persistence context
        */
    }
}