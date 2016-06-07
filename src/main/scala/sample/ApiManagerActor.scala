package sample

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import scala.concurrent.duration._

/**
  * @author Anton Gnutov
  */
class ApiManagerActor(host: String, port:Int) extends Actor with ActorLogging with DashboardResource {
  var binding: Option[Http.ServerBinding] = None
  implicit val system = context.system

  implicit val materializer = ActorMaterializer()
  implicit val timeout: Timeout = 15.seconds

  implicit val dispatcher = context.dispatcher

  override val dashboardService = new DashboardService()

  override def preStart(): Unit = {
    val selfRef = self
    Http().bindAndHandle(route, host, port).foreach(bound => selfRef ! bound)
  }

  override def postStop(): Unit = {
    binding foreach (_.unbind())
  }

  override def receive: Receive = {
    case boundEvent: Http.ServerBinding =>
      log.info(s"API Started at: ${boundEvent.localAddress.toString}")
      binding = Some(boundEvent)
  }
}

object ApiManagerActor {
  def props(host: String, port: Int): Props = Props(classOf[ApiManagerActor], host, port)
}
