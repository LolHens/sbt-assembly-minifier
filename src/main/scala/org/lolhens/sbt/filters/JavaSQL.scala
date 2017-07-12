package org.lolhens.sbt.filters

import org.lolhens.sbt.Exclusion.{Class, Subclasses}
import org.lolhens.sbt.ExclusionFilter

/**
  * Created by pierr on 12.07.2017.
  */
object JavaSQL {
  val filter = ExclusionFilter(
    Subclasses(Class("java.sql.Driver", clazz = false))
  )
}
