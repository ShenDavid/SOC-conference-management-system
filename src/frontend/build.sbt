name := "play-ebean-example"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
  
libraryDependencies += jdbc
libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"

libraryDependencies += "org.apache.commons" % "commons-email" % "1.4"

libraryDependencies ++= Seq(
  javaWs
)

//libraryDependencies += "org.apache.commons" % "commons-io" % "2.5"
libraryDependencies ++= Seq(
  "com.sun.jersey" % "jersey-core" % "1.19.1",
  "com.sun.jersey" % "jersey-client" % "1.19.1",
  "com.sun.jersey.contribs" % "jersey-multipart" % "1.19.1")

libraryDependencies += evolutions
