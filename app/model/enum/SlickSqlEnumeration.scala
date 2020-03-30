package model.`enum`

import play.api.libs.json._
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType

trait SlickSqlEnumeration extends Enumeration {

  import slick.jdbc.PostgresProfile.api._

  implicit val enumSqlFormat: JdbcType[Value] with BaseTypedType[Value] = MappedColumnType.base[this.Value, Int](
    { e => e.id },
    { s => this.apply(s) }
  )

  private def app(json: JsValue) = this.apply(json.as[Int])

  implicit val enumTypeFormat: Format[Value] = new Format[Value] {
    override def reads(json: JsValue): JsResult[Value] = JsSuccess(app(json))

    override def writes(o: Value): JsValue = Json.toJson(o.id)
  }
}
