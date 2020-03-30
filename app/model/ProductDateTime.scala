package model

import java.sql.Timestamp

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json._

object ProductDateTime {

  import slick.jdbc.PostgresProfile.api._

  def now: DateTime = dateTimeInstance.toDateTime(DateTimeZone.UTC)

  private def dateTimeInstance = new DateTime()

  private val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

  implicit def dateTime =
    MappedColumnType.base[DateTime, Timestamp](
      dt => new Timestamp(dt.getMillis),
      ts => new DateTime(ts.getTime)
    )

  implicit val format = new Format[DateTime] {

    override def reads(json: JsValue): JsResult[DateTime] = JsSuccess(
      DateTime.parse(json.as[String], DateTimeFormat.forPattern(dateFormat))
    )

    override def writes(o: DateTime): JsValue = JsString(o.toString())

  }
}
