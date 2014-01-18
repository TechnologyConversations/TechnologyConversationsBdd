name := "tcbdd"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "postgresql" % "postgresql" % "8.4-702.jdbc4",
  "org.specs2" %% "specs2" % "2.3.7" % "test"
)     

play.Project.playScalaSettings

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)