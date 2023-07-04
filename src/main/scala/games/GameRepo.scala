package games
import zio.*

trait GameRepo:
  def lookup(id: String): Task[Option[Game]]

  def games: Task[List[Game]]

object GameRepo:
  def lookup(id: String): ZIO[GameRepo, Throwable, Option[Game]] =
    ZIO.serviceWithZIO[GameRepo](_.lookup(id))

  def games: ZIO[GameRepo, Throwable, List[Game]] =
    ZIO.serviceWithZIO[GameRepo](_.games)
