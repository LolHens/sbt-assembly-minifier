sbtPlugin := true

name := (name in ThisBuild).value

inThisBuild(Seq(
  name := "sbt-assembly-minifier",
  organization := "org.lolhens",
  version := "0.5.5",

  externalResolvers := Seq(
    Resolver.defaultLocal,
    "artifactory-maven" at "http://lolhens.no-ip.org/artifactory/maven-public/",
    Resolver.url("artifactory-ivy", url("http://lolhens.no-ip.org/artifactory/ivy-public/"))(Resolver.ivyStylePatterns)
  ),

  scalacOptions ++= Seq("-Xmax-classfile-name", "127")
))

//addSbtPlugin("com.eed3si9n" % "sbt-slash" % "0.1.0")

addCrossSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")

addCrossSbtPlugin("com.lightbend.sbt" % "sbt-proguard" % "0.3.0")

def addCrossSbtPlugin(dependency: ModuleID): Setting[Seq[ModuleID]] =
  libraryDependencies += {
    val sbtV = (sbtBinaryVersion in pluginCrossBuild).value
    val scalaV = (scalaBinaryVersion in update).value
    Defaults.sbtPluginExtra(dependency, sbtV, scalaV)
  }

crossSbtVersions := Seq("0.13.16", "1.0.1")
