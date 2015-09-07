import com.typesafe.sbt.SbtNativePackager._

name := """nativePackagerUploadZipWithPom"""

organization := "com.company"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

packageArchetype.java_application

deploymentSettings

scalacOptions += "-target:jvm-1.6"

publishMavenStyle := true

checksums := Nil

scalacOptions += "-target:jvm-1.6"


credentials ++= {

   def loadMavenCredentials(file: java.io.File): Seq[Credentials] = {
      for {
         s <- xml.XML.loadFile(file) \ "servers" \ "server"
         id = (s \ "id").text
         username = (s \ "username").text
         password = (s \ "password").text
      } yield Credentials("Sonatype Nexus Repository Manager", "localhost", username, password)
   }
   val mvn_credentials = Path.userHome / ".m2" / "settings.xml"
   loadMavenCredentials(mvn_credentials)
}

publishTo <<= version { v: String =>
   val localRepo = "http://localhost:8081/nexus/content/repositories"
   if (v.trim.endsWith("SNAPSHOT"))
      Some("company SNAPSHOTS repo" at localRepo + "/company-snapshots")
   else
      Some("company RELEASE repo" at localRepo + "company-releases")}
