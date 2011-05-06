package irc.actors
import util._
import akka.actor.Actor

class IRCActor(val name:String)  extends Actor with ActorUtil with PingHandler {

  self.id = name

  /**
   * The message handler calls "pingHandler" first. If it doesn't match on the message
   * (because it is a PartialFunction), then the "defaultHandler" is tried, and finally
   * "unrecognizedMessageHandler" (from the ActorUtil trait) is tried.
   */
  def receive = pingHandler orElse defaultHandler orElse unrecognizedMessageHandler


  // TODO: What should it do?
  def defaultHandler: PartialFunction[Any,Unit] = {
    case message =>
      self reply Success("message received: " + message)
  }
}
