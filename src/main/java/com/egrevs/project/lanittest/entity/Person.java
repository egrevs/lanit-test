package com.egrevs.project.lanittest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
public class Person {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private LocalDate birthday;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<Car> cars = new ArrayList<>();
}
