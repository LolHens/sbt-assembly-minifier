package org.lolhens.sbt.assemblyminifier

/**
  * Created by pierr on 13.07.2017.
  */
case class Config(name: String, params: String*) extends Rule {
  override def toString: String = s"-${(name +: params).mkString(" ")}"

  override def config: Seq[Config] = Seq(this)
}
