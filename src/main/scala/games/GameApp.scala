package games

import zio._
import zio.http._
import zio.json._

object GameApp {
  def apply(): Http[GameRepo, Throwable, Request, Response] =
    Http.collectZIO[Request] {

      // GET /games?page=1&limit=100
      case req @ Method.GET -> !! / "games" =>
        val limit = req.url.queryParams.get("limit").flatMap(_.headOption).map(_.toInt).getOrElse(100)
        val page = req.url.queryParams.get("page").flatMap(_.headOption).map(_.toInt).getOrElse(1)
        val offset = (page - 1) * limit
        print(s"limit: $limit, offset: $offset")
        GameRepo.games(limit, offset)
          .map(response => Response.json(response.toJson))
          .catchAll { error =>
            Console.printError(s"Error: ${error.getMessage}") *>
              ZIO.succeed(Response.status(Status.InternalServerError))
          }

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

      // POST /seed
      case Method.POST -> !! / "seed" =>
        GameRepo.seedDatabase
          .map(_ => Response.ok)
          .catchAll { error =>
            Console.printError(s"Error: ${error}") *>
              ZIO.succeed(
                Response
                  .text(error.getMessage)
                  .withStatus(Status.InternalServerError)
              )
          }

    }
}
