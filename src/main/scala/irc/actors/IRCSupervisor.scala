package irc.actors
import util._
import akka.actor._
import akka.actor.Actor._
import akka.dispatch.Futures
import org.jibble.pircbot._;

sealed trait SupervisorMessage

case class  Make(name:String) extends SupervisorMessage
case class  Destroy(name:String) extends SupervisorMessage
case class  Ken(message:String) extends SupervisorMessage
case class  Connect(channel:String) extends SupervisorMessage

case class MyBot() extends PircBot {  

        var ircSupervisor : ActorRef = _
        def setup(supervisor: ActorRef) = {
                ircSupervisor = supervisor
                setName("pirc_ken_bot5");
        }

        override def onMessage(channel : String, sender : String, login : String, hostname:String, message : String)
        {
                println("everyone knows " + message + ", duh!")
                ircSupervisor !! Ken(message)
        }

}

/**
* Return the actor refs for this actors subordinates.
*/
case object Subordinates extends SupervisorMessage
/**
* Return the names of this actors subordinates.
* You'll get a java.io.NotSerializableException if you try sending the
* Subordinates message to the app from a remote client. This happens
* because this app tries to return a list of akka.actor.LocalActorRef 
* objects, which aren't serializable! Hence, use SubordinatesNames for
* these cases!
*/
case object SubordinatesNames extends SupervisorMessage

class IRCSupervisor extends Actor with ActorUtil with PingHandler {

        /**
        * The message handler calls "pingHandler" first. If it doesn't match on the message
        * (because it is a PartialFunction), then the "defaultHandler" is tried, and finally
        * "unrecognizedMessageHandler" (from the ActorUtil trait) is tried.
        */
        def receive = pingHandler orElse defaultHandler orElse unrecognizedMessageHandler

        // Should only receive SupervisorMessages.
        def defaultHandler: PartialFunction[Any,Unit] = {
                case Make(name) =>
                val actorRef = actorOf[KenActor].start
                self link actorRef
                self reply Success("actor "+name+" created.")
                case Destroy(name) =>
                subordinates.find(_.id == name) match {
                        case None => println("No subordinate actor found with name!")
                        case Some(a) =>
                        a.stop
                        self unlink a
                }
                self reply Success("actor "+name+" destroyed.")
                case Subordinates =>
                self reply Success(subordinates)
                case SubordinatesNames =>
                self reply Success(subordinates.map(_.toString))
                case Ken(message) =>
                val response = subordinates.head !! message
                response match { 
                        case Some(item) =>
                                println("Getting the message: " + response)
                                bot.sendMessage("#pircbot", item.toString()) 
                        
                        case None =>
                                println("nothing to say...")
                        
                
                }
                self reply Success("this is a successful call: " + response)
                case Connect(channel) =>
                println("connecting " +channel)
                bot.setup(self)
                bot.setVerbose(true)
                bot.connect("irc.freenode.net");
                bot.joinChannel(channel)
                bot.sendMessage(channel, "I just connected")
                println("connected!")
                case message =>
                println(" setting up a bad message")
                self reply BadMessage(message)
        }

        def subordinates: List[ActorRef] = Actor.registry.actorsFor[KenActor].toList

        var bot: MyBot = MyBot()

        /**
        * Used by PingHandler; the list of subordinate actors (if any).
        */
        override protected def subordinatesToPing: List[ActorRef] = subordinates

}
