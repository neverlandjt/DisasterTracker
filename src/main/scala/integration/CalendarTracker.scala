package integration

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.{Authorization, Host}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, MediaTypes, StatusCodes, Uri}
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.{Accept, Authorization, OAuth2BearerToken}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Sink, Source}
import integration.DisasterTracker.runRequest
import stage.CalendarEventFlow

import java.time.Instant
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object CalendarTracker {

  def apply(): Source[Seq[CalendarEvent], Any] = {

    Source.fromMaterializer((mat, a) => {
      implicit val system: ActorSystem = ActorSystem()
      import integration.CalendarEventJsonProtocol._
      implicit val context: ExecutionContext.parasitic.type = ExecutionContext.parasitic
      val (queue, source) = Source.queue[Seq[CalendarEvent]](256, OverflowStrategy.dropHead).preMaterialize()(mat)

      queue.watchCompletion()
        .onComplete(_ => {
          println("disconnected")
        })

      Source.tick(FiniteDuration(0, TimeUnit.SECONDS), FiniteDuration(1, TimeUnit.SECONDS), NotUsed)
        .map(_ => {
          val uri = Uri("https://www.googleapis.com/calendar/v3/calendars/9guuq6ak4oinau8hn9p5qbp95k@group.calendar.google.com/events")
          Http().singleRequest(HttpRequest(uri = uri, headers = Authorization(OAuth2BearerToken("ya29.a0ARrdaM_G5K9TgOwgNZ0U5U9Cma62JRl8N4YkpOoOcUic0uUMDEjEdZxVWyO5TC1Ct-3k8c8LGUKww_WT4qOwo12kf6GV7IrYtD9q0UZEmHjZXGpEjcwA_G3ClgosYBPvv2ErWN3Zva4B3Jk1oLl-RoXVrJNY"))
            :: Host("www.googleapis.com") :: Accept(MediaTypes.`application/json`) :: Nil))
            .filter(_.status == StatusCodes.OK).flatMap {
            response => Unmarshal(response.entity).to[CalendarRoot].map(_.items)
          }.onComplete(_.map(queue.offer))
        })
      source
    }).async
  }
}



