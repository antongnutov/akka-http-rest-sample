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
    case JObject(JField("type", JString("QueueWidget")) ::
      JField("displayName", JString(displayName)) ::
      JField("width", JInt(width)) ::
      JField("height", JInt(height)) ::
      JField("settings", JObject(JField("queueId", JString(queueId)) :: Nil)) ::
      Nil) => QueueWidget(id = None, displayName = displayName, width = width.toInt, height = height.toInt, settings = QueueWidgetSettings(queueId))
    case JObject(JField("type", JString("SlaWidget")) ::
      JField("displayName", JString(displayName)) ::
      JField("width", JInt(width)) ::
      JField("height", JInt(height)) ::
      JField("settings", JObject(JField("queueId", JString(queueId)) ::
        JField("timeInterval", JObject(JField("type", JString(intType)) :: JField("from", JInt(from)) :: JField("to", JInt(to)) :: Nil))
        :: Nil))
      :: Nil) =>
      SlaWidget(id = None, displayName = displayName, width = width.toInt, height = height.toInt,
        settings = SlaWidgetSettings(queueId, TimeInterval(intType, from.longValue(), to.longValue())))
  }, {
    case widget: Widget => Extraction.decompose(widget)(Serialization.formats(NoTypeHints))
  }))

  implicit val formats = Serialization.formats(NoTypeHints).withLong + instantSerializer + widgetSerializer
  implicit val serialization = Serialization
}
