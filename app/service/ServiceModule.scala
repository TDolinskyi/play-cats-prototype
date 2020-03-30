package service

import cats.effect.IO
import com.google.inject.{AbstractModule, TypeLiteral}

class ServiceModule extends AbstractModule {

  override def configure(): Unit = {

    bind(new TypeLiteral[ProductService[IO]] {}).to(classOf[ProductServiceImpl])

  }

}