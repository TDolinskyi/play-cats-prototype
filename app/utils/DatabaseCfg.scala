package utils

import cats.effect.{Async, IO}
import com.google.inject.{Inject, Singleton}
import com.typesafe.config.Config
import helper.LoggerHelper
import model.messages.InternalResponseSuccess
import play.api.{ConfigLoader, Configuration}
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.meta.MTable
import slick.tables.SlickProductTable
import utils.DatabaseCfg.{DBConfig, DBOnStart}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class DatabaseCfg @Inject()(slickProductTable: SlickProductTable, configuration: Configuration)
                           (implicit ec: ExecutionContext)
  extends LoggerHelper {

  val database = Database.forConfig("mydb")

  lazy private val tables = List(slickProductTable.Products)

  def setup: IO[InternalResponseSuccess] = {
    val conf = configuration.get[DBConfig]("mydb")
    for {
      _ <- if (conf.onStart.createDatabase) createDb(conf, conf.properties.databaseName) else IO.unit
      _ <- if (conf.onStart.createTables) createTablesIfNotExists(tables) else IO.unit
    } yield conf.onStart match {
      case DBOnStart(true, false) => InternalResponseSuccess("DB created")
      case DBOnStart(false, true) => InternalResponseSuccess("Schemas created")
      case DBOnStart(false, false) => InternalResponseSuccess("DB and tables exists")
      case DBOnStart(true, true) => InternalResponseSuccess("DB and tables created")
    }
  }


  private def createDb(config: DBConfig, dbName: String): IO[Int] = {
    val sName = config.properties.serverName
    val sPort = config.properties.portNumber
    val dbUser = config.properties.user
    val dbPassword = config.properties.password
    val onlyHostNoDbUrl = s"jdbc:postgresql://$sName:$sPort/"
    val connection = Database.forURL(onlyHostNoDbUrl, dbUser, dbPassword)

    Async[IO].async { cb =>
      connection.run(sqlu"CREATE DATABASE #$dbName").onComplete {
        case Success(a) => cb(Right(a))
        case Failure(e) => cb(Left(e))
      }
    }


  }

  private def createTablesIfNotExists(tables: List[TableQuery[_ <: Table[_]]]): IO[List[Unit]] = {
    import cats.implicits._
    tables.map { table =>
      Async[IO].async[Unit] { cb =>
        database.run(MTable.getTables(table.baseTableRow.tableName)).flatMap { result =>
          if (result.isEmpty) {
            database.run(table.schema.create)
          } else {
            Future.successful(())
          }
        }.onComplete {
          case Success(a) => cb(Right(a))
          case Failure(e) => cb(Left(e))
        }
      }
    }.sequence
  }

}

object DatabaseCfg {

  case class DBConfigProperties(serverName: String,
                                portNumber: String,
                                databaseName: String,
                                user: String,
                                password: String)

  case class DBOnStart(createDatabase: Boolean,
                       createTables: Boolean)

  case class DBConfig(connectionPool: String,
                      dataSourceClass: String,
                      onStart: DBOnStart,
                      properties: DBConfigProperties,
                      numThreads: Int)

  object DBConfig {
    implicit val configLoader: ConfigLoader[DBConfig] = new ConfigLoader[DBConfig] {
      def load(rootConfig: Config, path: String): DBConfig = {
        val config = rootConfig.getConfig(path)
        val properties = config.getConfig("properties")
        val onStart = config.getConfig("onStart")

        DBConfig(
          connectionPool = config.getString("connectionPool"),
          dataSourceClass = config.getString("dataSourceClass"),
          onStart = DBOnStart(
            createDatabase = onStart.getBoolean("createDatabase"),
            createTables = onStart.getBoolean("createTables")
          ),
          properties = DBConfigProperties(
            serverName = properties.getString("serverName"),
            portNumber = properties.getString("portNumber"),
            databaseName = properties.getString("databaseName"),
            user = properties.getString("user"),
            password = properties.getString("password"),
          ),
          numThreads = config.getInt("numThreads")
        )
      }
    }

  }

}
