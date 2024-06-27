--------------------------------------------------------
--  File created - Thu-Jun-27-2024
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table LOCATIONS
--------------------------------------------------------

CREATE TABLE `HAN_SPRING_REST`.`LOCATIONS` (
    `CODE` VARCHAR(10),
    `CITY_NAME` VARCHAR(128),
    `COUNTRY_CODE` VARCHAR(2),
    `COUNTRY_NAME` VARCHAR(64),
    `ENABLED` TINYINT(1),
    `REGION_NAME` VARCHAR(128),
    `TRASHED` TINYINT(1),
    PRIMARY KEY (`CODE`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--------------------------------------------------------
--  DDL for Table WEATHER_REALTIME
--------------------------------------------------------

CREATE TABLE `HAN_SPRING_REST`.`WEATHER_REALTIME` (
    `LOCATION_CODE` VARCHAR(10),
    `HUMIDITY` INT,
    `LAST_UPDATED` TIMESTAMP,
    `PRECIPITATION` INT,
    `STATUS` VARCHAR(50),
    `TEMPERATURE` INT,
    `WIND_SPEED` INT,
     PRIMARY KEY (`LOCATION_CODE`),
     FOREIGN KEY (`LOCATION_CODE`) REFERENCES `HAN_SPRING_REST`.`LOCATIONS`(`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--------------------------------------------------------
--  Insert data into LOCATIONS
--------------------------------------------------------

INSERT INTO `HAN_SPRING_REST`.`LOCATIONS`
(`CODE`, `CITY_NAME`, `COUNTRY_CODE`, `COUNTRY_NAME`, `ENABLED`, `REGION_NAME`, `TRASHED`)
VALUES
    ('DELHI_IN', 'Delhi', 'IN', 'India', 1, 'Delhi', 0),
('LACA_US', 'Los Angeles', 'US', 'United State of America', 1, 'California', 0),
('NYC_USA', 'New York City', 'US', 'United State of America', 1, 'New York', 0);

--------------------------------------------------------
--  Insert data into WEATHER_REALTIME
--------------------------------------------------------

INSERT INTO `HAN_SPRING_REST`.`WEATHER_REALTIME`
(`LOCATION_CODE`, `HUMIDITY`, `LAST_UPDATED`, `PRECIPITATION`, `STATUS`, `TEMPERATURE`, `WIND_SPEED`)
VALUES
    ('NYC_USA', 20, STR_TO_DATE('27-JUN-24 02.11.10.554790000 PM', '%d-%b-%y %h.%i.%s.%f %p'), 3, 'Sunny', 30, 2),
('DELHI_IN', 200, STR_TO_DATE('27-JUN-24 02.21.02.453298000 PM', '%d-%b-%y %h.%i.%s.%f %p'), 7, 'Rainy', 60, 5);
