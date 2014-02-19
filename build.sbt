name := "tcbdd"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.7" % "test",
  "com.github.nscala-time" %% "nscala-time" % "0.8.0",
  "org.jbehave" % "jbehave-core" % "3.9"
)     

play.Project.playScalaSettings

org.scalastyle.sbt.ScalastylePlugin.Settings

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)