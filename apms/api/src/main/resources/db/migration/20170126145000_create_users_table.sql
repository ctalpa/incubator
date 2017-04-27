--liquibase formatted sql
create table users (
    id         serial primary key,
    login      varchar(50)  not null unique,
    email      varchar(255) not null unique,
    password   varchar(60)  not null,
    name       varchar(100) not null,
    created_at timestamp    not null default now(),
    created_by varchar(50)  not null,
    updated_at timestamp,
    updated_by varchar(50)
);