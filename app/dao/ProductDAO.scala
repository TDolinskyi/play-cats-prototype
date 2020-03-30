package dao

import cats.effect.{Async, IO}
import com.google.inject.{Inject, Singleton}
import helper.LoggerHelper
import model.{ProductData, Property}
import slick.jdbc.H2Profile.api._
import slick.tables.SlickProductTable
import utils.DatabaseCfg

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait ProductDAO[F[_]] {

  def getById(id: Int): F[Option[ProductData]]

  def sortedByProperty(property: Property.Value): F[List[ProductData]]

  def create(product: ProductData): F[Int]

  def update(product: ProductData): F[Int]

  def delete(id: Int): F[Int]

}

@Singleton
class ProductDAOImpl @Inject()(conf: DatabaseCfg, slickTable: SlickProductTable)(implicit ec: ExecutionContext)
  extends ProductDAO[IO] with LoggerHelper {

  import slickTable._

  private val db = conf.database

  override def getById(id: Int): IO[Option[ProductData]] = Async[IO].async { cb =>
    db.run(Products.filter(_.id === id).sortBy(_.id).result.headOption) onComplete {
      case Success(a) => cb(Right(a))
      case Failure(e) => cb(Left(e))
    }
  }

  override def sortedByProperty(property: Property.Value): IO[List[ProductData]] = {
    Async[IO].async { cb =>
      (property match {
        case Property.NAME => db.run(Products.sortBy(_.name).result).map(_.toList)
        case Property.DEVICE_TYPE => db.run(Products.sortBy(_.productType).result).map(_.toList)
        case Property.PRICE => db.run(Products.sortBy(_.price).result).map(_.toList)
        case Property.BRAND => db.run(Products.sortBy(_.brand).result).map(_.toList)
        case Property.ID => db.run(Products.sortBy(_.id).result).map(_.toList)
      }) onComplete {
        case Success(a) => cb(Right(a))
        case Failure(e) => cb(Left(e))
      }
    }
  }

  override def create(product: ProductData): IO[Int] = Async[IO].async { cb =>
    db.run(Products returning Products.map(_.id.get) += product).mapTo[Int] onComplete {
      case Success(a) => cb(Right(a))
      case Failure(e) => cb(Left(e))
    }
  }

  override def update(product: ProductData): IO[Int] = {
    Async[IO].async { cb =>
      db.run(Products.filter(_.id === product.id).update(product)) onComplete {
        case Success(a) => cb(Right(a))
        case Failure(e) => cb(Left(e))
      }
    }
  }

  override def delete(id: Int): IO[Int] = {
    Async[IO].async { cb =>
      db.run(Products.filter(_.id === id).delete) onComplete {
        case Success(a) => cb(Right(a))
        case Failure(e) => cb(Left(e))
      }
    }
  }
}
