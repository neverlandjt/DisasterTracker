name := "disasterTracker_"

version := "0.1"

scalaVersion := "2.13.7"

lazy val akkaVersion = "2.6.10"
val AkkaHttpVersion = "10.2.6"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.0"
  //  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  //  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)