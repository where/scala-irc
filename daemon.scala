import akka.actor._
import akka.actor.Actor._
import irc.actors._
import irc.actors.util._

// Run the IRC Bot Daemon
object driver {

	var ircSupervisor: ActorRef = _
	
	// It's conventional to add empty parentheses to side-effecting methods.
	def start() = {
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

driver.start
driver("Make(actor)",        Make("actor"))
driver("1 subordinate",     SubordinatesNames)
driver("twitter actor should be hit", Ken("Kenneth Cole Reaction is a cool dude"))
driver.stop

