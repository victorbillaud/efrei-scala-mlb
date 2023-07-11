package games

import io.getquill.Escape
import io.getquill.H2ZioJdbcContext
import io.getquill.*
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.jdbczio.Quill
import zio.*

import java.sql.SQLException
import java.util.UUID
import javax.sql.DataSource
import scala.collection.immutable.Stream.Cons

case class GameTable(
    uuid: UUID,
    season: Int,
    neutral: Boolean,
    playoff: Option[String],
    team1: String,
    team2: String,
    score1: Int,
    score2: Int
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
      rows <- CSVManager.readAndPrint("mlb_elo_latest.csv")
      _ <- Console.printLine(s"Rows: ${rows.length}")
      // Drop table if exists
      _ <- ctx.run {
        quote {
          query[GameTable].delete
        }
      }
      _ <- ZIO.foreach[DataSource, SQLException, List[String], Long, List](rows) { row =>
        val id: UUID = UUID.randomUUID()
        val season = row(1).toIntOption
        val neutral = row(2).toBooleanOption
        val playoff = Option(row(3))
        val team1 = row(4)
        val team2 = row(5)
        val score1 = row(24).toIntOption
        val score2 = row(25).toIntOption
        ctx.run {
          quote {
            query[GameTable].insertValue(
              lift(
                GameTable(
                  id,
                  season.getOrElse(0),
                  neutral.getOrElse(false),
                  playoff,
                  team1,
                  team2,
                  score1.getOrElse(0),
                  score2.getOrElse(0)
                )
              )
            )
          }
        }
      }
    yield ()
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
