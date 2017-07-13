package org.lolhens.sbt.assemblyminifier.filters

import org.lolhens.sbt.assemblyminifier.Filter
import org.lolhens.sbt.assemblyminifier.Rule.Class

/**
  * Created by pierr on 12.07.2017.
  */
object Scala_2_12 {
  val filter: Filter = Filter.Module("org.scala-lang", "scala-library", "2\\.12(?:\\..+)?$")(
    Class("scala.Symbol", interface = false)
  )
}
