package sample

import akka.http.scaladsl.marshalling.{ToResponseMarshallable, _}
import akka.http.scaladsl.server._

import scala.concurrent.Future

/**
  * @author Anton Gnutov
  */
trait Resource extends Directives with JsonSupport {
  def complete[T: ToResponseMarshaller](resource: Future[Option[T]]): Route =
    onSuccess(resource) {
      case Some(t) => complete(ToResponseMarshallable(t))
      case None => complete(404, None)
    }

  def complete(resource: Future[Unit]): Route = onSuccess(resource) { complete(204, None) }
}
