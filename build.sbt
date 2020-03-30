name := "cats-play-prototype"

version := "1.0"

lazy val `cats-play-prototype` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  jdbc,
  ws,
  guice,
  "com.google.inject" % "guice" % "4.2.3",
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "org.typelevel" %% "cats-effect" % "2.1.2",
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  filters
)

