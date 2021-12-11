package integration

import spray.json.{DefaultJsonProtocol, JsNumber, JsString, JsValue, JsonFormat}

import java.time.Instant

trait BaseJsonProtocol extends DefaultJsonProtocol {
  implicit val timestampFormat: JsonFormat[Instant] = new JsonFormat[Instant] {
    override def write(obj: Instant): JsValue = JsNumber(obj.toString)

    override def read(json: JsValue): Instant = {

      json match {
        case JsString(x) => Instant.parse(x)
        case _ =>
          throw new IllegalArgumentException(
            s"Can not parse json value [$json] to a timestamp object")
      }
    }
  }
}
