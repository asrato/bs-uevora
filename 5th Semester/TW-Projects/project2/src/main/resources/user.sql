CREATE TABLE userinfo
(
    user_name    varchar(30)  NOT NULL,
    user_pass    varchar(255) NOT NULL,
    nome_proprio varchar(255) NOT NULL,
    gender       char(1)      NOT NULL,
    tier         varchar(10)  NOT NULL,
    enable       smallint     NOT NULL DEFAULT 1,
    PRIMARY KEY (user_name)
);

CREATE TABLE user_role
(
    user_name varchar(30) NOT NULL,
    user_role varchar(15) NOT NULL,
    FOREIGN KEY (user_name) REFERENCES userinfo (user_name)
);

CREATE TABLE events
(
    event_name  varchar(30)  NOT NULL,
    description varchar(255) NOT NULL,
    event_date  date         NOT NULL,
    value       real         NOT NULL DEFAULT 0,
    PRIMARY KEY (event_name)
);

CREATE TABLE inscricoes
(
    participant_name varchar(30) NOT NULL,
    event_name       varchar(30) NOT NULL,
    dorsal           int         NOT NULL,
    PRIMARY KEY (event_name, participant_name),
    FOREIGN KEY (event_name) REFERENCES events (event_name)
);

CREATE TABLE refs
(
    username   varchar(30) NOT NULL,
    entidade   int         NOT NULL,
    referencia int         NOT NULL,
    valor      varchar(20) NOT NULL,
    event_name varchar(30) NOT NULL,
    PRIMARY KEY (entidade, referencia),
    FOREIGN KEY (username) REFERENCES userinfo (user_name),
    FOREIGN KEY (event_name) REFERENCES events (event_name)
);

CREATE TABLE paid
(
    username   varchar(30) NOT NULL,
    event_name varchar(30) NOT NULL,
    PRIMARY KEY (username, event_name),
    FOREIGN KEY (username) REFERENCES userinfo (user_name),
    FOREIGN KEY (event_name) REFERENCES events (event_name)
);

CREATE TABLE times
(
    event_name   varchar(30) NOT NULL,
    dorsal       int         NOT NULL,
    sectionID    varchar(8)  NOT NULL,
    elapsed_time timestamp   NOT NULL,
    PRIMARY KEY (dorsal, event_name, sectionID),
    FOREIGN KEY (event_name) REFERENCES events (event_name)
);


-- Delete all database content

DO
$$
    DECLARE
        r RECORD;
    BEGIN
        -- if the schema you operate on is not "current", you will want to
        -- replace current_schema() in query with 'schematodeletetablesfrom'
        -- *and* update the generate 'DROP...' accordingly.
        FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema())
            LOOP
                EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
            END LOOP;
    END
$$;


-- Give admin permissions to user

UPDATE user_role
set user_role='ROLE_STAFF'
WHERE user_name = 'rato';

