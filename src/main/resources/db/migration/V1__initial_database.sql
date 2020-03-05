DO $$
BEGIN
    CREATE ROLE example WITH NOLOGIN;
    EXCEPTION WHEN OTHERS THEN
      RAISE NOTICE 'not creating role example -- it already exists';
END
$$;

CREATE SEQUENCE user_id_seq;

alter sequence user_id_seq owner to example;

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

create table if not exists "user"
(
    id         integer   default nextval('user_id_seq') not null
        constraint user_pkey
            primary key,
    email      varchar(200),
    password   varchar(200),
    createdts  timestamp default CURRENT_TIMESTAMP                not null,
    modifiedts timestamp default CURRENT_TIMESTAMP                not null
);

alter table "user"
    owner to example;
