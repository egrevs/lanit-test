package com.egrevs.project.lanittest.controller;

import com.egrevs.project.lanittest.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/clear")
@RequiredArgsConstructor
public class ClearController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<Void> clearAll(){
        personService.clearAll();
        return ResponseEntity.ok().build();
    }
}
