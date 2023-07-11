package games
import zio.*

trait GameRepo:
  def lookup(id: String): Task[Option[Game]]

  def games: Task[List[Game]]

  def create(game: Game): Task[String]

  def seedDatabase: Task[Unit]

object GameRepo:
  def lookup(id: String): ZIO[GameRepo, Throwable, Option[Game]] =
    ZIO.serviceWithZIO[GameRepo](_.lookup(id))

  def games: ZIO[GameRepo, Throwable, List[Game]] =
    ZIO.serviceWithZIO[GameRepo](_.games)

  def create(game: Game): ZIO[GameRepo, Throwable, String] =
    ZIO.serviceWithZIO[GameRepo](_.create(game))

  def seedDatabase: ZIO[GameRepo, Throwable, Unit] =
    ZIO.serviceWithZIO[GameRepo](_.seedDatabase)
