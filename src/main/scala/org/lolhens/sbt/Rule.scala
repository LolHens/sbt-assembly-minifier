package org.lolhens.sbt

/**
  * Created by pierr on 12.07.2017.
  */
abstract class Rule {
  def config: Seq[String]
}

object Rule {

  case class Class(name: String,
                   clazz: Boolean = true,
                   interface: Boolean = true) extends Rule {
    override lazy val config: Seq[String] = Seq(
      Some(s"-keep class $name {*;}").filter(_ => clazz),
      Some(s"-keep interface $name {*;}").filter(_ => interface)
    ).flatten
  }

  case class Subclasses(clazz: Class) extends Rule {
    override lazy val config: Seq[String] = {
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
    override def config: Seq[String] = Class(
      s"$name.*${if (recursive) "*" else ""}",
      clazz = clazz,
      interface = interface
    ).config
  }

  case class Collect(exclusion: Seq[Rule]) extends Rule {
    override def config: Seq[String] = exclusion.flatMap(_.config)
  }

}
