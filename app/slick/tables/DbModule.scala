package slick.tables

import com.google.inject.AbstractModule

class DbModule extends AbstractModule {

  override def configure(): Unit = {

    bind(classOf[SlickProductTable]).asEagerSingleton()

  }

}
