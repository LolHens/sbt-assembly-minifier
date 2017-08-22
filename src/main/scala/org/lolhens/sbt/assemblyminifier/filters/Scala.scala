package org.lolhens.sbt.assemblyminifier.filters

import org.lolhens.sbt.assemblyminifier.Rule.Class
import org.lolhens.sbt.assemblyminifier.{Config, Filter}

/**
  * Created by pierr on 13.07.2017.
  */
object Scala {
  val filter: Filter = Filter.Module("org.scala-lang", "scala-library")(
    Config("keepattributes", "Signature, *Annotation*"),
    Config("keepclassmembers", "class", "*", "{** MODULE$;}"),
    Class("scala.Symbol", interface = false)
  )
}
