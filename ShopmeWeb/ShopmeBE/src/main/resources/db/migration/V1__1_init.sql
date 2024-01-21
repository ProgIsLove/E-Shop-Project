create table if not exists users
(
    id         int auto_increment
        primary key,
    email      varchar(128) not null,
    enabled    bit          not null,
    first_name varchar(45)  not null,
    last_name  varchar(45)  not null,
    password   varchar(64)  not null,
    photos     varchar(64)  null,
    constraint UK_6dotkott2kjsp8vw4d0m25fb7
        unique (email)
);