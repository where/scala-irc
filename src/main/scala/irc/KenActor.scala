package irc
import shapes._
import akka.actor._
import org.apache.http.client._
import org.apache.http.client.methods._
import org.apache.http.impl.client._


class KenActor extends Actor {  
  def receive: PartialFunction[Any, Unit] = {
    case y: String  =>
      println("##" + y + "!!!")

      var i = 0
      var largest = ""
      var secondLargest = ""
      while (i < y.split(' ').length) {
         val part = y.split(' ')(i)
         if(part.length > largest.length) {
           largest = part
         }
         if(part.length > secondLargest.length && part != largest) {
           secondLargest = part
         } 
         i += 1
      }

      val twitter_url = "http://search.twitter.com/search.atom?lang=en&q=" + largest + "+" + secondLargest
      self.reply(Pair("twitter", getPageContent(twitter_url)))
      self.reply("good bye!")
    
    case x  => 
      println("###error: " + x)
      self.reply("Unknown message: " + x)
  }


  def getPageContent(url: String): String = {
    val httpclient = new DefaultHttpClient
    val httpget = new HttpGet(url)
    val brh = new BasicResponseHandler
    val responseBody = httpclient.execute (httpget, brh)
    println(responseBody)
    return responseBody
  }

}

