import sbt.Attributed

name := "tcbdd"

version := "0.3.0-SNAPSHOT"

unmanagedBase := baseDirectory.value / "steps"

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "0.8.0",
  "org.clapper" % "classutil_2.10" % "1.0.5",
  "org.jbehave" % "jbehave-core" % "3.9.2",
  "org.jbehave" % "jbehave-groovy" % "3.9.2",
  "com.codeborne" % "selenide" % "2.8",
  "org.seleniumhq.selenium" % "selenium-htmlunit-driver" % "2.37.1",
  "com.opera" % "operadriver" % "1.5",
  "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.0.4",
  "commons-io" % "commons-io" % "2.4",
  "commons-cli" % "commons-cli" % "1.2",
  "net.sourceforge.findbugs" % "annotations" % "1.3.2",
  "org.specs2" %% "specs2" % "2.3.7" % "test",
  "org.mongodb" %% "casbah" % "2.7.3"
)

play.Project.playScalaSettings

org.scalastyle.sbt.ScalastylePlugin.Settings

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)