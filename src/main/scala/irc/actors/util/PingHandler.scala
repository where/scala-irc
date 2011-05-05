package irc.actors.util
import akka.actor._
import akka.dispatch.{Future, Futures, FutureTimeoutException}

case class Ping(message: String)

/** 
 * Logic for "pinging" a hierarchy of actors. It is useful for ensuring liveness,
 * debugging, and for returning a list of the actors that are currently alive.
 */
trait PingHandler extends Actor with ActorUtil {
  
  /**
   * Return a list of "subordinates" to ping. An actor that supervises other 
   * actors should override this method to return the list of those actors.
   */
  protected def subordinatesToPing: List[ActorRef] = Nil
  
  /**
   * Handle ping-related messages. Use in the receive method:
   * e.g., <tt>def receive = pingHandler orElse ...</tt>.
   * TODO: If there is only one actor running, an array with that actor is returned. If there
   * are multiple actors, an array of an array is returned. Fix so only a single-level array
	 * is always returned!
   * TODO: Make the format of the returned value user definable, e.g., with a "formatter".
   */
  def pingHandler: PartialFunction[Any, Unit] = {
    case Ping(message) => 
      log.debug("Ping message: "+message)
      val fullResponse = this.toString :: pingSubordinates(subordinatesToPing, message)
      self reply fullResponse
  } 
  
  protected def pingSubordinates(actors: List[ActorRef], message: String): List[String] = actors match {
    case Nil => Nil
    case _ => 
      try {
        val futures = actors map { _ !!! Ping(message) }
        Futures.awaitAll(futures)
        handlePingRepliesIn(futures)
      } catch {
        case fte: FutureTimeoutException =>
          List("error: Actors timed out (" + fte.getMessage + ")")
      }
  }
    
  def handlePingRepliesIn(futures: Iterable[Future[_]]): List[String] =
    futuresToList(futures toList, "failed!")
}  
