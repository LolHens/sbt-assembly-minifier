sbtPlugin := true

name := (name in ThisBuild).value

inThisBuild(Seq(
  name := "sbt-assembly-minifier",
  organization := "org.lolhens",
  version := "0.0.7",

  scalaVersion := "2.10.6",

  externalResolvers := Seq(
    "artifactory-maven" at "http://lolhens.no-ip.org/artifactory/maven-public/",
    Resolver.url("artifactory-ivy", url("http://lolhens.no-ip.org/artifactory/ivy-public/"))(Resolver.ivyStylePatterns)
  ),

  scalacOptions ++= Seq("-Xmax-classfile-name", "254"),

  publishTo := Some(Resolver.file("file", new File("target/releases")))
))

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")
addSbtPlugin("com.typesafe.sbt" % "sbt-proguard" % "0.2.3")
