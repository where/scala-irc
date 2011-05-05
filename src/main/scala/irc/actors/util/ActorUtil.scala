package irc.actors.util
import akka.dispatch.Future

/**
 * A trait to add "utility" methods to actors, such as extracting JSON from Futures.
 */
trait ActorUtil {
  /**
   * When an unknown message is received, return a uniform error message.
   */
  def unrecognizedMessageHandler: PartialFunction[Any, Unit] = {
    case message => List("error: Unrecognized message received: " + message)
  }
  
  /**
   * Process the result of a list of Actor futures in a uniform way.
   */
   def futuresToList(futures: List[Future[_]], messageForNone: String): List[String] =
     (for (future <- futures) yield futureToString(future, messageForNone)).toList

  /**
   * Process the result of a single Actor future in a uniform way.
   */
  def futureToString(future: Future[_], messageForNone: String): String = future.result match {
    case Some(result) => result.toString
    case None => messageForNone
  }
}
