package model

import model.ProductType.ProductType
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}

case class ProductData(id: Option[Int],
                       name: String,
                       description: String,
                       deviceType: ProductType,
                       price: BigDecimal,
                       brand: String,
                       created: Option[DateTime])

object ProductData {
  lazy implicit val dateTime: Format[DateTime] = ProductDateTime.format
  lazy implicit val json: Format[ProductData] = Json.format[ProductData]
}