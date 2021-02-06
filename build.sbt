import ReleaseTransformations._

ThisBuild / organization := "com.github.dwickern"

lazy val root = project.in(file("."))
  .aggregate(nameof.projectRefs: _*)
  .settings(
    compile / skip := true,
    publish / skip := true,
  )

lazy val scala213 = "2.13.4"
lazy val scala212 = "2.12.13"
lazy val scala211 = "2.11.12"

lazy val nameof = (projectMatrix in file("."))
  .settings(
    name := "scala-nameof",
    publishTo := sonatypePublishToBundle.value,
    releaseCrossBuild := true,
  )
  .jsPlatform(scalaVersions = Seq(scala213, scala212, scala211))
  .jvmPlatform(scalaVersions = Seq(scala213, scala212, scala211))
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided",
      "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided",
      "org.scalatest" %%% "scalatest" % "3.2.3" % Test,
      "com.chuusai" %% "shapeless" % "2.3.3" % Test,
    ),
  )

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
