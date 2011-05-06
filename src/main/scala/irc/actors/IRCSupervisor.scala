package irc.actors
import util._
import akka.actor._
import akka.actor.Actor._
import akka.dispatch.Futures

sealed trait SupervisorMessage
case class  Make(name:String) extends SupervisorMessage
case class  Destroy(name:String) extends SupervisorMessage
case object Subordinates extends SupervisorMessage

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
      val actorRef = actorOf(new IRCActor(name)).start
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
    case message =>
      self reply BadMessage(message)
  }

  def subordinates: List[ActorRef] = Actor.registry.actorsFor[IRCActor].toList

  /**
   * Used by PingHandler; the list of subordinate actors (if any).
   */
  override protected def subordinatesToPing: List[ActorRef] = subordinates

}
