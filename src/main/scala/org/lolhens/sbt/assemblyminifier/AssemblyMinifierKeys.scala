package org.lolhens.sbt.assemblyminifier

import sbt.{File, TaskKey, taskKey}

trait AssemblyMinifierKeys {
  val minifiedAssembly: TaskKey[File] = taskKey("Builds a deployable minified fat jar.")

  val minifiedAssemblyOutputPath: TaskKey[File] = taskKey("output path of the minified fat jar")

  val minifyFilters: TaskKey[Seq[Filter]] = taskKey("filters for the minification process")
}
