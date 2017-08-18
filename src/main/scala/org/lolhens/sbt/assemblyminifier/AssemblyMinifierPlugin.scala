package org.lolhens.sbt.assemblyminifier

import com.typesafe.sbt.SbtProguard
import com.typesafe.sbt.SbtProguard.ProguardKeys.proguardVersion
import org.lolhens.sbt.assemblyminifier.filters._
import sbt.Keys._
import sbt._
import sbt.sbtslash.SlashSyntaxPlugin.autoImport._
import sbtassembly.AssemblyPlugin

/**
  * Created by pierr on 12.07.2017.
  */
object AssemblyMinifierPlugin extends AutoPlugin {

  object autoImport extends AssemblyMinifierKeys {
    val baseMinifySettings: Seq[Setting[_]] = AssemblyMinifierPlugin.baseMinifySettings
  }

  override def requires: Plugins = AssemblyPlugin

  import AssemblyPlugin.autoImport._
  import SbtProguard._
  import autoImport._

  override lazy val projectSettings: Seq[Setting[_]] = minifySettings

  lazy val Minify: Configuration = config("minify").hide

  lazy val minifySettings: Seq[Setting[_]] = baseMinifySettings

  lazy val baseMinifySettings: Seq[Setting[_]] = Seq(
    inConfig(Minify)(ProguardSettings.default),

    inConfig(Minify)(Seq(
      assembly := Assembly.assemblyTask(assembly).value,

      assembly / assemblyOutputPath := {
        (assembly / target).value / "proguard" / (assembly / assemblyJarName).value
      }
    )),

    Seq[Setting[_]](
      minifiedAssembly / minifiedAssemblyOutputPath := (assembly / assemblyOutputPath).value,

      minifiedAssembly / minifyFilters := Seq[Filter](
        DefaultSettings.filter,
        Scala.filter,
        Scala_2_12.filter,
        Akka.filter,
        MySQL.filter
      ),

      Minify / ProguardKeys.proguardVersion := "5.3.3",
      Minify / ProguardKeys.proguard / javaOptions := Seq("-Xmx2G"),
      Minify / ProguardKeys.options ++=
        (Compile / mainClass).value.map(mainClass => ProguardOptions.keepMain(mainClass)).toList,
      Minify / ProguardKeys.inputs := Seq((Minify / assembly).value),
      Minify / ProguardKeys.outputs := Seq((minifiedAssembly / minifiedAssemblyOutputPath).value),
      Minify / ProguardKeys.inputFilter := (_ => None),
      Minify / ProguardKeys.libraries := Seq(file(System.getProperty("java.home") + "/lib/rt.jar")),
      Minify / ProguardKeys.merge := false,

      Minify / ProguardKeys.options ++=
        (minifiedAssembly / minifyFilters).value
          .flatMap(_.config(libraryDependencies.value).map(_.toString)),

      ivyConfigurations += Minify,
      libraryDependencies += "net.sf.proguard" % "proguard-base" % (proguardVersion in Minify).value % Minify,

      minifiedAssembly := (Minify / ProguardKeys.proguard).value.head
    )
  ).flatten
}
