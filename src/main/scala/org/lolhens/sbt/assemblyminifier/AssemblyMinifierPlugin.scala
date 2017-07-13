package org.lolhens.sbt.assemblyminifier

import com.typesafe.sbt.SbtProguard
import org.lolhens.sbt.assemblyminifier.filters._
import sbt.Keys._
import sbt._
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
    minifiedAssemblyDefaultJarName in minifiedAssembly := (name.value + "-assembly-" + version.value + "-min.jar"),

    minifiedAssemblyJarName in minifiedAssembly := {
      ((minifiedAssemblyJarName in minifiedAssembly) or (minifiedAssemblyDefaultJarName in minifiedAssembly)).value
    },

    target in minifiedAssembly := crossTarget.value,

    minifiedAssemblyOutputPath in minifiedAssembly := {
      (target in minifiedAssembly).value / (minifiedAssemblyJarName in minifiedAssembly).value
    },


    minifyFilters in minifiedAssembly := Seq[Filter](
      DefaultSettings.filter,
      Scala.filter,
      Scala_2_12.filter,
      Akka.filter,
      MySQL.filter
    ),


    ProguardKeys.proguardVersion in Proguard := "5.3.3",

    javaOptions in(Proguard, ProguardKeys.proguard) := Seq("-Xmx2G"),

    ProguardKeys.options in Proguard ++= (mainClass in Compile).value.map(mainClass => ProguardOptions.keepMain(mainClass)).toList,

    ProguardKeys.inputs in Proguard := Seq((assemblyOutputPath in assembly).value),

    ProguardKeys.outputs in Proguard := Seq((minifiedAssemblyOutputPath in minifiedAssembly).value),

    ProguardKeys.inputFilter in Proguard := (_ => None),

    ProguardKeys.libraries in Proguard := Seq(file(System.getProperty("java.home") + "/lib/rt.jar")),

    ProguardKeys.merge in Proguard := false,

    (ProguardKeys.options in Proguard) ++= (minifyFilters in minifiedAssembly).value
      .flatMap(_.config(libraryDependencies.value).map(_.toString)),

    (ProguardKeys.proguard in Proguard) := (ProguardKeys.proguard in Proguard).dependsOn(assembly).value,

    assemblyOutputPath in assembly := {
      (target in assembly).value / "proguard" / (assemblyJarName in assembly).value
    },

    minifiedAssembly := (ProguardKeys.proguard in Proguard).value.head
  )
}
