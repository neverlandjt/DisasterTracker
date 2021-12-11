
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives.{complete, concat, get, getFromResource, handleWebSocketMessages, path}
import akka.http.scaladsl.server.PathMatcher
import akka.stream.FlowShape
import akka.stream.scaladsl.GraphDSL.Implicits.fanOut2flow
import akka.stream.scaladsl.{Broadcast, BroadcastHub, Flow, GraphDSL, Keep, Merge, Sink, Source}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import integration.CalendarTracker
//import edu.fp.examples.app.dto.Message
//import edu.fp.examples.app.integration.CryptoCompareSource
//import edu.fp.examples.app.stages.{MongoDBSink, PriceAvgFlow, PriceFlow, TradeFlow}

import scala.concurrent.{ExecutionContextExecutor, Future}

object Application extends App  {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val mapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
  private val graphSource = CalendarTracker()

  val serverSource: Future[Http.ServerBinding] =
    Http().newServerAt("localhost", 8080).
      bind(get {
        concat{
        path("test") {

          handleWebSocketMessages(
          Flow.fromSinkAndSource(Sink.ignore, graphSource.map(data=>mapper.writeValueAsString(data)).map(TextMessage(_)))

          )
        }}
      })
}
