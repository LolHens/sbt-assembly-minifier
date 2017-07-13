package org.lolhens.sbt.assemblyminifier.filters

import org.lolhens.sbt.assemblyminifier.Filter
import org.lolhens.sbt.assemblyminifier.Rule.{Class, Subclasses}

/**
  * Created by pierr on 12.07.2017.
  */
object JavaSQL {
  val filter: Filter = Filter(
    Subclasses(Class("java.sql.Driver", clazz = false))
  )
}
