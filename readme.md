# Snowflake Spark Examples
The purpose of this repository is to demonstrate using the [Snowflake Spark Connector](https://docs.snowflake.com/en/user-guide/spark-connector.html).

## Requirements
To execute the examples provided in this repository the user must first have a Snowflake account.  Snowflake provides a free 30 day or $400 account [here](https://signup.snowflake.com/) if one is not available.

[Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) are used to create a [Confluent Kafka](https://www.confluent.io/) environment to demonstrate Spark Streaming capabilities with Snowflake. 

[IntelliJ Community Edition](https://www.jetbrains.com/idea/download)

### Snowflake Account
After getting access to a Snowflake environment a [User](https://docs.snowflake.com/en/user-guide/admin-user-management.html) account will need to be created.  This user will be used in the Spark application to interface with Snowflake.  Generally a service or application account should be created.

In order to execute the [snowflake-setup.sql](src/main/resources/snowflake-setup.sql) the provisioned user must have `SECURITYADMIN` and `SYSADMIN` roles.  If the user does not have these roles ask that a system administrator executes the script. 

#### Setup
1. Following the instructions [here](https://docs.snowflake.com/en/user-guide/snowsql-install-config.html) install and [configure](https://docs.snowflake.com/en/user-guide/snowsql-config.html) the SnowSQL cli tool.  The CLI tool will be used for executing environment setup scripts.
2. Generate public and private key
    1. Navigate to the `scripts/` directory `cd <repo home>/scripts`
    2. Generate keys `./generate_private_key.sh`
    3. Verify the public (private-rsa-key.pub) and private keys (private-rsa-key.pem) are written to `<repo home>/src/main/resources`
3. Modify [snowflake-setup.sql](src/main/resources/snowflake-setup.sql) script and replace the `<place holders>` (password, email, public key contents)
4. Execute the environment setup script
    1. Navigate to the `src/main/resources` directory `cd <repo home>/src/main/resources`
    2. Execute `snowsql -c <adminstrator connection> -f snowflake-setup.sql`
    3. Add new connection [configuration](https://docs.snowflake.com/en/user-guide/snowsql-config.html) for the newly created Snowflake user
5. Load example datasets into Snowflake
    1. Navigate to the `src/main/resources` directory `cd <repo home>/src/main/resources`
    2. Modify the `<path>` place holder to reference this repository's directory path 
    3. Execute `snowsql -c <service account connection> -f snowflake-load.sql`
    4. Log into Snowflake and verify the results in the `SPARK_DEMO.EMPLOYEES` database and schema

# Environment Setup
Import the `snowflake-spark-poc` repository into a new [Intellij project](https://www.jetbrains.com/help/idea/sbt-support.html).

# Use Cases
This repo will demonstrate three different ways of reading and loading data into Snowflake using Spark.

- [Reading data in Snowflake](#reading-data-in-snowflake)
- [Writing data to Snowflake](#writing-data-to-snowflake)
- [Kafka Structured Streaming to Snowflake](#kafka-structured-streaming-to-snowflake)

## Reading Data in Snowflake
This tutorial will demonstrate using the Snowflake Spark Connector to read a table in Snowflake, also provide a custom query to Snowflake and load the results into a DataFrame.

### Steps
1. Modify the application.conf
    1. Navigate to `<repo home>/src/main/resources`
    2. Replace any `<place holders>` in the `application.conf` file.  NOTE: Do not commit these changes back to git.
        
        If the `snowflake-setup.sql` was used the default values are as follows:
        
        - sfUser = "SPARK_DEMO_USER"
        - sfDatabase = "SPARK_DEMO"
        - sfSchema = "EMPLOYEES"
        - sfWarehouse = "SPARK_DEMO_WH"
        - sfRole = "SPARK_DEMO_FR"
        - dbtable = "EMPLOYEES"
        
2. Open the `BatchReadSnowflake` Spark application in Intellij
3. Add a Run Configuration
    1. Click `Play` icon next to Line 12
    2. Observe the output in the run window
    
#### Extra Credit
1. Modify the application.conf to execute a custom query instead of reading just a single table.
    1. Replace the `dbtable` property with a custom query
    
    ex. query = "SELECT emp_no, MAX(from_date) as from_date, MAX(to_date) AS to_date FROM dept_emp GROUP BY emp_no"
2. Rerun application

## Writing data to Snowflake
This tutorial will demonstrate using Spark to read in a JSON dataset, parse it into a flattened table, and write the results to Snowflake.

### Steps
1. Modify the application.conf
    1. Navigate to `<repo home>/src/main/resources`
    3. Replace any `<place holders>` in the `application.conf` file.  NOTE: Do not commit these changes back to git.
        
        If the `snowflake-setup.sql` was used the default values are as follows:
        
        - sfUser = "SPARK_DEMO_USER"
        - sfDatabase = "SPARK_DEMO"
        - sfSchema = "JSON"
        - sfWarehouse = "SPARK_DEMO_WH"
        - sfRole = "SPARK_DEMO_FR"
        - dbtable = "BLOG_POSTS"

2. Open the `BatchWriteSnowflake` Spark application in Intellij
3. Add a Run Configuration
    1. Click `Play` icon next to Line 13
    2. Observe the output in the run window

## Kafka Structured Streaming to Snowflake
This tutorial will demonstrate how Kafka can be used with the Snowflake Spark Connector to stream data into Snowflake.  For this demo Docker and Docker Compose will need to be installed and running.

### Setup
1. Setup Confluent Kafka and Streamsets environment
    1. Navigate to `<repo home>/src/main/resources`
    2. Create Docker environment `docker-compose create`
    3. Spin up services `docker-compose up`
    4. Wait for the services to be available
2. Add Kafka Library to Streamsets
    1. Login to Streamsets from a browser [http://localhost:18630](http://localhost:18630)
    2. User = admin, password = admin
    3. Click on the "Package Manager Icon" in the upper right corner
    4. Select the checkbox next to `Apache Kafka 1.1.0`
    5. Install library
    6. Restart Streamsets `docker restart streamsets`
3. Import Streamsets kafka pipeline
    1. Log back into Streamsets
    2. Select Import Pipeline
    3. Select browse
        1. Pipeline json can be found in `src/main/resources/streamsets-pipeline.json`
    4. Click start
        1. 10 Demo records will be written to Kafka every 5 seconds

### Steps
1. Modify the application.conf
    1. Navigate to `<repo home>/src/main/resources`
    3. Replace any `<place holders>` in the `application.conf` file.  NOTE: Do not commit these changes back to git.
        
        If the `snowflake-setup.sql` was used the default values are as follows:
        
        - sfUser = "SPARK_DEMO_USER"
        - sfDatabase = "SPARK_DEMO"
        - sfSchema = "KAFKA"
        - sfWarehouse = "SPARK_DEMO_WH"
        - sfRole = "SPARK_DEMO_FR"
        - dbtable = "KAFKA_STREAM"

2. Open the `StreamingJsonSnowflake` Spark application in Intellij
3. Add a Run Configuration
    1. Click `Play` icon next to Line 16
    2. Observe the output in the run window
    
#### Extra Credit
Use the Snowflake Kafka Connector to stream Kafka records into Snowflake.

1. Configure Kafka Connector
    1. Navigate to `<repo home>/src/main/resources`
    2. Make a copy of the `kafka-connect.json` `cp kafka-connect.json private-kafka-connect.json`
    3. Modify `private-kafka-connect.json`
      
        If the `snowflake-setup.sql` was used the default values are as follows:
                
        - tasks.max = "4"
        - topics = "demo-kafka-json"
        - snowflake.private.key = "contents of the private-rsa-key.pem file"
        - snowflake.database.name = "SPARK_DEMO"
        - snowflake.schema.name = "KAFKA"
2. Run the connector
    1. Navigate to `<repo home>/scripts`
    2. Execute `./start_kafka_connect_snowflake.sh`
3. After some time observe records being written into `SPARK_DEMO.KAFKA` on Snowflake.
4. Stop the connector after testing
    1. Navigate to `<repo home>/scripts`
    2. Execute `./stop_kafka_connect_snowflake.sh`
    
# Environment tear down
1. Navigate to `<repo home>/src/main/resources`
2. Execute `docker-compose stop`
3. Execute `docker-compose rm`