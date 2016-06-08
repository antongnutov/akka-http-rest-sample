package sample

import java.time.Instant
import java.time.temporal.ChronoUnit

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.JsonAST.JString
import org.json4s.jackson.Serialization
import org.json4s.{CustomSerializer, NoTypeHints}

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

  implicit val formats = Serialization.formats(NoTypeHints).withLong + instantSerializer
  implicit val serialization = Serialization
}
