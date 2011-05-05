package shapesactors
import org.scalatest.{FlatSpec, FunSuite, BeforeAndAfterAll}
import org.scalatest.matchers.ShouldMatchers

class FooBarTest extends FunSuite with ShouldMatchers with BeforeAndAfterAll {
  override def beforeAll(configMap: Map[String, Any]) {
  }
  override def afterAll(configMap: Map[String, Any]) {
  }

  test ("i+j just works.") {
    (1 -> 3) should equal (Pair(1,3))
  }

}