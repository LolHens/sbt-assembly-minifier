package org.lolhens.sbt

import sbt.ModuleID

/**
  * Created by pierr on 12.07.2017.
  */
class ExclusionFilter(val exclusions: Exclusion*) {
  def rules(modules: Seq[ModuleID]): Seq[String] = Exclusion.Collect(exclusions).rules
}

object ExclusionFilter {
  def apply(exclusions: Exclusion*): ExclusionFilter = new ExclusionFilter(exclusions: _*)

  case class Automatic(organization: String, name: String, versionPattern: String = ".*")
                      (exclusions: Exclusion*) extends ExclusionFilter(exclusions: _*) {
    private val Pattern = versionPattern.r

    override def rules(modules: Seq[ModuleID]): Seq[String] =
      if (modules.exists {
        case ModuleID(`organization`, `name`, version@Pattern(), _, _, _, _, _, _, _, _) =>
          println(s"included: $organization $name $version")
          true
        case _ => false
      }) super.rules(modules)
      else Nil
  }

}
