package io.phdata.spark.snowflake.poc.conf

import pureconfig._
import pureconfig.generic.auto._

trait Configuration {

  val applicationConf = ConfigSource.default.load[Application] match {
    case Left(err) => throw new RuntimeException(err.prettyPrint())
    case Right(conf) => conf
  }

}
