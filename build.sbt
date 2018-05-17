sbtPlugin := true

name := (name in ThisBuild).value

inThisBuild(Seq(
  name := "sbt-assembly-minifier",
  organization := "org.lolhens",
  version := "1.0.0",

  resolvers ++= Seq(
    "lolhens-maven" at "http://artifactory.lolhens.de/artifactory/maven-public/",
    Resolver.url("lolhens-ivy", url("http://artifactory.lolhens.de/artifactory/ivy-public/"))(Resolver.ivyStylePatterns)
  ),

  scalacOptions ++= Seq("-Xmax-classfile-name", "127")
))

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.6")

addSbtPlugin("com.lightbend.sbt" % "sbt-proguard" % "0.3.0")

/*def addCrossSbtPlugin(dependency: ModuleID): Setting[Seq[ModuleID]] =
  libraryDependencies += {
    val sbtV = (sbtBinaryVersion in pluginCrossBuild).value
    val scalaV = (scalaBinaryVersion in update).value
    Defaults.sbtPluginExtra(dependency, sbtV, scalaV)
  }

crossSbtVersions := Seq("0.13.16", "1.0.1")*/
