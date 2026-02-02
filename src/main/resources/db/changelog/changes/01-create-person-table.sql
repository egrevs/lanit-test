-- liquibase formatted sql

--changeset egrevs:20260202-create-person-table
create table person (
    id bigint primary key,
    name varchar(255) not null,
    birthday date not null
);