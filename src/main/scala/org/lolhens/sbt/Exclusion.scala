package org.lolhens.sbt

/**
  * Created by pierr on 12.07.2017.
  */
abstract class Exclusion {
  def rules: Seq[String]
}

object Exclusion {

  case class Class(name: String,
                   clazz: Boolean = true,
                   interface: Boolean = true) extends Exclusion {
    override lazy val rules: Seq[String] = Seq(
      Some(s"-keep class $name {*;}").filter(_ => clazz),
      Some(s"-keep interface $name {*;}").filter(_ => interface)
    ).flatten
  }

  case class Subclasses(clazz: Class) extends Exclusion {
    override lazy val rules: Seq[String] = {
      if (clazz.clazz) Class(s"* extends ${clazz.name}").rules
      else Nil
    } ++ {
      if (clazz.interface) Class(s"* implements ${clazz.name}", interface = false).rules
      else Nil
    }
  }

  case class Package(name: String,
                     recursive: Boolean = true,
                     clazz: Boolean = true,
                     interface: Boolean = true) extends Exclusion {
    override def rules: Seq[String] = Class(
      s"$name.*${if (recursive) "*" else ""}",
      clazz = clazz,
      interface = interface
    ).rules
  }

  case class Collect(exclusion: Seq[Exclusion]) extends Exclusion {
    override def rules: Seq[String] = exclusion.flatMap(_.rules)
  }

}
