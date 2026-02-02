package com.egrevs.project.lanittest.controller;

import com.egrevs.project.lanittest.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/clear")
public class ClearController {

    private final PersonService personService;

    public ClearController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @Operation(summary = "Очистить базу данных")
    public ResponseEntity<Void> clearAll() {
        personService.clearAll();
        return ResponseEntity.ok().build();
    }
}
