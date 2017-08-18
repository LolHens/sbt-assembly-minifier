sbtPlugin := true

name := (ThisBuild / name).value

inThisBuild(Seq(
  name := "sbt-assembly-minifier",
  organization := "org.lolhens",
  version := "0.5.0",

  scalaVersion := "2.10.6",

  externalResolvers := Seq(
    Resolver.defaultLocal,
    "artifactory-maven" at "http://lolhens.no-ip.org/artifactory/maven-public/",
    Resolver.url("artifactory-ivy", url("http://lolhens.no-ip.org/artifactory/ivy-public/"))(Resolver.ivyStylePatterns)
  ),

  scalacOptions ++= Seq("-Xmax-classfile-name", "254"),

  publishTo := Some(Resolver.file("file", new File("target/releases")))
))

addSbtPlugin("com.eed3si9n" % "sbt-slash" % "0.1.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")

addSbtPlugin("com.typesafe.sbt" % "sbt-proguard" % "0.2.3")
