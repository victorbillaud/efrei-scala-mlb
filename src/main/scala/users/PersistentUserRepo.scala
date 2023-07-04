package users

import io.getquill.Escape
import io.getquill.H2ZioJdbcContext
import io.getquill.*
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.jdbczio.Quill
import zio.*

import java.util.UUID
import javax.sql.DataSource

case class UserTable(uuid: UUID, name: String, age: Int)

case class PersistentUserRepo(ds: DataSource) extends UserRepo:
  val ctx = new H2ZioJdbcContext(Escape)

  import ctx._

  override def register(user: User): Task[String] = {
    for
      id <- Random.nextUUID
      _ <- ctx.run {
        quote {
          query[UserTable].insertValue {
            lift(UserTable(id, user.name, user.age))
          }
        }
      }
    yield id.toString
  }.provide(ZLayer.succeed(ds))

  override def lookup(id: String): Task[Option[User]] =
    ctx
      .run {
        quote {
          query[UserTable]
            .filter(p => p.uuid == lift(UUID.fromString(id)))
            .map(u => User(u.name, u.age))
        }
      }
      .provide(ZLayer.succeed(ds))
      .map(_.headOption)

  override def users: Task[List[User]] =
    ctx
      .run {
        quote {
          query[UserTable].map(u => User(u.name, u.age))
        }
      }
      .provide(ZLayer.succeed(ds))

object PersistentUserRepo:
  def layer: ZLayer[Any, Throwable, PersistentUserRepo] =
    Quill.DataSource.fromPrefix("UserApp") >>>
      ZLayer.fromFunction(PersistentUserRepo(_))
