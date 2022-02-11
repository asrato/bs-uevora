CREATE TABLE events
(
    eventName varchar(255),
    type      varchar(255),
    date      date,
        PRIMARY KEY (eventName)
);

CREATE TABLE registrations
(
    participantName varchar(255),
    eventName       varchar(225) REFERENCES events,
    genre           char(1),
    tier            varchar(20),
    dorsal          int,
    chipID          int PRIMARY KEY
);

CREATE TABLE times
(
    eventName  varchar(255) references events,
    chipID     int references registrations,
    locationID varchar(10),
    timestamp  TIMESTAMP,
        PRIMARY KEY (locationID, chipID)
);