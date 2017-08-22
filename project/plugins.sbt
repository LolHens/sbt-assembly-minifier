logLevel := Level.Warn

externalResolvers := Seq(
  Resolver.defaultLocal,
  "artifactory-maven" at "http://lolhens.no-ip.org/artifactory/maven-public/",
  Resolver.url("artifactory-ivy", url("http://lolhens.no-ip.org/artifactory/ivy-public/"))(Resolver.ivyStylePatterns)
)

//addSbtPlugin("com.eed3si9n" % "sbt-slash" % "0.1.0")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.1")
