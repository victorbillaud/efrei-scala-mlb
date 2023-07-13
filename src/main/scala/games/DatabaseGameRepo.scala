package games

import io.getquill.Escape
import io.getquill.H2ZioJdbcContext
import io.getquill.*
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.jdbczio.Quill
import zio.*

import java.util.UUID
import javax.sql.DataSource

case class GameTable(
    uuid: UUID,
    season: Int,
    neutral: Boolean,
    playoff: String | Null,
    team1: String,
    team2: String,
    score1: Double,
    score2: Double
)

case class DatabaseGameRepo(dataSource: DataSource) extends GameRepo:
  val ctx = new H2ZioJdbcContext(Literal)

  import ctx._

  override def lookup(id: String): Task[Option[Game]] =
    ctx
      .run {
        quote {
          query[GameTable]
            .filter(p => p.uuid == lift(UUID.fromString(id)))
            .map(u =>
              Game(
                u.season,
                u.neutral,
                u.playoff,
                u.team1,
                u.team2,
                u.score1,
                u.score2
              )
            )
        }
      }
      .provide(ZLayer.succeed(dataSource))
      .map(_.headOption)

  override def games: Task[List[Game]] =
    ctx
      .run {
        quote {
          query[GameTable].map(u =>
            Game(
              u.season,
              u.neutral,
              u.playoff,
              u.team1,
              u.team2,
              u.score1,
              u.score2
            )
          )
        }
      }
      .provide(ZLayer.succeed(dataSource))

  override def seedDatabase: Task[Unit] = {
    for
      id <- Random.nextUUID
      _ <- ctx.run {
        quote {
          query[GameTable].insertValue {
            lift(
              GameTable(
                id,
                2021,
                false,
                null,
                "team1",
                "team2",
                1,
                1
              )
            )
          }
        }
      }
    yield id.toString()
  }.provide(ZLayer.succeed(dataSource)).unit

  override def create(game: Game): Task[String] = {
    for
      id <- Random.nextUUID
      _ <- ctx.run {
        quote {
          query[GameTable].insertValue {
            lift(
              GameTable(
                id,
                game.season,
                game.neutral,
                game.playoff,
                game.team1,
                game.team2,
                game.score1,
                game.score2
              )
            )
          }
        }
      }
    yield id.toString
  }.provide(ZLayer.succeed(dataSource))

object DatabaseGameRepo:
  def layer: ZLayer[Any, Throwable, DatabaseGameRepo] =
    Quill.DataSource.fromPrefix("GameApp") >>>
      ZLayer.fromFunction(DatabaseGameRepo(_))
