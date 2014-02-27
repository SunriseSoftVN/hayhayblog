import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "hayhayblog"
  val appVersion = "0.0.1"

  val parserDependencies = Seq(
    "org.jsoup" % "jsoup" % "1.7.3",
    "org.scalanlp" %% "breeze-process" % "0.3",
    "com.googlecode.juniversalchardet" % "juniversalchardet" % "1.0.3"
  )

  val templateEngine = Seq(
    "com.mohiva" %% "play-html-compressor" % "0.2-SNAPSHOT",
    "org.ocpsoft.prettytime" % "prettytime" % "3.2.4.Final",
    "com.scalatags" %% "scalatags" % "0.2.2"
  )

  val persistentDependencies = Seq(
    "joda-time" % "joda-time" % "2.1",
    "org.joda" % "joda-convert" % "1.2",
    "org.mongodb" % "mongo-java-driver" % "2.11.2",
    "org.joda" % "joda-money" % "0.9",
    "se.radley" %% "play-plugins-salat" % "1.4.0" exclude("joda-time", "joda-time")
  )

  val appDependencies = Seq(
    "commons-collections" % "commons-collections" % "3.2.1",
    "org.apache.commons" % "commons-lang3" % "3.1",
    "org.apache.commons" % "commons-math3" % "3.0",
    "commons-digester" % "commons-digester" % "2.1" exclude("commons-beanutils", "commons-beanutils"),
    "commons-validator" % "commons-validator" % "1.4.0" exclude("commons-beanutils", "commons-beanutils"),
    "commons-io" % "commons-io" % "2.4",
    "org.apache.httpcomponents" % "httpclient" % "4.2.5",
    "jp.t2v" %% "stackable-controller" % "0.3.0",
    "jp.t2v" %% "play2-auth" % "0.11.0",
    "jp.t2v" %% "play2-auth-test" % "0.11.0" % "test",
    "org.apache.commons" % "commons-email" % "1.3.1",
    "com.restfb" % "restfb" % "1.6.11",
    "com.google.http-client" % "google-http-client-jackson2" % "1.11.0-beta",
    "com.google.apis" % "google-api-services-oauth2" % "v2-rev48-1.16.0-rc"
  ) ++ persistentDependencies ++ templateEngine ++ parserDependencies

  val appResolvers = Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
    "t2v.jp repo" at "http://www.t2v.jp/maven-repo/"
  )

  val romeDependencies = Seq(
    "org.apache.commons" % "commons-lang3" % "3.1",
    "com.rometools" % "rome" % "2.0.0-SNAPSHOT",
    "junit" % "junit" % "4.11" % "test"
  )

  val romeModules = Project("rome-modules", file("rome-modules")).settings(
    libraryDependencies ++= romeDependencies
  )

  val core = play.Project(appName + "-core", appVersion, appDependencies, path = file("core")).settings(
    templatesImport ++= Seq(
      "org.bson.types.ObjectId",
      "model._",
      "dto._"
    ),
    resolvers ++= appResolvers
  )

  val admin = play.Project(appName + "-admin", appVersion, appDependencies, path = file("admin")).settings(
    routesImport ++= Seq(
      "se.radley.plugin.salat.Binders._"
    ),
    templatesImport ++= Seq(
      "org.bson.types.ObjectId",
      "model._",
      "dto._"
    ),
    resolvers ++= appResolvers
  ).dependsOn(core)

  val main = play.Project(appName, appVersion, appDependencies).settings(
    routesImport ++= Seq(
      "se.radley.plugin.salat.Binders._"
    ),
    templatesImport ++= Seq(
      "org.bson.types.ObjectId",
      "model._",
      "dto._"
    ),
    resolvers ++= appResolvers
  ).dependsOn(admin, romeModules).aggregate(core, admin, romeModules)

}
