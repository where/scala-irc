package irc.boot
import akka.actor._
import akka.actor.Actor._
import akka.config.Supervision._
import irc.actors._
import irc.actors.util.Ping

class Boot {

  // Do other custom global initialization here that
	// should happen BEFORE the actors are started:
  // MyObject.init

	val ircSupervisor = actorOf(new IRCSupervisor).start
  val factory = Supervisor(
    SupervisorConfig(
      OneForOneStrategy(List(classOf[RuntimeException]), 5, 5000),
      Supervise(ircSupervisor, Permanent) ::  Nil))

  // Do any other custom global initialization here that
	// should happen After the actors are started:
}
