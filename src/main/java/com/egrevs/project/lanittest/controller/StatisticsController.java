package com.egrevs.project.lanittest.controller;

import com.egrevs.project.lanittest.dto.StatisticsDto;
import com.egrevs.project.lanittest.service.StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/statistics")
public class StatisticsController {

    private final StatisticService statisticService;

    public StatisticsController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping
    @Operation(summary = "Получить статистику по пользователю и машинам")
    public ResponseEntity<StatisticsDto> getStat() {
        return ResponseEntity.ok(statisticService.getStatistics());
    }
}
