package irc.boot
import akka.actor._
import akka.actor.Actor._
import akka.config.Supervision._
import irc.actors._

class Boot {

  // Do any other custom global initialization here:
  // MyObject.init

  val factory = Supervisor(
    SupervisorConfig(
      OneForOneStrategy(List(classOf[Throwable]), 5, 5000),
      Supervise(
        actorOf(new IRCSupervisor).start,
        Permanent) :: Nil))
}
