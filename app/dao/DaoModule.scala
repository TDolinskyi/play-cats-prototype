package dao

import cats.effect.IO
import com.google.inject.{AbstractModule, TypeLiteral}
import utils.DatabaseCfg

class DaoModule extends AbstractModule {

  override def configure(): Unit = {

    bind(new TypeLiteral[ProductDAO[IO]] {}).to(classOf[ProductDAOImpl])
    bind(classOf[DatabaseCfg]).asEagerSingleton()

  }

}
