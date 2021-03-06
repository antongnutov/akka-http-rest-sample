name := "akka-http-rest-sample"

version := "0.1"

organization in ThisBuild := "sample"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.7"
val log4j2Version = "2.5"

libraryDependencies ++= Seq(
    // Akka
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

    // Akka Http
    "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,

    // Json
    "org.json4s" %% "json4s-jackson" % "3.3.0",
    "de.heikoseeberger" %% "akka-http-json4s" % "1.7.0",

    // Logging
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4j2Version,
    "org.apache.logging.log4j" % "log4j-core" % log4j2Version,
    "org.apache.logging.log4j" % "log4j-api" % log4j2Version,
    "com.lmax" % "disruptor" % "3.3.2",

    // Testing
    "org.scalatest" %% "scalatest" % "3.0.0-RC1" % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test"
  )

enablePlugins(JavaAppPackaging)

// Bash Script config
bashScriptExtraDefines += """addJava "-Dconfig.file=${app_home}/../conf/app.conf""""
bashScriptExtraDefines += """addJava "-Dlog4j.configurationFile=${app_home}/../conf/log4j2.xml""""

// Bat Script config
batScriptExtraDefines += """set _JAVA_OPTS=%_JAVA_OPTS% -Dconfig.file=%AKKA_HTTP_REST_SAMPLE_HOME%\\conf\\app.conf"""
batScriptExtraDefines += """set _JAVA_OPTS=%_JAVA_OPTS% -Dlog4j.configurationFile=%AKKA_HTTP_REST_SAMPLE_HOME%\\conf\\log4j2.xml"""