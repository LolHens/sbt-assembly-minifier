package org.lolhens.sbt.assemblyminifier

import com.typesafe.sbt.SbtProguard
import com.typesafe.sbt.SbtProguard.ProguardSettings
import org.lolhens.sbt.assemblyminifier.filters._
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin

/**
  * Created by pierr on 12.07.2017.
  */
object AssemblyMinifierPlugin extends AutoPlugin {

  object autoImport extends AssemblyMinifierKeys {
    val baseMinifySettings: Seq[Setting[_]] = AssemblyMinifierPlugin.baseMinifySettings
  }

  override def requires: Plugins = AssemblyPlugin && SbtProguard

  import AssemblyPlugin.autoImport._
  import SbtProguard.autoImport._
  import autoImport._

  override lazy val projectSettings: Seq[Setting[_]] = minifySettings

  lazy val Minify: Configuration = config("minify").hide

  lazy val minifySettings: Seq[Setting[_]] = baseMinifySettings

  lazy val baseMinifySettings: Seq[Setting[_]] = Seq(
    inConfig(Minify)(ProguardSettings.default),

    inConfig(Minify)(Seq(
      assembly := Assembly.assemblyTask(assembly).value,

      assemblyOutputPath in assembly := {
        (target in assembly).value / "proguard" / (assemblyJarName in assembly).value
      }
    )),

    Seq[Setting[_]](
      minifiedAssemblyOutputPath in minifiedAssembly := (assemblyOutputPath in assembly).value,

      minifyFilters in minifiedAssembly := Seq[Filter](
        DefaultSettings.filter,
        Scala.filter,
        Akka.filter,
        MySQL.filter
      ),

      proguardVersion in Minify := "5.3.3",
      javaOptions in (Minify, proguard) := Seq("-Xmx2G"),
      options in Minify ++=
        (mainClass in Compile).value.map(mainClass => ProguardOptions.keepMain(mainClass)).toList,
      inputs in Minify := Seq((assembly in Minify).value),
      outputs in Minify := Seq((minifiedAssemblyOutputPath in minifiedAssembly).value),
      inputFilter in Minify := (_ => None),
      libraries in Minify := Seq(file(System.getProperty("java.home") + "/lib/rt.jar")),
      merge in Minify := false,

      options in Minify ++=
        (minifyFilters in minifiedAssembly).value
          .flatMap(_.config(libraryDependencies.value).map(_.toString)),

      ivyConfigurations += Minify,
      libraryDependencies += "net.sf.proguard" % "proguard-base" % (proguardVersion in Minify).value % Minify,

      minifiedAssembly := (proguard in Minify).value.head
    )
  ).flatten
}
