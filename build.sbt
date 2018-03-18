val akkaVersion = "2.5.9"
val kafkaVersion = "1.0.1"
scalaVersion := "2.12.4"


libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % "0.19"


//prometheus

libraryDependencies ++= Seq(
  "io.prometheus" % "simpleclient" % "0.3.0",
  "io.prometheus" % "simpleclient_hotspot" % "0.3.0",
  "io.prometheus" % "simpleclient_httpserver" % "0.3.0",
  "io.prometheus" % "simpleclient_pushgateway" % "0.3.0",
)




