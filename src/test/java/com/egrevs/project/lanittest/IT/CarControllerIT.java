package com.egrevs.project.lanittest.IT;

import com.egrevs.project.lanittest.dto.CreateCarRequest;
import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.repository.CarRepository;
import com.egrevs.project.lanittest.util.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("integration")
public class CarControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    public static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    @DisplayName("Test create car functionality")
    void givenPersonAndCarToSaveRequests_whenSaveCar_thenReturnSuccessStatus() throws Exception {
        //given
        String personJson = """
                {
                    "id": 1,
                    "name": "Александр",
                    "birthday": "01.01.2000"
                }
                """;
        String carJson = """
                {
                    "id": 1,
                    "model": "Toyota-Camry",
                    "horsepower": 249,
                    "ownerId": 1
                }
                """;
        //when
        mockMvc.perform(post("/api/people")
                        .content(personJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/cars")
                .content(carJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        Car car = carRepository.findById(1L).orElseThrow();

        assertThat(car.getId()).isEqualTo(1L);
        assertThat(car.getVendor()).isEqualTo("Toyota");
        assertThat(car.getModel()).isEqualTo("Camry");
        assertThat(car.getHorsepower()).isEqualTo(249);

        assertThat(carRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Test create invalid car functionality")
    void givenCarToSaveWithoutPresentOwner_whenSaveCar_thenReturnBadRequestStatus() throws Exception{
        //given
        String carJson = """
                {
                    "id": 1,
                    "model": "Toyota-Camry",
                    "horsepower": 249,
                    "ownerId": null
                }
                """;
        Long countBefore = carRepository.count();
        //when
        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(carJson))
                .andExpect(status().isBadRequest());
        //then
        Long countAfter = carRepository.count();
        assertThat(countBefore).isEqualTo(countAfter);
    }
}
