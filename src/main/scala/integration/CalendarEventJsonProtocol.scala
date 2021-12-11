package integration

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsValue, _}

import java.time.Instant


case class CalendarRoot(kind: String,
                        defaultReminders: Option[Seq[String]],
                        items: Seq[CalendarEvent],
                        updated: Instant,
                        summary: String,
                        etag: String,
                        nextSyncToken: String,
                        timeZone: String,
                        accessRole: String
                       )

case class CalendarEvent(status: String,
                         kind: String,
                         end: TimeZone,
                         created: String,
                         iCalUID: String,
                         reminders: Reminder,
                         htmlLink: String,
                         sequence: Int,
                         updated: Instant,
                         summary: String,
                         start: TimeZone,
                         etag: String,
                         location: String,
                         eventType: String,
                         organizer: Organizer,
                         creator: Creator,
                         id: String
                        )

case class TimeZone(timeZone: String, dateTime: String)

case class Reminder(useDefault: Boolean)

case class Organizer(self_ : Boolean, displayName: String, email: String)

case class Creator(email: String)




object CalendarEventJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport with NullOptions with BaseJsonProtocol {
  implicit val OrganizerFormat: RootJsonFormat[Organizer] = jsonFormat(Organizer, "self", "displayName", "email")
  implicit val ReminderFormat: RootJsonFormat[Reminder] = jsonFormat1(Reminder)
  implicit val TimeZoneFormat: RootJsonFormat[TimeZone] = jsonFormat2(TimeZone)
  implicit val CreatorFormat: RootJsonFormat[Creator] = jsonFormat1(Creator)
  implicit val CalendarEventFormat: RootJsonFormat[CalendarEvent] = jsonFormat17(CalendarEvent)
  implicit val CalendarRootFormat: RootJsonFormat[CalendarRoot] = jsonFormat9(CalendarRoot)
}






