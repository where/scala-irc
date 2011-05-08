import akka.actor._
import akka.actor.Actor._
import irc.actors._
import irc.actors.util._

import org.jibble.pircbot._;

case class MyBot() extends PircBot {  

def setup() = {
  setName("pirc_ken_bot5");
  }
}


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

//driver.start
//driver("Make(actor)",        Make("actor"))
//driver("1 subordinate",     SubordinatesNames)
//driver("twitter actor should be hit", Ken("Kenneth Cole Reaction is a cool dude"))
//driver.stop


println("BOT STUFF")
val bot = MyBot()

println("my name: "+bot.getName());
bot.setup()
println("my name: "+bot.getName());

// Enable debugging output.
bot.setVerbose(true);

// Connect to the IRC server.
bot.connect("irc.freenode.net");

// Join the #pircbot channel.
bot.joinChannel("#pircbot");
bot.sendMessage("#pircbot", "I just connected")
println("my passowrd" + bot.getPassword())
bot.partChannel("#pircbot");
bot.quitServer("I quit!")





