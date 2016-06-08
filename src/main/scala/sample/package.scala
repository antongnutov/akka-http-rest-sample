import java.time.Instant

/**
  * @author Anton Gnutov
  */
package object sample {
  case class Dashboard(id: String, uri: String, displayName: String, isPublic: Boolean, owner: Owner,
                       creationTime: Instant, lastModifiedTime: Instant, widgets: List[Widget])

  case class Owner(id: String, uri: String)

  case class DashboardUpdate(displayName: String, isPublic: Boolean, widgets: List[Widget])

  sealed trait WidgetSettings

  sealed trait Widget {
    self: Widget =>

    def id: Option[String]
    def `type`: String
    def displayName: String
    def width: Int
    def height: Int
    def settings: WidgetSettings
  }

  case class QueueWidget(id: Option[String], `type`: String = "QueueWidget", displayName: String = "Queue Widget",
                         width: Int, height: Int, settings: QueueWidgetSettings) extends Widget

  case class SlaWidget(id: Option[String], `type`: String = "SlaWidget", displayName: String = "SLA Widget",
                       width: Int, height: Int, settings: SlaWidgetSettings) extends Widget

  case class QueueWidgetSettings(queueId: String) extends WidgetSettings
  case class SlaWidgetSettings(queueId: String, timeInterval: TimeInterval) extends WidgetSettings

  case class TimeInterval(`type`: String, from: Long = 0, to: Long = 0)
}
