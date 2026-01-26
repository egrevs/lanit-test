package com.egrevs.project.lanittest.controller;

import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.dto.PersonWithCarsResponse;
import com.egrevs.project.lanittest.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Поменять название api
@RestController
@RequestMapping("api/people")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<Void> createPerson(@RequestBody @Valid CreatePersonRequest personRequest){
        personService.createPerson(personRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PersonWithCarsResponse> getPeopleWithCars(@RequestParam(name = "personId") Long personId){
        return ResponseEntity.ok(personService.getPeopleWithCars(personId));
    }
}
