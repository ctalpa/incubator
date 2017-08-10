create table roles (
    id   serial primary key,
    name varchar(25) not null unique,
    created_at          timestamp    not null default now(),
    created_by          varchar(50)  not null,
    updated_at          timestamp,
    updated_by          varchar(50)
);

create table permissions (
    id   serial primary key,
    name varchar(100) not null unique,
    created_at          timestamp    not null default now(),
    created_by          varchar(50)  not null,
    updated_at          timestamp,
    updated_by          varchar(50)
);

create table role_permission (
    id            serial primary key,
    role_id       int       not null references roles (id),
    permission_id int       not null references permissions (id),
    created_at          timestamp    not null default now(),
    created_by          varchar(50)  not null,
    updated_at          timestamp,
    updated_by          varchar(50),
    unique (role_id, permission_id)

);

create table user_role (
    id         serial primary key,
    user_id    int       not null references users (id),
    role_id    int       not null references roles (id),
    created_at          timestamp    not null default now(),
    created_by          varchar(50)  not null,
    updated_at          timestamp,
    updated_by          varchar(50),
    unique (user_id, role_id)
);
