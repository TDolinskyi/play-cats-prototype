package utils

import com.google.inject.AbstractModule

class UtilsModule extends AbstractModule {

  override def configure(): Unit = {

    bind(classOf[DatabaseCfg]).asEagerSingleton()

  }

}
