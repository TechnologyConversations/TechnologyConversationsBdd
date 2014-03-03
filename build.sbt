name := "tcbdd"

version := "1.0-SNAPSHOT"

unmanagedBase := baseDirectory.value / "steps"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.7" % "test",
  "com.github.nscala-time" %% "nscala-time" % "0.8.0",
  "org.clapper" % "classutil_2.10" % "1.0.2",
  "org.jbehave" % "jbehave-core" % "3.9",
  "com.codeborne" % "selenide" % "2.8",
  "org.seleniumhq.selenium" % "selenium-htmlunit-driver" % "2.37.1",
  "com.opera" % "operadriver" % "1.5",
  "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.0.4"
)     

play.Project.playScalaSettings

org.scalastyle.sbt.ScalastylePlugin.Settings

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)