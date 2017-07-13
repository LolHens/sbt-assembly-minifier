package org.lolhens.sbt.assemblyminifier

/**
  * Created by pierr on 12.07.2017.
  */
abstract class Rule {
  def config: Seq[Config]
}

object Rule {

  case class Class(name: String,
                   clazz: Boolean = true,
                   interface: Boolean = true) extends Rule {
    override lazy val config: Seq[Config] = Seq(
      Some(Config("keep", "class", name, "{*;}")).filter(_ => clazz),
      Some(Config("keep", "interface", name, "{*;}")).filter(_ => interface)
    ).flatten
  }

  case class Subclasses(clazz: Class) extends Rule {
    override lazy val config: Seq[Config] = {
      if (clazz.clazz) Class(s"* extends ${clazz.name}").config
      else Nil
    } ++ {
      if (clazz.interface) Class(s"* implements ${clazz.name}", interface = false).config
      else Nil
    }
  }

  case class Package(name: String,
                     recursive: Boolean = true,
                     clazz: Boolean = true,
                     interface: Boolean = true) extends Rule {
    override def config: Seq[Config] = Class(
      s"$name.*${if (recursive) "*" else ""}",
      clazz = clazz,
      interface = interface
    ).config
  }

  case class Collect(exclusion: Seq[Rule]) extends Rule {
    override def config: Seq[Config] = exclusion.flatMap(_.config).distinct
  }

}
