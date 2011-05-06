package irc.actors.util

trait Message
case class Success[T](data: T) extends Message
case class Failure[T](data: T) extends Message
case class BadMessage[T](message: T) extends Message

