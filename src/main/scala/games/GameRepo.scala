package games
import zio.*

trait GameRepo:
  def lookup(id: String): Task[Option[GameOutput]]

  def games(limit: Int, offset: Int): Task[List[GameOutput]]

  def create(game: Game): Task[String]

  def seedDatabase: Task[Unit]

object GameRepo:
  def lookup(id: String): ZIO[GameRepo, Throwable, Option[GameOutput]] =
    ZIO.serviceWithZIO[GameRepo](_.lookup(id))

  def games(limit: Int, offset: Int): ZIO[GameRepo, Throwable, List[GameOutput]] =
    ZIO.serviceWithZIO[GameRepo](_.games(limit, offset))

  def create(game: Game): ZIO[GameRepo, Throwable, String] =
    ZIO.serviceWithZIO[GameRepo](_.create(game))

  def seedDatabase: ZIO[GameRepo, Throwable, Unit] =
    ZIO.serviceWithZIO[GameRepo](_.seedDatabase)
