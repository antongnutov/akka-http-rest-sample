package sample

import com.fasterxml.jackson.databind.ObjectMapper
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.jackson.Serialization
import org.json4s.{DefaultFormats, Formats, NoTypeHints}

/**
  * @author Anton Gnutov
  */
trait JsonSupport extends Json4sSupport {
  val m = new ObjectMapper()
  m.findAndRegisterModules()
  //implicit val formats = customFormats()
  implicit val formats = Serialization.formats(NoTypeHints)
  implicit val serialization = Serialization

  private def customFormats() = new Formats {
    val dateFormat = DefaultFormats.dateFormat
    override val typeHints = NoTypeHints
  }
}
