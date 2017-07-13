package org.lolhens.sbt.assemblyminifier.filters

import org.lolhens.sbt.assemblyminifier.{Config, Filter}

/**
  * Created by pierr on 13.07.2017.
  */
object DefaultSettings {
  val filter = Filter(
    Config("dontnote"),
    Config("dontwarn"),
    Config("ignorewarnings"),
    Config("dontobfuscate"),
    Config("dontoptimize")
  )
}
