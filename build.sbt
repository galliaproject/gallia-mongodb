// gallia-mongodb

// ===========================================================================
// TODO: inherit these from core

// ---------------------------------------------------------------------------
lazy val scala213 = "2.13.4"
lazy val scala212 = "2.12.13"

// ---------------------------------------------------------------------------
lazy val supportedScalaVersions = List(scala213, scala212)

// ===========================================================================
lazy val root = (project in file("."))
  .settings(
    name               := "gallia-mongodb",
    version            := "0.1.0",
    scalaVersion       := supportedScalaVersions.head,
    crossScalaVersions := supportedScalaVersions)
  .dependsOn(RootProject(file("../gallia-core")))

// ===========================================================================
// TODO: more + inherit from core
scalacOptions in Compile ++=
  Seq("-Ywarn-value-discard") ++ 
  (scalaBinaryVersion.value match {
    case "2.13" => Seq("-Ywarn-unused:imports")
    case _      => Seq("-Ywarn-unused-import" ) })

// ===========================================================================
lazy val mongodbVersion         = "3.12.7"
lazy val jongoVersion           = "1.4.1" // need jongo until figure out: https://stackoverflow.com/questions/35771369/mongo-java-driver-how-to-create-cursor-in-mongodb-by-cusor-id-returned-by-a-db

// ===========================================================================
libraryDependencies ++=
  Seq(
    "org.mongodb" % "mongodb-driver" % mongodbVersion withSources() withJavadoc(),
    "org.jongo"   % "jongo"          % jongoVersion   withSources() withJavadoc())

// ===========================================================================
