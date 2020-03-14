DO
$$
    BEGIN
        CREATE ROLE example WITH NOLOGIN;
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'not creating role example -- it already exists';
    END
$$;

create table if not exists flyway_schema_history
(
    installed_rank integer                 not null
        constraint flyway_schema_history_pk
            primary key,
    version        varchar(50),
    description    varchar(200)            not null,
    type           varchar(20)             not null,
    script         varchar(1000)           not null,
    checksum       integer,
    installed_by   varchar(100)            not null,
    installed_on   timestamp default now() not null,
    execution_time integer                 not null,
    success        boolean                 not null
);

alter table flyway_schema_history
    owner to example;

create index if not exists flyway_schema_history_s_idx
    on flyway_schema_history (success);

create table if not exists "item"
(
    name        varchar(200)                        not null
        constraint item_pkey primary key,
    description varchar(500)                        not null,
    createdts   timestamp default CURRENT_TIMESTAMP not null
);

alter table "item"
    owner to example;
