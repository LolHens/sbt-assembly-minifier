package org.lolhens.sbt.filters

import org.lolhens.sbt.Filter
import org.lolhens.sbt.Rule.Class

/**
  * Created by pierr on 12.07.2017.
  */
object Scala_2_12 {
  val filter: Filter = Filter.Module("org.scala-lang", "scala-library", "2\\.12(?:\\..+)?$")(
    Class("scala.Symbol", interface = false)
  )
}
