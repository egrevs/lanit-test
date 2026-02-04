package com.egrevs.project.lanittest.IT;

import com.egrevs.project.lanittest.repository.CarRepository;
import com.egrevs.project.lanittest.repository.PersonRepository;
import com.egrevs.project.lanittest.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("integration")
public class ClearControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CarRepository carRepository;

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
    @DisplayName("Test delete all functionality")
    public void givenEntitiesToSave_whenDeleteAll_thenStatusIsSuccess() throws Exception {
        //given
        personRepository.saveAll(List.of(
                DataUtils.getAdultAndreyPerson(),
                DataUtils.getDmitriyPerson()));

        carRepository.saveAll(List.of(
                DataUtils.getAndreyBmwCar(),
                DataUtils.getAndreyBmwCar()));

        assertThat(carRepository.count()).isGreaterThan(0L);
        assertThat(personRepository.count()).isGreaterThan(0L);
        //when
        mockMvc.perform(get("/api/clear"))
                .andExpect(status().isOk());
        //then
        assertThat(carRepository.count()).isEqualTo(0L);
        assertThat(personRepository.count()).isEqualTo(0L);
    }
}
