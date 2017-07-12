package org.lolhens.sbt.filters

import org.lolhens.sbt.Exclusion._
import org.lolhens.sbt.ExclusionFilter

/**
  * Created by pierr on 12.07.2017.
  */
object MySQL {
  val filter = ExclusionFilter.Automatic("mysql", "mysql-connector-java")(
    Package("com.mysql.cj.core"),
    Package("com.mysql.cj.api"),
    Collect(JavaSQL.filter.exclusions)
  )
}
