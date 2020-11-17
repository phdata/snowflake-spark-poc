package io.phdata.spark.snowflake.poc

import io.phdata.spark.snowflake.poc.conf.Configuration
import org.apache.spark.sql.SparkSession

trait Spark extends Configuration {

  val spark = SparkSession.builder()
    .master(applicationConf.master)
    .appName(applicationConf.name)
    .getOrCreate()

}
