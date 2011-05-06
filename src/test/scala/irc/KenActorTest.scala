package irc
import org.scalatest.{FlatSpec, FunSuite, BeforeAndAfterAll}
import org.scalatest.matchers.ShouldMatchers
import shapes._
import akka.actor._
import akka.actor.Actor._
import scala.xml._

object KenActorDriver {
   var msg = ""
}
class KenActorDriver extends Actor {
  val drawer = actorOf[KenActor].start()

  def receive: PartialFunction[Any, Unit] = {
    case "good bye!" =>
      drawer.stop()
      self.stop()
    case Pair("twitter", x:String) => 
      val title = (XML.loadString(x) \\ "feed" \ "entry" \ "title")(0).text
      println("the title is: " + title)
/*	
    x match {
      case "feed" \ "entry" \ "title" => 
      println("the internet has a response: " + x)
      }

*/
    case y: String =>
      println("Sending off " + y + " for a reply")
      KenActorDriver.msg = y
      drawer ! y
    case msg =>
      println("ERROR NOT SUPPORTED")
      KenActorDriver.msg = "ERROR HAPPENED"
  }
}



class KenActorTest extends FunSuite with ShouldMatchers with BeforeAndAfterAll {
  override def beforeAll(configMap: Map[String, Any]) {
  }
  override def afterAll(configMap: Map[String, Any]) {
  }

  test ("i+j just works.") {
    // test here

    val driver = actorOf[KenActorDriver].start()
    driver !! "Charlie Sheen is the man"

    //driver.actor.msg()
    println("!!!!!!!!!!!!!!")
    println(KenActorDriver.msg)
  }

}  





