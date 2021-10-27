// gallia-mongodb

// ===========================================================================
lazy val root = (project in file("."))
  .settings(
    organizationName     := "Gallia Project",
    organization         := "io.github.galliaproject", // *must* match groupId for sonatype
    name                 := "gallia-mongodb",
    version              := "0.3.0",    
    homepage             := Some(url("https://github.com/galliaproject/gallia-mongodb")),
    scmInfo              := Some(ScmInfo(
        browseUrl  = url("https://github.com/galliaproject/gallia-mongodb"),
        connection =     "scm:git@github.com:galliaproject/gallia-mongodb.git")),
    licenses             := Seq("BSL 1.1" -> url("https://github.com/galliaproject/gallia-mongodb/blob/master/LICENSE")),
    description          := "A Scala library for data manipulation" )
  .settings(GalliaCommonSettings.mainSettings:_*)

// ===========================================================================    
lazy val galliaVersion  = "0.3.0"

lazy val mongodbVersion = "3.12.7"
lazy val jongoVersion   = "1.4.1" // need jongo until figure out: https://stackoverflow.com/questions/35771369/mongo-java-driver-how-to-create-cursor-in-mongodb-by-cusor-id-returned-by-a-db

// ===========================================================================
libraryDependencies ++=
  Seq(
    "io.github.galliaproject" %% "gallia-core"    % galliaVersion,
    "org.mongodb"             %  "mongodb-driver" % mongodbVersion withSources() withJavadoc(),
    "org.jongo"               %  "jongo"          % jongoVersion   withSources() withJavadoc())

// ===========================================================================
sonatypeRepository     := "https://s01.oss.sonatype.org/service/local"
sonatypeCredentialHost :=         "s01.oss.sonatype.org"        
publishMavenStyle      := true
publishTo              := sonatypePublishToBundle.value

// ===========================================================================
