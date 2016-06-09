package sample

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, Route}

/**
  * @author Anton Gnutov
  */
trait DashboardResource extends Directives with JsonSupport {

  val dashboardService: DashboardService

  private val restPath = pathPrefix("rest")

  val route: Route = restPath {
    pathPrefix("dashboards") {
      pathEndOrSingleSlash {
        get {
          onSuccess(dashboardService.getAll) {
            case dashboards => complete(OK, dashboards)
          }
        } ~
        post {
          entity(as[DashboardUpdate]) { update =>
            onSuccess(dashboardService.create(update)) {
              case dashboard => complete(Created, dashboard)
            }
          }
        }
      } ~
      pathPrefix(Segment) { id =>
        pathEndOrSingleSlash {
          get {
            onSuccess(dashboardService.find(id)) {
              case Some(d) => complete(OK, d)
              case None => complete(NotFound)
            }
          } ~
          put {
            entity(as[DashboardUpdate]) { update =>
              onSuccess(dashboardService.update(id, update)) {
                case Some(dashboard) => complete(OK, dashboard)
                case None => complete(BadRequest)
              }
            }
          } ~
          delete {
            onSuccess(dashboardService.delete(id)) {
              complete(NoContent)
            }
          }
        }
      }
    }
  }
}
