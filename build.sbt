
lazy val sharedSettings = Seq(
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.10.6", scalaVersion.value, "2.12.0-RC1"),
  libraryDependencies ++= Seq(
  "org.scalatest" %%% "scalatest" % "3.0.0" % "test",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
)

lazy val root = project.in(file("."))
  .aggregate(nameofJVM, nameofJS)
  .settings(sharedSettings: _*)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val nameof = crossProject.crossType(CrossType.Pure).in(file("."))
  .settings(sharedSettings: _*)
  .settings(
    organization := "com.github.dwickern",
    name := "scala-macro-nameof",
    version := "1.0"
  )

lazy val nameofJVM = nameof.jvm
lazy val nameofJS = nameof.js