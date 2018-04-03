name := "SparkWordCount"

version := "0.1"

scalaVersion := "2.11.8"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.0",
  "org.apache.spark" %% "spark-sql"  % "2.2.0",
  "org.apache.solr" % "solr-core" % "1.4.1",
  "org.apache.lucene" % "lucene-core" % "3.0.3",
  "org.wltea.ik-analyzer" % "ik-analyzer" % "3.2.8",
  "org.xerial" % "sqlite-jdbc" % "3.20.0",
  "com.microsoft.sqlserver" % "mssql-jdbc" % "6.4.0.jre8"
)
