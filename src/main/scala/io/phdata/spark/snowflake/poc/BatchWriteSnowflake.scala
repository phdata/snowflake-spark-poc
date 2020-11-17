package io.phdata.spark.snowflake.poc

import net.snowflake.spark.snowflake.Utils.SNOWFLAKE_SOURCE_NAME
import org.apache.spark.sql.SaveMode

/**
 * Basic application demonstrating how the Snowflake Spark Connector can be used to read and write data with Snowflake.
 *
 * Snowflake configurations are read in from an application.conf file
 *
 * Use -Dconfig.file as a program argument to manually specify an alternative application.conf location.
 */
object BatchWriteSnowflake extends Spark {

  def main(args: Array[String]): Unit = {

    import org.apache.spark.sql.functions._
    val df = spark.read.json(applicationConf.file.location)


    val resultsDf = df.select(
      explode(col("results"))
    )
      .select(
        col("col").getItem("created").as("created"),
        col("col").getItem("date").as("create_date"),
        col("col").getItem("changed").as("changed_date"),
        col("col").getItem("title").as("title"),
        col("col").getItem("body").as("body"),
        col("col").getItem("url").as("url")
      )

    resultsDf.show(10)

    resultsDf.write
      .format(SNOWFLAKE_SOURCE_NAME)
      .options(applicationConf.sfOptions)
      .mode(SaveMode.Overwrite)
      .save()

  }

}
