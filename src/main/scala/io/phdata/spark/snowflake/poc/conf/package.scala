package io.phdata.spark.snowflake.poc

package object conf {

  case class Application(name: String,
                         master: String,
                         file: File,
                         sfOptions: Map[String, String],
                         kafkaOptions: Map[String, String])

  case class File(location: String)

}
