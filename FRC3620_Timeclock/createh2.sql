create schema if not exists SA;

create cached table if not exists SA.PERSONS (
    PERSON_ID integer auto_increment primary key,
    LASTNAME varchar(255),
    FIRSTNAME varchar(255),
    MENTOR boolean not null default 0,
);

create cached table if not exists SA.WORKSESSIONS (
    WORKSESSION_ID integer auto_increment primary key,
    PERSON_ID integer not null,
    START_DATE timestamp not null,
    END_DATE timestamp,
    REMOVED boolean not null default 0,
    REMOVED_BY integer,
    ORIGINAL_START_DATE timestamp not null,
    ORIGINAL_END_DATE timestamp,
);

create index if not exists WORKSESSIONS_PERSON_ID_IDX on SA.WORKSESSIONS (PERSON_ID);

create cached table if not exists SA.CORRECTIONS (
    CORRECTION_ID integer auto_increment primary key,
    WORKSESSION_ID integer not null,
    START_OR_END varchar(6) not null,
    NEW_DATE timestamp,
    OLD_DATE timestamp,
    CORRECTION_DATE timestamp not null,
    CORRECTED_BY integer not null,
);
