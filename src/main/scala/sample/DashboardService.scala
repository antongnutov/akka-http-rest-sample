package sample

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author Anton Gnutov
  */
class DashboardService(implicit val executionContext: ExecutionContext) {

  var dashboards: List[Dashboard] = List.empty

  def getAll: Future[List[Dashboard]] = Future { dashboards }

  def find(id: String): Future[Option[Dashboard]] = Future { dashboards.find(_.id == id) }

  def create(update: DashboardUpdate): Future[Dashboard] = Future {
    val id = Util.generateId()
    val time = Util.now()
    val uri = ""
    val owner = Owner("1234567", "")
    val widgets = update.widgets.map(w => addId(Util.generateId(), w))

    val dashboard = Dashboard(id, uri, update.displayName, update.isPublic, owner, time, time, widgets)
    dashboards :+= dashboard
    dashboard
  }

  def update(id: String, update: DashboardUpdate): Future[Option[Dashboard]] = Future {
    dashboards
      .find(_.id == id)
      .map(_.copy(lastModifiedTime = Util.now(), displayName = update.displayName, isPublic = update.isPublic,
        widgets = update.widgets.map(w => addId(Util.generateId(), w))
      )).map { dashboard =>
      dashboards = dashboards.filterNot(_.id == id) :+ dashboard
      dashboard
    }
  }

  def delete(id: String): Future[Unit] = Future {
    dashboards = dashboards.filterNot(_.id == id)
  }

  private def addId(newId: String, w: Widget): Widget = w match {
    case s: SlaWidget => s.copy(id = Some(newId))
    case q: QueueWidget => q.copy(id = Some(newId))
  }
}
