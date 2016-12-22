name := "akka-http-websocket"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val AkkaHttpVersion   = "10.0.0"
  Seq(
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
  )
}