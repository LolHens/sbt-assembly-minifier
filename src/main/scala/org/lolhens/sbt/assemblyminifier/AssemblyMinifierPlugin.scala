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

  object autoImport {
    val minifiedAssembly: TaskKey[File] = taskKey("Builds a deployable minified fat jar.")

    val minifiedAssemblyJarName: TaskKey[String] = taskKey("name of the minified jar")

    val minifiedAssemblyDefaultJarName: TaskKey[String] = taskKey("default name of the minified fat jar")

    val minifiedAssemblyOutputPath: TaskKey[File] = taskKey("output path of the minified fat jar")

    val minifyFilters: TaskKey[Seq[Filter]] = taskKey("filters for the minification process")
  }

  override def requires: Plugins = AssemblyPlugin

  import AssemblyPlugin.autoImport._
  import SbtProguard._
  import autoImport._

  override lazy val projectSettings: Seq[Setting[_]] = proguardSettings ++ Seq[Setting[_]](
    minifiedAssembly / minifiedAssemblyDefaultJarName := (name.value + "-assembly-" + version.value + "-min.jar"),

    minifiedAssembly / minifiedAssemblyJarName := {
      ((minifiedAssembly / minifiedAssemblyJarName) or (minifiedAssembly / minifiedAssemblyDefaultJarName)).value
    },

    minifiedAssembly / target := crossTarget.value,

    minifiedAssembly / minifiedAssemblyOutputPath := {
      (minifiedAssembly / target).value / (minifiedAssembly / minifiedAssemblyJarName).value
    },

    minifiedAssembly / minifyFilters := Seq[Filter](
      DefaultSettings.filter,
      Scala.filter,
      Scala_2_12.filter,
      Akka.filter,
      MySQL.filter
    ),

    Proguard / ProguardKeys.proguardVersion := "5.3.3",
    Proguard / ProguardKeys.proguard / javaOptions := Seq("-Xmx2G"),
    Proguard / ProguardKeys.options ++= (Compile / mainClass).value.map(mainClass => ProguardOptions.keepMain(mainClass)).toList,
    Proguard / ProguardKeys.inputs := Seq((assembly / assemblyOutputPath).value),
    Proguard / ProguardKeys.outputs := Seq((minifiedAssembly / minifiedAssemblyOutputPath).value),
    Proguard / ProguardKeys.inputFilter := (_ => None),
    Proguard / ProguardKeys.libraries := Seq(file(System.getProperty("java.home") + "/lib/rt.jar")),
    Proguard / ProguardKeys.merge := false,

    Proguard / ProguardKeys.options ++=
      (minifiedAssembly / minifyFilters).value
        .flatMap(_.config(libraryDependencies.value).map(_.toString)),

    Proguard / ProguardKeys.proguard := (Proguard / ProguardKeys.proguard).dependsOn(assembly).value,

    assembly / assemblyOutputPath := {
      (assembly / target).value / "proguard" / (assembly / assemblyJarName).value
    },

    minifiedAssembly := (Proguard / ProguardKeys.proguard).value.head
  )
}
