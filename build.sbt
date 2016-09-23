import com.typesafe.sbt.pgp.PgpKeys

lazy val sharedSettings = Seq(
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.10.6", scalaVersion.value, "2.12.0-RC1"),
  libraryDependencies ++= Seq(
  "org.scalatest" %%% "scalatest" % "3.0.0" % "test",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ),
  pomExtra := {
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
)

lazy val root = project.in(file("."))
  .aggregate(nameofJVM, nameofJS)
  .settings(sharedSettings: _*)
  .settings(
    publish := {},
    publishLocal := {},
    PgpKeys.publishSigned := {}
  )

lazy val nameof = crossProject.crossType(CrossType.Pure).in(file("."))
  .settings(sharedSettings: _*)
  .settings(
    organization := "com.github.dwickern",
    name := "scala-nameof",
    version := "1.0"
  )

lazy val nameofJVM = nameof.jvm
lazy val nameofJS = nameof.js
