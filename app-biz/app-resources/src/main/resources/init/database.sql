create schema if not exists resources;

create table if not exists resources_device
(
    id            bigint auto_increment
        primary key,
    uuid          varchar(100)         not null,
    name          varchar(100)         not null,
    ip            varchar(100)         not null,
    port          int                  not null,
    username      varchar(50)          null,
    password      varchar(255)         null,
    type          int                  not null,
    enable        tinyint(1) default 1 not null,
    extra_info    json                 null,
    modified_time datetime             not null,
    create_time   datetime             not null,
    version       bigint               not null,
    vendor        varchar(255)         not null,
    constraint uk_ip
        unique (ip),
    constraint uk_uuid
        unique (uuid)
);

create table if not exists resources_person
(
    id            bigint auto_increment
        primary key,
    uid           varchar(100)      not null,
    username      varchar(255)      not null,
    password      varchar(255)      not null,
    name          varchar(255)      not null,
    id_card       varchar(100)      not null,
    age           int               null,
    enable        tinyint default 1 null,
    deleted       tinyint default 0 null,
    create_time   datetime          not null,
    modified_time datetime          not null,
    constraint uk_id_card
        unique (id_card),
    constraint uk_uid
        unique (uid),
    constraint uk_username
        unique (username)
)
    comment '人员表';

