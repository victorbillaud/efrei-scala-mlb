import zio._
import zio.http._
import zio.jdbc._

object HelloWorld extends ZIOAppDefault {

  val createZIOPoolConfig: ULayer[ZConnectionPoolConfig] =
    ZLayer.succeed(ZConnectionPoolConfig.default)

  val properties: Map[String, String] = Map(
    "user" -> "postgres",
    "password" -> "postgres"
  )

  val connectionPool : ZLayer[ZConnectionPoolConfig, Throwable, ZConnectionPool] =
    ZConnectionPool.h2mem(
      database = "testdb",
      props = properties
    )

  val create: ZIO[ZConnectionPool, Throwable, Unit] = transaction {
    execute(
      sql"CREATE TABLE IF NOT EXISTS ..."
    )
  }

  val insertRows: ZIO[ZConnectionPool, Throwable, UpdateResult] = transaction {
    insert(
      sql""
    )
  }

  val endpoints: App[Any] =
    Http
      .collect[Request] {
        case Method.GET -> !! / "test" => Response.text("Hello World!")
        case Method.GET -> !! / "init" => ???
        case Method.GET -> !! / "games" => ???
        case Method.GET -> !! / "predict" / "game" / gameId => ???
      }
      .withDefaultErrorResponse

  val app: ZIO[ZConnectionPool & Server, Throwable, Unit] = for {
    conn <- create *> insertRows
    _ <- Server.serve(endpoints)
  } yield ()

  override def run: ZIO[Any, Throwable, Unit] =
    app.provide(createZIOPoolConfig >>> connectionPool, Server.default)
}