package io.phdata.spark.snowflake.poc

import net.snowflake.spark.snowflake.Utils.SNOWFLAKE_SOURCE_NAME
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{DataType, DataTypes, StructType}

/**
 * Basic application demonstrating how the Snowflake Spark Connector can be used with Structured Streaming to
 * consume Kafka messages and write the results to Snowflake.
 *
 * Snowflake configurations are read in from an application.conf file
 *
 * Use -Dconfig.file as a program argument to manually specify an alternative application.conf location.
 */
object StreamingJsonSnowflake extends Spark {

  def main(args: Array[String]): Unit = {

    import org.apache.spark.sql.functions._

    val schema = new StructType()
      .add("id", DataTypes.StringType)
      .add("date_time", DataTypes.StringType)
      .add("number", DataTypes.DoubleType)

    val stream = spark.readStream
      .format("kafka")
      .options(applicationConf.kafkaOptions)
      .load()

    stream.select(
      col("key").cast(DataTypes.StringType),
      from_json(
        col("value").cast(DataTypes.StringType), schema).as("VALUE"),
      col("topic"),
      col("offset"),
      col("timestamp"))
      .writeStream
      .foreachBatch { (df: DataFrame, id: Long) =>
        df
          .write
          .format(SNOWFLAKE_SOURCE_NAME)
          .options(applicationConf.sfOptions)
          .mode(SaveMode.Append)
          .save()
      }
      .outputMode(OutputMode.Append())
      .start()

    spark.streams.awaitAnyTermination()
  }

}
