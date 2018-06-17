ThisBuild / version      := "0.1.0"
ThisBuild / scalaVersion := "2.12.6"
ThisBuild / organization := "com.example"

val versions = new {
  val jackson = "2.9.5"
  val scalaLogging = "3.9.0"
  val logback = "1.2.3"
  val scalaTest = "3.0.5"
}

lazy val root = (project in file("."))
  .settings(
    name := "jackson-scala-example",
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % versions.jackson,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % versions.jackson,
      "com.typesafe.scala-logging" %% "scala-logging" % versions.scalaLogging,
      "ch.qos.logback" % "logback-classic" % versions.logback,
      "org.scalatest" %% "scalatest" % versions.scalaTest % Test
    )
  )
