package irc.actors
import util._
import akka.actor._
import akka.actor.Actor._
import akka.dispatch.Futures

class IRCSupervisor extends Actor with ActorUtil with PingHandler {
  /**
   * The message handler calls "pingHandler" first. If it doesn't match on the message
   * (because it is a PartialFunction), then the "defaultHandler" is tried, and finally
   * "unrecognizedMessageHandler" (from the ActorUtil trait) is tried.
   */
  def receive = pingHandler orElse defaultHandler orElse unrecognizedMessageHandler

  // Forward to other actors here?
  def defaultHandler: PartialFunction[Any,Unit] = {
    case message =>
      self.reply(List(message))
  }

  /**
   * Ping the subordinate actors (if any currently exist) and return their responses.
   */
  override protected def subordinatesToPing: List[ActorRef] = Nil // TODO

}