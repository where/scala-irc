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
	// Here, we set up a dummy test run...
	def tryit[T](description: String, message: T) = {
		(ircSupervisor !! message) match {
			case None => println(description + ": result = None!")
			case Some(x) => println(description + ": received = Some("+x+").")
		}
	}
	tryit("ping 1", Ping("ping1"))
	tryit("subordinates empty?", Subordinates)
	tryit("Make(actor1)", Make("actor1"))
	tryit("Make(actor2)", Make("actor2"))
	tryit("Make(actor3)", Make("actor3"))
	tryit("ping 2", Ping("ping2"))
	tryit("3 subordinates?", Subordinates)
	tryit("Destroy(actor2)", Destroy("actor2"))
	tryit("ping 3", Ping("ping3"))
	tryit("2 subordinates", Subordinates)
}
