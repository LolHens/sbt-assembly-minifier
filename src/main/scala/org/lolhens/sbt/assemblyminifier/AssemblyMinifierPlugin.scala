package org.lolhens.sbt.assemblyminifier

import com.typesafe.sbt.SbtProguard
import org.lolhens.sbt.assemblyminifier.filters._
import sbt.Keys._
import sbt._
import sbt.sbtslash.SlashSyntaxPlugin.autoImport._
import sbtassembly.AssemblyPlugin

/**
  * Created by pierr on 12.07.2017.
  */
object AssemblyMinifierPlugin extends AutoPlugin {

  object autoImport extends AssemblyMinifierKeys

  override def requires: Plugins = AssemblyPlugin

  import AssemblyPlugin.autoImport._
  import SbtProguard._
  import autoImport._

  lazy val Minify: Configuration = config("minify").hide

  override lazy val projectSettings: Seq[Setting[_]] = minifySettings

  lazy val minifySettings: Seq[Setting[_]] =
    inConfig(Minify)(
      baseAssemblySettings ++
        ProguardSettings.default
    ) ++
      baseMinifySettings

  lazy val baseMinifySettings: Seq[Setting[_]] = proguardSettings ++ Seq[Setting[_]](
    minifiedAssembly / minifiedAssemblyOutputPath := (assembly / assemblyOutputPath).value,

    Minify / assembly / assemblyOutputPath := {
      (Minify / assembly / target).value / "proguard" / (Minify / assembly / assemblyJarName).value
    },

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

    Minify / ProguardKeys.proguard :=
      (Minify / ProguardKeys.proguard).value,

    minifiedAssembly := (Minify / ProguardKeys.proguard).value.head
  )
}
