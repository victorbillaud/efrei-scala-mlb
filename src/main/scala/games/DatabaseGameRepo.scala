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
    date: java.time.LocalDate,
    season: Int,
    neutral: Boolean,
    playoff: Option[String],
    team1: String,
    team2: String,
    elo1_pre: Double,
    elo2_pre: Double,
    elo_prob1: Double,
    elo_prob2: Double,
    elo1_post: Double,
    elo2_post: Double,
    rating1_pre: Double,
    rating2_pre: Double,
    pitcher1: String,
    pitcher2: String,
    pitcher1_rgs: Double,
    pitcher2_rgs: Double,
    pitcher1_adj: Double,
    pitcher2_adj: Double,
    rating_prob1: Double,
    rating_prob2: Double,
    rating1_post: Double,
    rating2_post: Double,
    score1: Int,
    score2: Int
)

case class DatabaseGameRepo(dataSource: DataSource) extends GameRepo:
  val ctx = new H2ZioJdbcContext(Escape)

  import ctx._

  override def lookup(id: String): Task[Option[Game]] =
    ctx
      .run {
        quote {
          query[GameTable]
            .filter(p => p.uuid == lift(UUID.fromString(id)))
            .map(u =>
              Game(
                u.date,
                u.season,
                u.neutral,
                u.playoff,
                u.team1,
                u.team2,
                u.elo1_pre,
                u.elo2_pre,
                u.elo_prob1,
                u.elo_prob2,
                u.elo1_post,
                u.elo2_post,
                u.rating1_pre,
                u.rating2_pre,
                u.pitcher1,
                u.pitcher2,
                u.pitcher1_rgs,
                u.pitcher2_rgs,
                u.pitcher1_adj,
                u.pitcher2_adj,
                u.rating_prob1,
                u.rating_prob2,
                u.rating1_post,
                u.rating2_post,
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
              u.date,
              u.season,
              u.neutral,
              u.playoff,
              u.team1,
              u.team2,
              u.elo1_pre,
              u.elo2_pre,
              u.elo_prob1,
              u.elo_prob2,
              u.elo1_post,
              u.elo2_post,
              u.rating1_pre,
              u.rating2_pre,
              u.pitcher1,
              u.pitcher2,
              u.pitcher1_rgs,
              u.pitcher2_rgs,
              u.pitcher1_adj,
              u.pitcher2_adj,
              u.rating_prob1,
              u.rating_prob2,
              u.rating1_post,
              u.rating2_post,
              u.score1,
              u.score2
            )
          )
        }
      }
      .provide(ZLayer.succeed(dataSource))

object DatabaseGameRepo:
  def layer: ZLayer[Any, Throwable, DatabaseGameRepo] =
    Quill.DataSource.fromPrefix("GameApp") >>>
      ZLayer.fromFunction(DatabaseGameRepo(_))
