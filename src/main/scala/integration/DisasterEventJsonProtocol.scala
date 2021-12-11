package integration

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.json._

import java.time.Instant


case class Root(title: String,
                description: String,
                link: String,
                events: List[Event])

case class Event(id: String,
                 title: String,
                 description: Option[String],
                 link: String,
                 closed: Option[String],
                 categories: List[Category],
                 sources: List[Source_],
                 geometry: List[Geo]
                )

case class Category(id: String, title: String)

case class Source_(id: String, url: String)

case class Geo(magnitudeValue: Float,
               magnitudeUnit: String,
               date: Instant,
               type_ : String,
               coordinates: List[Double])

object DisasterEventJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport with NullOptions with BaseJsonProtocol {
  implicit val GeometryFormat: RootJsonFormat[Geo] = jsonFormat(Geo, "magnitudeValue", "magnitudeUnit", "date", "type","coordinates")
  implicit val SourceFormat: RootJsonFormat[Source_] = jsonFormat2(Source_)
  implicit val CategoryFormat: RootJsonFormat[Category] = jsonFormat2(Category)
  implicit val EventFormat: RootJsonFormat[Event] = jsonFormat8(Event)
  implicit val RootFormat: RootJsonFormat[Root] = jsonFormat4(Root)
}




