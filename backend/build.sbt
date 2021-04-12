name := "CoderBlog"

version := "0.1"

scalaVersion := "2.13.5"

idePackagePrefix := Some("ru.codercat")

lazy val root = (project in file("."))
  .enablePlugins(Api4s)
  .settings(
    libraryDependencies ++= Seq(
      "com.github.IndiscriminateCoding" %% "api4s-core" % "0.2.6",
      "org.http4s" %% "http4s-blaze-server" % "0.21.21",
      "ch.qos.logback" % "logback-classic" % "1.1.3"
    ),
    api4sSources := Seq(Api4s.Src(
      file = sourceDirectory.value / "main" / "swagger" / "CoderBlog.yaml",
      pkg = "ru.codercat.api.posts",
      server = true,
      client = false
    ).without4xx.without5xx),
    mainClass in assembly := Some("ru.codercat.Server"),
    assemblyJarName in assembly := "CoderBlog.jar"
  )


