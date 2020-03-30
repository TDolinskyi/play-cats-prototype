package service

import cats.effect.IO
import cats.implicits._
import com.google.inject.{Inject, Singleton}
import dao.ProductDAO
import helper.LoggerHelper
import model.{Data, ProductData, ProductDateTime, Property}
import play.api.Configuration

trait ProductService[F[_]] {

  def mock: F[List[Int]]

  def getById(id: Int): F[Option[ProductData]]

  def getAllAndSortBy(property: Property.Value): F[List[ProductData]]

  def create(product: ProductData): F[Int]

  def update(product: ProductData): F[Int]

  def delete(id: Int): F[Int]

}

@Singleton
class ProductServiceImpl @Inject()(productDao: ProductDAO[IO], configuration: Configuration)
  extends ProductService[IO] with LoggerHelper {

  override def mock: IO[List[Int]] = Data.dataToGenerate.map(create).sequence

  override def getById(id: Int): IO[Option[ProductData]] = productDao.getById(id)

  override def getAllAndSortBy(property: Property.Value): IO[List[ProductData]] = productDao.sortedByProperty(property)

  override def create(product: ProductData): IO[Int] = {
    productDao.create(product.copy(created = Some(ProductDateTime.now))).map{ id =>
      logger.info(s"created new product: ${product.copy(id = Some(id)).toString} "); id
    }
  }

  override def update(product: ProductData): IO[Int] = productDao.update(product)

  override def delete(id: Int): IO[Int] = productDao.delete(id)
}
