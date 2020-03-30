package slick.tables

import javax.inject.Singleton
import model.ProductType.ProductType
import model.{ProductData, ProductType}
import org.joda.time.DateTime
import slick.jdbc.PostgresProfile.api._

@Singleton
class SlickProductTable {

  import model.ProductDateTime._

  class Products(tag: Tag) extends Table[ProductData](tag, "products") {

    def * = (id, name, description, productType, price, brand, created) <> ((ProductData.apply _).tupled, ProductData.unapply)

    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(description), Rep.Some(productType), Rep.Some(price), Rep.Some(brand), Rep.Some(created)).shaped.<>({ r => import r._; _1.map(_ => (ProductData.apply _).tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    val id: Rep[Option[Int]] = column[Option[Int]]("id", O.AutoInc, O.PrimaryKey)
    val name: Rep[String] = column[String]("name", O.Length(45, varying = true))
    val description: Rep[String] = column[String]("description", O.Length(254, varying = true))
    val productType: Rep[ProductType] = column[ProductType]("deviceType", O.Length(45, varying = true))(ProductType.enumSqlFormat)
    val price: Rep[BigDecimal] = column[BigDecimal]("price", O.Length(45, varying = true))
    val brand: Rep[String] = column[String]("brand")
    val created: Rep[Option[DateTime]] = column[Option[DateTime]]("created")
  }

  lazy val Products = new TableQuery(tag => new Products(tag))

}
