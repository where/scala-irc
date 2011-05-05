import sbt._
import sbt.CompileOrder._
import de.element34.sbteclipsify._

class ActorExample(info: ProjectInfo) extends DefaultWebProject(info)
		with AkkaProject with IdeaProject with Eclipsify with Exec {

  lazy val EmbeddedRepo   = MavenRepository("Embedded Repo", (info.projectPath / "embedded-repo").asURL.toString)
  lazy val LocalMavenRepo = MavenRepository("Local Maven Repo", (Path.userHome / ".m2" / "repository").asURL.toString)
  lazy val AkkaRepo       = MavenRepository("Akka Repository", "http://akka.io/repository")
  lazy val ScalaToolsRepo = MavenRepository("Scala Tools Repo", "http://nexus.scala-tools.org/content/repositories/hosted")

  lazy val akkaTypedActor = akkaModule("typed-actor")
  lazy val akkaKernel     = akkaModule("kernel")
  lazy val scalaTestModuleConfig   = ModuleConfiguration("org.scalatest",   ScalaToolsRepo)

  lazy val embeddedRepo   = EmbeddedRepo
  lazy val localMavenRepo = LocalMavenRepo

  override def repositories = Set(AkkaRepo, EmbeddedRepo, ScalaToolsRepo)

  lazy val scalaTest      = "org.scalatest"          % "scalatest"       % "1.3"      % "test"

  override def libraryDependencies = (Set(akkaTypedActor, akkaKernel) map (_ % "compile")) ++ Set(scalaTest)

  override def compileOptions = super.compileOptions ++
    Seq("-deprecation",
        "-unchecked",
        "-Xmigration",
        "-Xcheckinit",
        "-Xwarninit",
        "-encoding", "utf8")
        .map(x => CompileOption(x))

  // If you want to have run always use a particular class
  // Uncomment this line and define the value appropriately.
  // override def mainClass = Some("shapesactors.ShapesDrawingDriver")
}
