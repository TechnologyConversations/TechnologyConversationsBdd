name := "tcbdd"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.7" % "test",
  "org.mockito" % "mockito-core" % "1.9.5"
)     

play.Project.playScalaSettings

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)