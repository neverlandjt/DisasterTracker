package dto

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import dto.Message.MessageType

import java.time.Instant


object Message extends Enumeration {

  class MessageType extends TypeReference[Message.type]

  type Type = Value
  val CALENDAR, DISASTER, WARNING = Value

  def disasterEvent(name: String, location: String, timestamp: String) = new Message[String](name, location, Instant.parse(timestamp), "", DISASTER)

  def calendarEvent(name: String, location: String, timestamp: Instant) = new Message[String](name, location, timestamp, "", CALENDAR)

//  def overlap(timestamp: Long, price: Float, amount: Float, currency: String, market: String) = new Message[Message.Warning](timestamp, Message.Trade(price, amount), currency, market, WARNING)

//  case class Warning(calendarName: String, disasterName: String)

}

case class Message[+T](name: String, location: String, timestamp: Instant, data: T, @JsonScalaEnumeration(classOf[MessageType]) `type`: Message.Type)
