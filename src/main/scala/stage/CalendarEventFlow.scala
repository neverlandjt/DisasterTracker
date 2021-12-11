package stage

import akka.stream.scaladsl.Flow
import dto.Message
import integration.CalendarEvent
import stage.MessageMapper.mapToCalendarEvent

import java.time.Instant

object CalendarEventFlow {
  def apply(): Flow[CalendarEvent, Message[String], Any] =
    Flow[CalendarEvent].statefulMapConcat { () =>
      var lastUpdatedTimeStamp = Instant.ofEpochMilli(0)

      { element =>
        if (element.updated.isAfter(lastUpdatedTimeStamp)) {
          lastUpdatedTimeStamp = element.updated
          element :: Nil
        } else {
          Nil
        }
      }
    }.map(mapToCalendarEvent)


}
