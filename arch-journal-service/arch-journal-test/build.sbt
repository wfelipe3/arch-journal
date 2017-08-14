name := "arch-journal-test"

version := "1.0"

scalaVersion := "2.12.2"

resolvers += Resolver.sonatypeRepo("snapshots")
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "com.pepegar" %% "hammock-core" % "0.6.3"
libraryDependencies += "com.pepegar" %% "hammock-circe" % "0.6.3"
libraryDependencies += "com.spotify" % "docker-client" % "8.8.1"

libraryDependencies += "org.typelevel" % "cats-core_2.12" % "0.9.0"
libraryDependencies += "org.typelevel" % "cats-free_2.12" % "0.9.0"



