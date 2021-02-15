// gallia-mongodb

// ===========================================================================
lazy val root = (project in file("."))
  .settings(
    name         := "gallia-mongodb",
    version      := "0.1.0",
    scalaVersion := "2.13.4" /* TODO: inherit from core */)
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
