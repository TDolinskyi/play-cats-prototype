package controllers

import com.google.inject.AbstractModule

class ControllerModule extends AbstractModule {

  override def configure(): Unit = {

    bind(classOf[ApplicationController]).asEagerSingleton()
    bind(classOf[ProductController]).asEagerSingleton()

  }

}
