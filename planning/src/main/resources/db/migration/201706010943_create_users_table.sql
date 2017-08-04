--liquibase formatted sql


create table users (
    id                  serial primary key,
    login               varchar(50)  not null unique,
    name                varchar(100) ,
    surname             varchar(100),
    email               varchar(100) not null unique,
    password            varchar(60)  not null,
    phone               varchar(100),
    contact_information varchar(255),
    created_at          timestamp    not null default now(),
    created_by          varchar(50)  not null,
    updated_at          timestamp,
    updated_by          varchar(50)
);
