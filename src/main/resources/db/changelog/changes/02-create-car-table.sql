-- liquibase formatted sql

--changeset egrevs:20260202-create-cars-table
create table cars(
    id bigint primary key ,
    vendor varchar (20) not null ,
    model varchar (20) not null ,
    horsepower int not null ,
    owner_id bigint not null ,
    constraint fk_car_person foreign key (owner_id) references person(id)
);