package com.egrevs.project.lanittest.controller;

import com.egrevs.project.lanittest.service.PersonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClearController.class)
class ClearControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    @DisplayName("Test delete all functionality")
    public void givenVoid_whenDeleteAll_thenSuccessResponse() throws Exception {
        //given
        BDDMockito.doNothing().when(personService).clearAll();
        //when
        mockMvc.perform(get("/api/clear"))
                .andExpect(status().isOk());
        //then
        verify(personService, times(1)).clearAll();
    }
}