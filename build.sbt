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
  "org.apache.spark" %% "spark-sql"               % "2.0.1",
  "org.apache.spark" %% "spark-streaming"         % "2.0.1",
  "org.apache.bahir" %% "spark-streaming-twitter" % "2.0.1"
)
