package org.lolhens.sbt.assemblyminifier

import org.lolhens.sbt.assemblyminifier.Sbt10Compat.ModuleID

/**
  * Created by pierr on 12.07.2017.
  */
class Filter(val exclusions: Rule*) {
  def config(modules: Seq[ModuleID]): Seq[Config] = Rule.Collect(exclusions).config
}

object Filter {
  def apply(exclusions: Rule*): Filter = new Filter(exclusions: _*)

  case class Module(moduleFilter: ((String, String, String)) => Boolean)
                   (exclusions: Rule*) extends Filter(exclusions: _*) {
    override def config(modules: Seq[ModuleID]): Seq[Config] =
      if (modules.exists { module =>
        moduleFilter((module.organization, module.name, module.revision))
      }) super.config(modules)
      else Nil
  }

  object Module {
    def apply(organization: String,
              name: String,
              versionPattern: String = ".*")
             (exclusions: Rule*): Module = {
      val Pattern = versionPattern.r

      Module {
        case (`organization`, `name`, version@Pattern()) => true
        case _ => false
      }(exclusions: _*)
    }
  }

}
