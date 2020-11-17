organization := "io.phdata.spark.snowflake.poc"
name := "snowflake-spark-poc"
version := "0.1"
scalaVersion := "2.12.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "3.0.0",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.0.0",
  "net.snowflake" %% "spark-snowflake" % "2.8.2-spark_3.0",
  "com.github.pureconfig" %% "pureconfig" % "0.14.0",
)
