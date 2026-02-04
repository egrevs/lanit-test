package com.egrevs.project.lanittest.IT;

import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.repository.PersonRepository;
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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("integration")
public class PersonControllerIT {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("Test save person functionality")
    void givenPersonToSave_whenSavePerson_thenSuccessResponse() throws Exception {
        //given
        String personJson = """
                {
                    "id": 1,
                    "name": "Александр",
                    "birthday": "01.01.2000"
                }
                """;
        //when
        mockMvc.perform(post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isOk());
        //then
        Person person = personRepository.findById(1L).orElseThrow();

        assertThat(person.getId()).isEqualTo(1L);
        assertThat(person.getName()).isEqualTo("Александр");
        assertThat(person.getBirthday()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(personRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Test save invalid person functionality")
    void givenInvalidBirthdayPersonToSave_whenSavePerson_thenBadRequestStatus() throws Exception {
        //given
        String invalidPersonJson = """
                {
                    "id": 1,
                    "name": "Александр",
                    "birthday": "2000.01.01"
                }
                """;
        Long countBefore = personRepository.count();
        //when
        mockMvc.perform(post("/api/people")
                        .content(invalidPersonJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        //then
        Long countAfter = personRepository.count();
        assertThat(countBefore).isEqualTo(countAfter);
    }
}
