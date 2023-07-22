val scalaVer = "3.3.0"
val h2Version = "2.1.214"
val scalaCsvVersion = "1.3.10"
val zioVersion = "2.0.6"
val zioSchemaVersion = "0.4.8"
val zioJdbcVersion = "0.0.2"
val zioJsonVersion = "0.5.0"
val zioHtppVersion = "3.0.0-RC1"

lazy val root = (project in file("."))
  .settings(
    name := "mlb-api",
    version := "1.0",
    scalaVersion := scalaVer,
    libraryDependencies ++= Seq(
      "com.h2database" % "h2" % h2Version,
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-streams" % zioVersion,
      "dev.zio" %% "zio-schema" % zioSchemaVersion,
      "dev.zio" %% "zio-jdbc" % zioJdbcVersion,
      "dev.zio" %% "zio-json" % zioJsonVersion,
      "dev.zio" %% "zio-http" % zioHtppVersion,
      "io.getquill" %% "quill-zio" % "4.6.0",
      "io.getquill" %% "quill-jdbc-zio" % "4.6.0",
      "io.getquill" %% "quill-cassandra-zio" % "4.6.0",
      "com.github.tototoshi" %% "scala-csv" % scalaCsvVersion
    ).map(_ % Compile),
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29",
      "com.github.poslegm" %% "munit-zio" % "0.1.1"
    ).map(_ % Test)
  )
