import akka.actor._
import akka.actor.Actor._
import irc.actors._
import irc.actors.util._

// A test script that starts the application remotely, then sends
// messages to it.

object driver {

	var ircSupervisor: ActorRef = _
	
	// It's conventional to add empty parentheses to side-effecting methods.
	def start() = {
		// Start the app on port 2552.
		remote.start("localhost", 2552)
		ircSupervisor = remote.actorFor("irc.actors.IRCSupervisor", "localhost", 2552)
	}
	
	def stop() = {
		remote.shutdown
	}

	def apply[T](description: String, message: T) = {
		(ircSupervisor !! message) match {
			case None => println("<-- " + description + ": result = None!")
			case Some(x) => println("<-- " + description + ": received = Some("+x+").")
		}
	}
}

// See the comments for SubordinatesNames in irc.actors.IRCSupervisor to
// see why we use SubordinatesNames rather tha Subordinates below.
driver.start
driver("ping 1",              Ping("ping1"))
driver("subordinates empty?", SubordinatesNames)
driver("Make(actor1)",        Make("actor1"))
driver("Make(actor2)",        Make("actor2"))
driver("Make(actor3)",        Make("actor3"))
driver("ping 2",              Ping("ping2"))
driver("3 subordinates?",     SubordinatesNames)
driver("Destroy(actor2)",     Destroy("actor2"))
driver("ping 3",              Ping("ping3"))
driver("2 subordinates",      SubordinatesNames)
driver.stop

