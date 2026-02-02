package com.egrevs.project.lanittest.controller;

import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.dto.PersonWithCarsResponse;
import com.egrevs.project.lanittest.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @Operation(summary = "Создать пользователя")
    public ResponseEntity<Void> createPerson(@RequestBody @Valid CreatePersonRequest personRequest) {
        personService.createPerson(personRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Получить пользователей, у которых есть автомобили")
    public ResponseEntity<PersonWithCarsResponse> getPeopleWithCars(@RequestParam(name = "personId") Long personId) {
        return ResponseEntity.ok(personService.getPeopleWithCars(personId));
    }
}
