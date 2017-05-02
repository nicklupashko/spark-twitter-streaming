name := "spark-twitter-streaming"

version := "1.0"

scalaVersion := "2.11.0"

resolvers ++= Seq(
  "Twitter"                       at "http://maven.twttr.com",
  "Maven Central Server"          at "http://repo1.maven.org/maven2",
  "TypeSafe Repository Releases"  at "http://repo.typesafe.com/typesafe/releases/",
  "TypeSafe Repository Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "Sonatype"                      at "https://oss.sonatype.org/content/groups/public"
)

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "org.scala-lang"   %  "scala-library"           % "2.11.0",
  "org.apache.spark" %% "spark-core"              % "1.6.0",
  "org.apache.spark" %% "spark-sql"               % "1.6.0",
  "org.apache.spark" %% "spark-streaming"         % "1.5.2",
  "org.apache.spark" %% "spark-streaming-twitter" % "1.6.0"
)
