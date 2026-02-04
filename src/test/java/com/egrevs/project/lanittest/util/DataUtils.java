package com.egrevs.project.lanittest.util;

import com.egrevs.project.lanittest.dto.CreateCarRequest;
import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.dto.StatisticsDto;
import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.entity.Person;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class DataUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy");

    public static Person getDmitriyPerson() {
        Person person = new Person();
        person.setId(1L);
        person.setName("Дмитрий");
        person.setBirthday(LocalDate.of(2000, 1, 1));
        return person;
    }

    public static Person getSergeyPerson() {
        Person person = new Person();
        person.setId(2L);
        person.setName("Сергей");
        person.setBirthday(LocalDate.of(2003, 2, 21));
        return person;
    }

    public static Person getInvalidDatePerson(){
        Person person = new Person();
        person.setId(5L);
        person.setName("Бродяга");
        person.setBirthday(LocalDate.of(2021, 2, 21));
        return person;
    }

    public static Person getVladimirPerson() {
        Person person = new Person();
        person.setId(3L);
        person.setName("Владимир");
        person.setBirthday(LocalDate.of(2011, 12, 1));
        return person;
    }

    public static Person getAdultAndreyPerson() {
        Person person = new Person();
        person.setId(1L);
        person.setName("Андрей");
        person.setBirthday(LocalDate.of(2000, 1, 1));
        return person;
    }

    public static Car getToyotaMarkCar() {
        Car car = new Car();
        car.setId(1L);
        car.setVendor("Toyota");
        car.setModel("Mark");
        car.setHorsepower(210);
        return car;
    }

    public static Car getToyotaCamryCar() {
        Car car = new Car();
        car.setId(2L);
        car.setVendor("Toyota");
        car.setModel("Camry");
        car.setHorsepower(230);
        return car;
    }

    public static Car getBmwCar() {
        Car car = new Car();
        car.setId(3L);
        car.setVendor("BMW");
        car.setModel("X5");
        car.setHorsepower(249);
        return car;
    }

    public static Car getAndreyBmwCar() {
        Car car = new Car();
        car.setId(1L);
        car.setVendor("BMW");
        car.setModel("X5");
        car.setHorsepower(249);
        car.setOwner(getAdultAndreyPerson());
        return car;
    }

    public static Car getAndreyToyotaCar() {
        Car car = new Car();
        car.setId(2L);
        car.setVendor("Toyota");
        car.setModel("Camry");
        car.setHorsepower(249);
        car.setOwner(getAdultAndreyPerson());
        return car;
    }

    public static CreatePersonRequest getPersonRequest() {
            return new CreatePersonRequest(
                    1L,
                    "Александр",
                    LocalDate.of(2000, 1, 1)
            );
    }

    public static CreatePersonRequest getInvalidPersonRequest() {
            return new CreatePersonRequest(
                    null,
                    "Александр",
                    LocalDate.of(2000, 1, 1)
            );
    }

    public static CreateCarRequest getCarRequest() {
        return new CreateCarRequest(
                1L,
                "Toyota-Camry",
                200,
                1L
        );
    }

    public static CreateCarRequest getInvalidCarRequest() {
        return new CreateCarRequest(
                null,
                "Toyota-Camry",
                200,
                1L
        );
    }

    public static StatisticsDto getStatisticsDto() {
        return new StatisticsDto(
                1L,
                2L,
                2L,
                1L,
                2L
        );
    }
}
