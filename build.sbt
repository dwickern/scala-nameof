ThisBuild / organization := "com.github.dwickern"

lazy val scala3 = "3.3.8"
lazy val scala213 = "2.13.18"
lazy val scala212 = "2.12.21"

lazy val root = project.in(file("."))
  .aggregate(nameof.projectRefs: _*)
  .enablePlugins(MdocPlugin)
  .settings(
    // for IntelliJ import: pick one project from the matrix to use
    nameof.jvm(scala3).settings,
    target := baseDirectory.value / "target",
    ideSkipProject := false,
    publish / skip := true,
    mdocIn := baseDirectory.value / "README.md",
  )

lazy val scala2Settings = Seq(
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
    "org.scala-lang" % "scala-compiler" % scalaVersion.value % Provided,
    "com.chuusai" %%% "shapeless" % "2.3.12" % Test,
  ),
)

lazy val scala3Settings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "shapeless3-test" % "3.4.3" % Test,
  ),
)

// Java runtime annotations are only testable on the JVM; see src/test/scalajvm
lazy val jvmSettings = Seq(
  libraryDependencies ++= Seq(
    "javax.annotation" % "javax.annotation-api" % "1.3.2" % Test,
  ),
)

lazy val nameof = (projectMatrix in file("."))
  .settings(
    name := "scala-nameof",
    ideSkipProject := true,
    libraryDependencies ++= Seq(
      "org.scalatest" %%% "scalatest" % "3.2.19" % Test,
    ),
  )
  .jvmPlatform(scalaVersions = Seq(scala3), scala3Settings ++ jvmSettings)
  .jvmPlatform(scalaVersions = Seq(scala213, scala212), scala2Settings ++ jvmSettings)
  .jsPlatform(scalaVersions = Seq(scala3), scala3Settings)
  .jsPlatform(scalaVersions = Seq(scala213, scala212), scala2Settings)

Global / excludeLintKeys += ideSkipProject

ThisBuild / homepage := Some(url("https://github.com/dwickern/scala-nameof"))
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / developers := List(
  Developer(
    id = "dwickern",
    name = "Derek Wickern",
    email = "dwickern@gmail.com",
    url = url("https://github.com/dwickern")
  )
)
