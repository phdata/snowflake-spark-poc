-- Setup database and schema for demo
USE ROLE SYSADMIN;

CREATE DATABASE IF NOT EXISTS SPARK_DEMO;
CREATE SCHEMA IF NOT EXISTS SPARK_DEMO.EMPLOYEES;
CREATE SCHEMA IF NOT EXISTS SPARK_DEMO.JSON;
CREATE SCHEMA IF NOT EXISTS SPARK_DEMO.KAFKA;

CREATE WAREHOUSE IF NOT EXISTS SPARK_DEMO_WH
    WITH
    WAREHOUSE_SIZE = 'XSMALL';

-- Create roles, user, and issue grants
USE ROLE SECURITYADMIN;

CREATE ROLE IF NOT EXISTS SPARK_DEMO_FR;

GRANT ALL ON DATABASE SPARK_DEMO TO ROLE SPARK_DEMO_FR;
GRANT ALL ON SCHEMA SPARK_DEMO.EMPLOYEES TO ROLE SPARK_DEMO_FR;
GRANT ALL ON SCHEMA SPARK_DEMO.JSON TO ROLE SPARK_DEMO_FR;
GRANT ALL ON SCHEMA SPARK_DEMO.KAFKA TO ROLE SPARK_DEMO_FR;

CREATE USER IF NOT EXISTS SPARK_DEMO_USER
DISPLAY_NAME = SPARK_DEMO_USER
PASSWORD = '<password>'
EMAIL = '<email>'
RSA_PUBLIC_KEY = '<public key content>';

GRANT USAGE ON WAREHOUSE SPARK_DEMO_WH TO ROLE SPARK_DEMO_FR;
GRANT ROLE SPARK_DEMO_FR TO USER SPARK_DEMO_USER;

ALTER USER SPARK_DEMO_USER SET DEFAULT_ROLE = SPARK_DEMO_FR;
ALTER USER SPARK_DEMO_USER SET DEFAULT_NAMESPACE = SPARK_DEMO;
ALTER USER SPARK_DEMO_USER SET DEFAULT_WAREHOUSE = SPARK_DEMO_WH;
