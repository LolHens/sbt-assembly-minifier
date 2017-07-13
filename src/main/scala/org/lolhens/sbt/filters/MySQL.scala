package org.lolhens.sbt.filters

import org.lolhens.sbt.Filter
import org.lolhens.sbt.Rule._

/**
  * Created by pierr on 12.07.2017.
  */
object MySQL {
  val filter: Filter = Filter.Module("mysql", "mysql-connector-java")(
    Package("com.mysql.cj.core"),
    Package("com.mysql.cj.api"),
    Collect(JavaSQL.filter.exclusions)
  )
}
