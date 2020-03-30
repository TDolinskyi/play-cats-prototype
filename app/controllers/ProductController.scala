package controllers

import cats.effect.IO
import com.google.inject.Inject
import helper.LoggerHelper
import javax.inject.Singleton
import model.messages.{InternalResponseFailure, InternalResponseSuccess}
import model.{ProductData, Property}
import play.api.Configuration
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents, EssentialAction}
import service.ProductService
import utils.DatabaseCfg

import scala.concurrent.ExecutionContext

@Singleton
class ProductController @Inject()(configuration: Configuration,
                                  productService: ProductService[IO],
                                  db: DatabaseCfg,
                                  cc: ControllerComponents)
                                 (implicit ec: ExecutionContext) extends AbstractController(cc) with LoggerHelper {

  import helper.ActionHelper._

  def mock: EssentialAction = Action.asyncF { _ =>
    productService.mock.attempt.map {
      case Right(result) => Ok(Json.toJson(InternalResponseSuccess(s"mocked: ${result.length}")))
      case Left(ex) => Ok(Json.toJson(InternalResponseFailure(ex.getMessage)))
    }
  }

  def getById(id: Int): EssentialAction = Action.asyncF { _ =>
    productService.getById(id).attempt.map {
      case Right(Some(product)) => Ok(Json.toJson(product)(ProductData.json))
      case Right(None) => Ok(Json.toJson(InternalResponseFailure(s"product with id - $id doesnt exist")))
      case Left(ex) => Ok(Json.toJson(InternalResponseFailure(ex.getMessage)))
    }
  }

  def getByFilters(property: Int): EssentialAction = Action.asyncF { _ =>
    productService.getAllAndSortBy(Property(property)).map { productData =>
      Ok(Json.toJson(productData.map(Json.toJson(_)(ProductData.json))))
    }
  }

  def create: EssentialAction = Action.asyncF(parse.json) { req =>
    req.body.asOpt(ProductData.json) match {
      case None => IO.pure(Ok(s"Can`t creat product from json body: ${req.body.toString()}"))
      case Some(product) => productService.create(product).attempt.map {
        case Right(id) => Ok(Json.toJson(InternalResponseSuccess(s"product created, id - $id")))
        case Left(ex) => Ok(Json.toJson(InternalResponseFailure(ex.getMessage)))
      }
    }
  }

  def update: EssentialAction = Action.asyncF(parse.json) {
    _.body.asOpt(ProductData.json) match {
      case None => IO(Ok(s"Can`t update, js error occurred"))
      case Some(device) => productService.update(device)
        .map(updated => Ok(Json.toJson(Json.obj("status" -> updated))))
    }
  }

  def delete(id: Int): EssentialAction = Action.asyncF { _ =>
    productService.delete(id).map(s => Ok(Json.toJson(Json.obj("status" -> s))))
  }

}
