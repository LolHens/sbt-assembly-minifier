package org.lolhens.sbt.filters

import org.lolhens.sbt.Filter
import org.lolhens.sbt.Rule.{Class, Subclasses}

/**
  * Created by pierr on 12.07.2017.
  */
object JavaSQL {
  val filter: Filter = Filter(
    Subclasses(Class("java.sql.Driver", clazz = false))
  )
}
