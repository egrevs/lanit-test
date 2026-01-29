package com.egrevs.project.lanittest.service;

import com.egrevs.project.lanittest.dto.CreateCarRequest;
import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.exception.InvalidFieldException;
import com.egrevs.project.lanittest.exception.PersonIsNotAdultException;
import com.egrevs.project.lanittest.exception.PersonNotFoundException;
import com.egrevs.project.lanittest.exception.PersonWithoutCarsException;
import com.egrevs.project.lanittest.repository.CarRepository;
import com.egrevs.project.lanittest.repository.PersonRepository;
import com.egrevs.project.lanittest.service.validator.CarsValidator;
import com.egrevs.project.lanittest.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CarsValidator carsValidator;

    @InjectMocks
    private CarService carService;

    @Test
    @DisplayName("Test car saving functionality")
    public void givenCarToSave_whenSaveCar_thenRepositoryIsCalled() {
        //given
        Car car = DataUtils.getToyotaMarkCar();
        Person person = DataUtils.getAdultAndreyPerson();
        car.setOwner(person);
        CreateCarRequest carToSave = new CreateCarRequest(
                car.getId(),
                car.getVendor() + "-" + car.getModel(),
                car.getHorsepower(),
                car.getOwner().getId()
        );
        BDDMockito.given(personRepository.findById(person.getId()))
                .willReturn(Optional.of(person));
        BDDMockito.given(carRepository.save(any(Car.class)))
                .willReturn(car);
        //when
        carService.createCar(carToSave);
        //then
        BDDMockito.verify(carsValidator).validateCarRequest(carToSave);

        ArgumentCaptor<Car> carArgumentCaptor = ArgumentCaptor.forClass(Car.class);
        BDDMockito.then(carRepository).should(times(1)).save(carArgumentCaptor.capture());
        Car capturedCar = carArgumentCaptor.getValue();

        assertThat(capturedCar.getId()).isEqualTo(carToSave.id());
        assertThat(capturedCar.getVendor() + "-" + capturedCar.getModel()).isEqualTo(carToSave.model());
        assertThat(capturedCar.getHorsepower()).isEqualTo(carToSave.horsepower());
        assertThat(capturedCar.getOwner().getId()).isEqualTo(carToSave.ownerId());
    }

    @Test
    @DisplayName("Test car saving with invalid owner id functionality")
    public void givenCarToSaveWithInvalidOwner_whenSaveCar_thenExceptionIsThrown() {
        //given
        Long notExistsOwnerId = 999L;
        CreateCarRequest carToSave = new CreateCarRequest(
                1L, "Toyota-Camry", 210, notExistsOwnerId
        );
        BDDMockito.given(personRepository.findById(notExistsOwnerId))
                .willReturn(Optional.empty());
        //when
        //then
        assertThrows(PersonNotFoundException.class, () -> carService.createCar(carToSave));
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    @DisplayName("Test car with not adult owner functionality")
    public void givenCarToSaveWithInvalidOwnerAge_whenSaveCar_thenExceptionIsThrown() {
        //given
        Person person = DataUtils.getDmitriyPerson();
        CreateCarRequest carToSave = new CreateCarRequest(
                1L, "Toyota-Camry", 210, person.getId()
        );
        BDDMockito.given(personRepository.findById(person.getId()))
                .willReturn(Optional.of(person));
        BDDMockito.willThrow(new PersonIsNotAdultException("Пользователь младше 18 лет"))
                .given(carsValidator).validateCarRequest(carToSave);
        //when
        //then
        assertThrows(PersonIsNotAdultException.class, () -> carService.createCar(carToSave));
        verify(carsValidator).validateCarRequest(carToSave);
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    @DisplayName("Test car with incorrect format of model functionality")
    public void givenCarToSaveWithIncorrectModelFormat_whenSaveCar_thenExceptionIsThrown() {
        //given
        Person person = DataUtils.getDmitriyPerson();
        CreateCarRequest carToSave = new CreateCarRequest(
                1L, "Toyota:Camry", 210, person.getId()
        );
        BDDMockito.given(personRepository.findById(person.getId()))
                .willReturn(Optional.of(person));
        //when
        //then
        assertThrows(InvalidFieldException.class, () -> carService.createCar(carToSave));
        verify(carsValidator).validateCarRequest(carToSave);
        verify(carRepository, never()).save(any(Car.class));
    }

}