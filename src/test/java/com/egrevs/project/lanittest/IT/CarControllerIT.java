package com.egrevs.project.lanittest.IT;

import com.egrevs.project.lanittest.config.TestContainersConfig;
import com.egrevs.project.lanittest.dto.CreateCarRequest;
import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.repository.CarRepository;
import com.egrevs.project.lanittest.util.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@Import(TestContainersConfig.class)
public class CarControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarRepository carRepository;

    //TODO написать прям руками objectMapper.writeValueAsString(carRequest), тоесть прям String json = ... и поправить ошибку
    @Test
    void givenCarToSaveRequest_whenSaveCar_thenReturnSuccessStatus() throws Exception {
        //given
        CreateCarRequest carRequest = DataUtils.getCarRequest();
        //when
        mockMvc.perform(post("/api/cars")
                .content(objectMapper.writeValueAsString(carRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        //then
        Car savedCar = carRepository.findAll()
                .stream()
                .filter(car -> car.getId().equals(carRequest.id()))
                .findFirst()
                .get();
        assertThat(savedCar).isEqualTo(carRequest);
    }

}
