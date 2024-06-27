INSERT INTO 
    locations (code, city_name, country_code, country_name, enabled, region_name, trashed)
SELECT 'DELHI_IN', 'New Delhi', 'IN', 'India', 1, 'Delhi', 0 FROM dual
UNION ALL
SELECT 'LACA_US', 'Los Angeles', 'US', 'United State of America', 1, 'California', 0 FROM dual
UNION ALL
SELECT 'NYC_USA', 'New York City', 'US', 'United State of America', 1, 'New York', 0 FROM dual;

SELECT * FROM locations;