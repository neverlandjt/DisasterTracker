package stage

import dto.Message
import integration.{CalendarEvent, CalendarRoot}


object MessageMapper {

  def mapToCalendarEvent(event: CalendarEvent): Message[String] = Message.calendarEvent(name = event.summary, location = event.location, timestamp = event.updated)

}
