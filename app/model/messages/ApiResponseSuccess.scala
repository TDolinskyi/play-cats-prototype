package model.messages

import play.api.libs.json.{Json, Format}

sealed trait InternalResponse

case class InternalResponseSuccess(message: String, status: String = "successful") extends InternalResponse

object InternalResponseSuccess {
  implicit val format: Format[InternalResponseSuccess] = Json.format[InternalResponseSuccess]
}

case class InternalResponseFailure(message: String, status: String = "failed") extends InternalResponse

object InternalResponseFailure {
  implicit val format: Format[InternalResponseFailure] = Json.format[InternalResponseFailure]
}

