package org.lolhens.sbt.filters

import org.lolhens.sbt.Exclusion.Class
import org.lolhens.sbt.ExclusionFilter

/**
  * Created by pierr on 12.07.2017.
  */
object Scala_2_12 {
  val filter = ExclusionFilter.Automatic("org.scala-lang", "scala-library", "2\\.\\12(?:\\..+$)")(
    Class("scala.Symbol", interface = false)
  )
}
