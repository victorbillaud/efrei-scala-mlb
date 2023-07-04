package games

import zio._
import zio.http._
import zio.json._

object GameApp {
  def apply(): Http[GameRepo, Throwable, Request, Response] =
    Http.collectZIO[Request] {

       // GET /games/:id
      case Method.GET -> !! / "games" / id =>
        GameRepo
          .lookup(id)
          .map {
            case Some(user) =>
              Response.json(user.toJson)
            case None =>
              Response.status(Status.NotFound)
          }
          .orDie
      // GET /games
      case Method.GET -> !! / "games" =>
        GameRepo.games.map(response => Response.json(response.toJson)).orDie
    }
}
