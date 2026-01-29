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

@Slf4j
public class DataUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy");

    public static Person getDmitriyPerson() {
        Person person = new Person();
        person.setId(1L);
        person.setName("Дмитрий");
        person.setBirthday(new Date());
        return person;
    }

    public static Person getSergeyPerson() {
        Person person = new Person();
        person.setId(2L);
        person.setName("Сергей");
        person.setBirthday(new Date());
        return person;
    }

    public static Person getVladimirPerson() {
        Person person = new Person();
        person.setId(3L);
        person.setName("Владимир");
        person.setBirthday(new Date());
        return person;
    }

    public static Person getAdultAndreyPerson() {
        Person person = new Person();
        person.setId(1L);
        person.setName("Андрей");
        person.setBirthday(Date.from(LocalDate.now()
                .minusYears(18)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()));
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
        String dateStr = "01.01.2000";
        try {
            Date date = sdf.parse(dateStr);
            return new CreatePersonRequest(
                    1L,
                    "Александр",
                    date
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static CreatePersonRequest getInvalidPersonRequest() {
        String dateStr = "01.01.2000";
        try {
            Date date = sdf.parse(dateStr);
            return new CreatePersonRequest(
                    null,
                    "Александр",
                    date
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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
                2L
        );
    }
}
