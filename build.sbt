ThisBuild / organization := "com.github.dwickern"

lazy val scala3 = "3.3.5"
lazy val scala213 = "2.13.16"
lazy val scala212 = "2.12.20"

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

lazy val nameof = (projectMatrix in file("."))
  .settings(
    name := "scala-nameof",
    ideSkipProject := true,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "javax.annotation" % "javax.annotation-api" % "1.3.2" % Test,
    ),
  )
  .jvmPlatform(scalaVersions = Seq(scala3), Seq(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "shapeless3-test" % "3.4.3" % Test,
    )
  ))
  .jvmPlatform(scalaVersions = Seq(scala213, scala212), Seq(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
      "org.scala-lang" % "scala-compiler" % scalaVersion.value % Provided,
      "com.chuusai" %% "shapeless" % "2.3.12" % Test,
    ),
  ))

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
