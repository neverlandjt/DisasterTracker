package integration

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.http.javadsl.model.headers.Host
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.Accept
import akka.http.scaladsl.model.{HttpRequest, MediaTypes, Uri, _}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.{Keep, Sink, Source}

import scala.concurrent.{ExecutionContextExecutor, Future}


object DisasterTracker {
  import integration.DisasterEventJsonProtocol._

  implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  def runRequest(): Future[Root] = {


    val uri = Uri("https://eonet.gsfc.nasa.gov/api/v3/events") withQuery ("limit", "1") +: Query.Empty

    Http().singleRequest(HttpRequest(uri = uri, headers = Host.create("eonet.gsfc.nasa.gov") :: Accept(MediaTypes.`application/json`) :: Nil))
      .filter(_.status == StatusCodes.OK).flatMap {
      response => Unmarshal(response.entity).to[Root]
    }
  }

  def main(args: Array[String]): Unit = {


    val responseFuture = runRequest()

    val source = Source.future(runRequest())

    val done: Future[Done] = source.runWith(Sink.foreach(println)) //10


  }

}