// gallia-mongodb

// ===========================================================================
ThisBuild / organizationName     := "Gallia Project"
ThisBuild / organization         := "io.github.galliaproject" // *must* match groupId for sonatype
ThisBuild / organizationHomepage := Some(url("https://github.com/galliaproject"))
ThisBuild / startYear            := Some(2021)
ThisBuild / version              := "0.6.0-SNAPSHOT"
ThisBuild / description          := "A Scala library for data manipulation"
ThisBuild / homepage             := Some(url("https://github.com/galliaproject/gallia-mongodb"))
ThisBuild / licenses             := Seq("Apache 2" -> url("https://github.com/galliaproject/gallia-mongodb/blob/master/LICENSE"))
ThisBuild / developers           := List(Developer(
  id    = "anthony-cros",
  name  = "Anthony Cros",
  email = "contact.galliaproject@gmail.com",
  url   = url("https://github.com/anthony-cros")))
ThisBuild / scmInfo              := Some(ScmInfo(
  browseUrl  = url("https://github.com/galliaproject/gallia-mongodb"),
  connection =     "scm:git@github.com:galliaproject/gallia-mongodb.git"))

// ===========================================================================
lazy val root = (project in file("."))
  .settings(
    name   := "gallia-mongodb",
    target := baseDirectory.value / "bin" / "mongodb")
  .settings(GalliaCommonSettings.mainSettings:_*)

// ===========================================================================    
lazy val mongodbVersion = "3.12.7"
lazy val jongoVersion   = "1.4.1" // need jongo until figure out: https://stackoverflow.com/questions/35771369/mongo-java-driver-how-to-create-cursor-in-mongodb-by-cusor-id-returned-by-a-db

// ===========================================================================
libraryDependencies ++=
  Seq(
    "io.github.galliaproject" %% "gallia-core"    % version.value,
    "org.mongodb"             %  "mongodb-driver" % mongodbVersion withSources() withJavadoc(),
    "org.jongo"               %  "jongo"          % jongoVersion   withSources() withJavadoc())

// ===========================================================================
sonatypeRepository     := "https://s01.oss.sonatype.org/service/local"
sonatypeCredentialHost :=         "s01.oss.sonatype.org"
publishMavenStyle      := true
publishTo              := sonatypePublishToBundle.value

// ===========================================================================

