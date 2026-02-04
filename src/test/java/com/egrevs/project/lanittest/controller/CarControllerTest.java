package com.egrevs.project.lanittest.controller;

import com.egrevs.project.lanittest.dto.CreateCarRequest;
import com.egrevs.project.lanittest.service.CarService;
import com.egrevs.project.lanittest.util.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @Test
    @DisplayName("Test create car functionality")
    public void givenCreateCarRequest_whenCreateCar_thenSuccessResponse() throws Exception {
        //given
        CreateCarRequest carToSave = DataUtils.getCarRequest();
        BDDMockito.doNothing().when(carService).createCar(carToSave);
        //when
        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carToSave)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //then
        verify(carService, times(1)).createCar(carToSave);
    }

    @Test
    @DisplayName("Test create invalid car functionality")
    public void givenCreatInvalidCarRequest_whenCreateCar_thenBadRequestResponse() throws Exception {
        //given
        CreateCarRequest carToSave = DataUtils.getInvalidCarRequest();
        //when
        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carToSave)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        //then
        verifyNoInteractions(carService);
    }
}