package games

import io.getquill.Escape
import io.getquill.H2ZioJdbcContext
import io.getquill.*
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.jdbczio.Quill
import io.getquill.autoQuote

import zio.*

import java.util.UUID
import javax.sql.DataSource

class SeedGameTable:
  val ctx = new H2ZioJdbcContext(Escape)

  import ctx._

  def seedDatabase: zio.ZIO[javax.sql.DataSource, java.sql.SQLException, Long] =
    ctx.run {
      quote {
        query[Game].insertValue {
          lift(
            Game(
              java.time.LocalDate.now(),
              2021,
              false,
              None,
              "team1",
              "team2",
              1.0,
              1.0,
              1.0,
              1.0,
              1.0,
              1.0,
              1.0,
              1.0,
              "pitcher1",
              "pitcher2",
              1.0,
              1.0,
              1.0,
              1.0,
              1.0,
              1.0,
              1.0,
              1.0,
              1,
              1
            )
          )
        }

      }

    }
