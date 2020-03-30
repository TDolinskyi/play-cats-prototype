import com.google.inject.AbstractModule
import controllers.ControllerModule
import dao.DaoModule
import service.ServiceModule
import slick.tables.DbModule
import utils.UtilsModule

class GuiceModule extends AbstractModule {

  override def configure(): Unit = {
    install(new DaoModule())
    install(new ServiceModule())
    install(new UtilsModule())
    install(new DbModule())
    install(new ControllerModule())
  }

}
