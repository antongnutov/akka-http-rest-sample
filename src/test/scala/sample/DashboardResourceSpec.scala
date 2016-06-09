package sample

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.json4s.jackson.Serialization.write
import org.scalatest.{Matchers, WordSpec}

/**
  * @author Anton Gnutov
  */
class DashboardResourceSpec extends WordSpec with Matchers with ScalatestRouteTest with DashboardResource {

  val dashboardService = new DashboardService()

  "DashboardResource" should {
    var id: String = ""

    "answers OK" in {
      Get("/rest/dashboards") ~> route ~> check {
        response.status should be(OK)
        responseAs[Seq[Dashboard]].isEmpty should be(true)
      }
    }

    "adds dashboard" in {
      val update = DashboardUpdate("dashboard name", isPublic = true,
        List(QueueWidget(id = None, displayName = "widget name", width = 1, height = 1, settings = QueueWidgetSettings("123456"))))
      val requestEntity = HttpEntity(MediaTypes.`application/json`, write(update))

      Post("/rest/dashboards", requestEntity) ~> route ~> check {
        response.status should be(Created)
        val dashboard = responseAs[Dashboard]
        id = dashboard.id
        id.nonEmpty should be(true)
        dashboard.displayName should be("dashboard name")
        dashboard.widgets.size should be(1)
      }
    }

    "answers dashboards" in {
      Get("/rest/dashboards") ~> route ~> check {
        response.status should be(OK)
        val resp = responseAs[Seq[Dashboard]]
        resp.isEmpty should be(false)
        resp.head.displayName should be("dashboard name")
        resp.head.id should be(id)
      }
    }

    "answers dashboard" in {
      Get(s"/rest/dashboards/$id") ~> route ~> check {
        response.status should be(OK)
        val resp = responseAs[Dashboard]
        resp.displayName should be("dashboard name")
        resp.id should be(id)
        resp.isPublic should be(true)
      }
    }

    "updates dashboard" in {
      val update = DashboardUpdate("new name", isPublic = false,
        List(QueueWidget(id = None, displayName = "widget name2", width = 1, height = 1, settings = QueueWidgetSettings("123456"))))
      val requestEntity = HttpEntity(MediaTypes.`application/json`, write(update))

      Put(s"/rest/dashboards/$id", requestEntity) ~> route ~> check {
        response.status should be(OK)
        val resp = responseAs[Dashboard]
        resp.displayName should be("new name")
        resp.id should be(id)
        resp.isPublic should be(false)
      }
    }

    "deletes dashboard" in {
      Delete(s"/rest/dashboards/$id") ~> route ~> check {
        response.status should be(NoContent)
      }
    }

    "not found" in {
      Get(s"/rest/dashboards/$id") ~> route ~> check {
        response.status should be(NotFound)
      }
    }
  }
}
