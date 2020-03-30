package helper

import cats.effect.IO
import play.api.mvc._

object ActionHelper {

  implicit class ActionExtension[+R[_], B](ab: ActionBuilder[R, B]) {

    def asyncF(cb: R[B] => IO[Result]): Action[B] = ab.async { c =>
      cb(c).unsafeToFuture()
    }

    def asyncF[A](bp: BodyParser[A])(cb: R[A] => IO[Result]): Action[A] =
      ab.async[A](bp) { c =>
        cb(c).unsafeToFuture()
      }

  }

}
