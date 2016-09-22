organization := "com.github.dwickern"
name := "scala-macro-nameof"
version := "1.0"

scalaVersion := "2.11.8"
crossScalaVersions := Seq("2.10.6", scalaVersion.value, "2.12.0-RC1")

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)