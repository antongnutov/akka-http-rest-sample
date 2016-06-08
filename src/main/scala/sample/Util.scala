package sample

import java.time.Instant
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

/**
  * @author Anton Gnutov
  */
object Util {
  private val currentId = new AtomicInteger(0)

  def generateId(): String = currentId.incrementAndGet().toString

  def now(): Instant = Instant.now()
}
