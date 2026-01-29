package com.egrevs.project.lanittest.controller;

import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.dto.PersonWithCarsResponse;
import com.egrevs.project.lanittest.exception.PersonNotFoundException;
import com.egrevs.project.lanittest.service.PersonService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @Test
    @DisplayName("Test create person functionality")
    public void givenCreatePersonRequest_whenCreatePerson_thenSuccessResponse() throws Exception {
        //given
        CreatePersonRequest personRequest = DataUtils.getPersonRequest();
        BDDMockito.doNothing().when(personService).createPerson(any(CreatePersonRequest.class));
        //when
        mockMvc.perform(post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //then
        verify(personService, times(1)).createPerson(any(CreatePersonRequest.class));
    }

    @Test
    @DisplayName("Test create invalid person functionality")
    public void givenCreateInvalidPersonRequest_whenCreatePerson_thenSuccessResponse() throws Exception {
        //given
        CreatePersonRequest personRequest = DataUtils.getInvalidPersonRequest();
        BDDMockito.doNothing().when(personService).createPerson(any(CreatePersonRequest.class));
        //when
        mockMvc.perform(post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        //then
        verify(personService, never()).createPerson(any(CreatePersonRequest.class));
    }

    @Test
    @DisplayName("Test get person by id functionality")
    public void givenPersonId_whenGetPersonWithCars_thenSuccessResponse() throws Exception {
        //given
        Long personId = 1L;

        BDDMockito.given(personService.getPeopleWithCars(personId))
                .willReturn(any(PersonWithCarsResponse.class));
        //when
        mockMvc.perform(get("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("personId", personId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //then
        verify(personService).getPeopleWithCars(personId);
    }

    @Test
    @DisplayName("Test get person by incorrect id functionality")
    public void givenInvalidPersonId_whenGetPersonWithCars_thenBadRequestResponse() throws Exception {
        //given
        Long personId = 1L;

        BDDMockito.given(personService.getPeopleWithCars(personId))
                .willThrow(PersonNotFoundException.class);
        //when
        mockMvc.perform(get("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("personId", personId.toString()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        //then
    }
}