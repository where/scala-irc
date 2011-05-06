package irc.actors.util
import akka.dispatch.Future
import akka.actor.Actor

/**
 * A trait to add some useful "utility" methods to actors.
 */
trait ActorUtil {
	// The following is a "self-type declaration". It says "you can treat me as
	// I mixin the Actor trait. Which ever concrete type mixes me in will also
	// have to mix in (or extend) Actor." Effectively, this lets ActorUtil invoke
	// Actor instance methods.

	this: Actor =>

  /**
   * When an unknown message is received, Return a BadMessage message
   */
  def unrecognizedMessageHandler: PartialFunction[Any, Unit] = {
    case message => self reply BadMessage(message)
  }
  
  /**
   * Process the result of a list of Actor futures in a uniform way.
   */
   def futuresToList[T](futures: List[Future[T]]): List[T] = {
     val sequence = for {
			 future <- futures
		   value  <- future.result
		 } yield value
		 sequence.toList
	 }

	/**
	 * Reasonably nice toString method
	 */
	override def toString = getClass.getName + "(id = "+ self.id + ", uuid = " + self.uuid + ")"
}
