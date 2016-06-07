import java.time.Instant
import java.util.Date

/**
  * @author Anton Gnutov
  */
package object sample {
  case class Dashboard(id: String, uri: String, displayName: String, isPublic: Boolean, owner: Owner,
                       creationTime: Instant, lastModifiedTime: Instant, widgets: List[Widget])

  case class Owner(id: String, uri: String)

  case class Widget(id: Option[String], `type`: String, displayName: String)

  case class DashboardUpdate(displayName: String, isPublic: Boolean, widgets: List[Widget])
}
