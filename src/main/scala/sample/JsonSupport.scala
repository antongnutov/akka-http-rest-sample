package sample

import java.time.Instant
import java.time.temporal.ChronoUnit

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.JsonAST._
import org.json4s.jackson.Serialization
import org.json4s.{CustomSerializer, Extraction, NoTypeHints}

/**
  * @author Anton Gnutov
  */
trait JsonSupport extends Json4sSupport {

  val instantSerializer = new CustomSerializer[Instant](format => ( {
    case t: JString => Instant.parse(t.values)
  }, {
    case x: Instant =>
      JString(x.truncatedTo(ChronoUnit.SECONDS).toString)
  }))

  val widgetSerializer = new CustomSerializer[Widget](format => ( {
    case obj: JObject =>
      implicit val f = Serialization.formats(NoTypeHints)
      obj \ "type" match {
        case JString("QueueWidget") => obj.extract[QueueWidget]
        case JString("SlaWidget") => obj.extract[SlaWidget]
        case _ => throw new RuntimeException("Unsupported widget")
      }
  }, {
    case widget: Widget => Extraction.decompose(widget)(Serialization.formats(NoTypeHints))
  }))

  implicit val formats = Serialization.formats(NoTypeHints).withLong + instantSerializer + widgetSerializer
  implicit val serialization = Serialization
}
