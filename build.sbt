import ReleaseTransformations._

ThisBuild / organization := "com.github.dwickern"

lazy val scala3 = "3.0.2"
lazy val scala213 = "2.13.7"
lazy val scala212 = "2.12.15"
lazy val scala211 = "2.11.12"

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
    publishTo := sonatypePublishToBundle.value,
    releaseCrossBuild := true,
    ideSkipProject := true,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.10" % Test,
      "javax.annotation" % "javax.annotation-api" % "1.3.1" % Test,
    ),
  )
  .jvmPlatform(scalaVersions = Seq(scala3), Seq(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "shapeless3-test" % "3.0.4" % Test,
    )
  ))
  .jvmPlatform(scalaVersions = Seq(scala213, scala212, scala211), Seq(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
      "org.scala-lang" % "scala-compiler" % scalaVersion.value % Provided,
      "com.chuusai" %% "shapeless" % "2.3.7" % Test,
    ),
  ))

Global / excludeLintKeys += ideSkipProject

ThisBuild / pomExtra := {
  <url>https://github.com/dwickern/scala-nameof</url>
    <licenses>
      <license>
        <name>MIT license</name>
        <url>http://www.opensource.org/licenses/mit-license.php</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/dwickern/scala-nameof.git</connection>
      <developerConnection>scm:git:git@github.com:dwickern/scala-nameof.git</developerConnection>
      <url>github.com/dwickern/scala-nameof.git</url>
    </scm>
    <developers>
      <developer>
        <id>dwickern</id>
        <name>Derek Wickern</name>
        <url>https://github.com/dwickern</url>
      </developer>
    </developers>
}

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
)
