USE ROLE SPARK_DEMO_FR;
USE DATABASE SPARK_DEMO;
USE SCHEMA EMPLOYEES;

CREATE TABLE IF NOT EXISTS employees (
    EMP_NO NUMBER,
    BIRTH_DATE DATE,
    FIRST_NAME STRING,
    LAST_NAME STRING,
    GENDER STRING,
    HIRE_DATE DATE
);

CREATE TABLE IF NOT EXISTS dept_emp (
    EMP_NO NUMBER,
    DEPT_NO STRING,
    FROM_DATE DATE,
    TO_DATE DATE
);

CREATE TABLE IF NOT EXISTS departments (
    DEPT_NO STRING,
    DEPT_NAME STRING
);

CREATE STAGE IF NOT EXISTS employees_stg
FILE_FORMAT = (
    TYPE = CSV
    SKIP_HEADER = 1 );

PUT file:///<path>/snowflake-spark-poc/data/csv/employees/employees.csv @employees_stg/employees/employees.csv auto_compress = true;
PUT file:///<path>/snowflake-spark-poc/data/csv/departments/departments.csv @employees_stg/departments/departments.csv auto_compress = true;
PUT file:///<path>/snowflake-spark-poc/data/csv/dept_emp/dept_emp.csv @employees_stg/dept_emp/dept_emp.csvc auto_compress = true;

COPY INTO employees FROM @employees_stg/employees;
COPY INTO departments FROM @employees_stg/departments;
COPY INTO dept_emp FROM @employees_stg/dept_emp;