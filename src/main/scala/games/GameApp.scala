package games

import zio._
import zio.http._
import zio.json._

object GameApp {
  def apply(): Http[GameRepo, Throwable, Request, Response] =
    Http.collectZIO[Request] {

      // POST /games
      case req @ (Method.POST -> !! / "games") =>
        (for {
          u <- req.body.asString.map(_.fromJson[Game])
          r <- u match
            case Left(e) =>
              ZIO
                .debug(s"Failed to parse the input: $e")
                .as(
                  Response.text(e).withStatus(Status.BadRequest)
                )
            case Right(game) =>
              GameRepo
                .create(game)
                .map(id => Response.text(id))
                .catchAll { error =>
                  Console.printError(s"Error: ${error.getMessage}") *>
                    ZIO.succeed(Response.status(Status.InternalServerError))
                }
        } yield r).orDie

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
        GameRepo.games
          .map(response => Response.json(response.toJson))
          .catchAll { error =>
            Console.printError(s"Error: ${error.getMessage}") *>
              ZIO.succeed(Response.status(Status.InternalServerError))
          }

      // POST /seed
      case Method.POST -> !! / "seed" =>
        GameRepo.seedDatabase
          .map(_ => Response.ok)
          .catchAll { error =>
            Console.printError(s"Error: ${error.getMessage}") *>
              ZIO.succeed(Response.status(Status.InternalServerError))
          }

    }
}
