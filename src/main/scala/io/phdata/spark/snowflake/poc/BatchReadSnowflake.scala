package io.phdata.spark.snowflake.poc

import net.snowflake.spark.snowflake.Utils.SNOWFLAKE_SOURCE_NAME

/**
 * Basic application demonstrating how the Snowflake Spark Connector can be used to read data from Snowflake.
 *
 * Snowflake configurations are read in from an application.conf file
 *
 * Use -Dconfig.file as a program argument to manually specify an alternative application.conf location.
 */
object BatchReadSnowflake extends Spark {

  def main(args: Array[String]): Unit = {
    val df = spark.read
      .format(SNOWFLAKE_SOURCE_NAME)
      .options(applicationConf.sfOptions)
      .load()

    df.show()

  }

}
