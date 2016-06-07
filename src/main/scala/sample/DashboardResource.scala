package sample

/**
  * @author Anton Gnutov
  */
trait DashboardResource extends Resource with JsonSupport {

  val dashboardService: DashboardService

  private val restPath = pathPrefix("rest")

  val route = restPath {
    pathPrefix("dashboards") {
      pathEndOrSingleSlash {
        get {
          complete(dashboardService.getAll)
        } ~
        post {
          entity(as[DashboardUpdate]) { update =>
            onSuccess(dashboardService.create(update)) {
              case dashboard => complete(201, dashboard)
            }
          }
        }
      } ~
      pathPrefix(Segment) { id =>
        pathEndOrSingleSlash {
          get {
            complete(dashboardService.find(id))
          } ~
          put {
            entity(as[DashboardUpdate]) { update =>
              onSuccess(dashboardService.update(id, update)) {
                case Some(dashboard) => complete(dashboard)
                case None => complete(400, None)
              }
            }
          } ~
          delete {
            complete(dashboardService.delete(id))
          }
        }
      }
    }
  }
}
