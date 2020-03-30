package controllers

import com.google.inject.Inject
import model.messages.InternalResponseFailure
import play.api.libs.json.Json
import play.api.mvc._
import utils.DatabaseCfg

class ApplicationController @Inject()(db: DatabaseCfg, cc: ControllerComponents) extends AbstractController(cc) {

  import helper.ActionHelper._

  def index: EssentialAction = Action.asyncF { _ =>
    db.setup.attempt.map {
      case Right(e) => Ok(Json.toJson(e))
      case Left(ex) => Ok(Json.toJson(InternalResponseFailure(ex.getMessage)))
    }
  }

}