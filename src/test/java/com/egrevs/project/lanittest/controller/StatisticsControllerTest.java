package com.egrevs.project.lanittest.controller;

import com.egrevs.project.lanittest.dto.StatisticsDto;
import com.egrevs.project.lanittest.service.StatisticService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(StatisticsController.class)
class StatisticsControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticService statisticService;

    @Test
    @DisplayName("Test get statistics functionality")
    public void givenVoid_whenGetStatistics_thenSuccessResponse() throws Exception {
        //given
        StatisticsDto statisticsDto = DataUtils.getStatisticsDto();
        BDDMockito.given(statisticService.getStatistics())
                .willReturn(statisticsDto);
        //when
        mockMvc.perform(get("/api/statistics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //then
        verify(statisticService, times(1)).getStatistics();
    }
}