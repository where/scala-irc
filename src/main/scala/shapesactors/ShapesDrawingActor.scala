package shapesactors
import shapes._
import akka.actor._

class ShapeDrawingActor extends Actor {  
  def receive: PartialFunction[Any, Unit] = {
    case s:Shape =>
      print("-> ")
      s.draw()
      self.reply("OH MY GAWD SO GOOD")
    
    case "exit"  =>
      println("-> exiting...")
      self.reply("good bye!")
    
    case x  => 
      println("-> Error: " + x)
      self.reply("Unknown message: " + x)
  }
}
