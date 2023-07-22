package games

import munit._
import zio.Task
import zio.ZIO
import zio.http.*
import zio.json.*

import java.util.UUID


class MainAppTest extends munit.ZSuite {
  val app: App[games.GameRepo] = games.GameApp().withDefaultErrorResponse

  testZ("GET /games returns a list of games") {
    val request = Request.get(URL(!! / "games"))
    assertZ(
      app
        .runZIO(request)
        .provideLayer(DatabaseGameRepo.layer)
        .map(_.status)
        .map(_ == Status.Ok)
    )
  }

  // Try to create a game
  testZ("POST /games creates a game") {
    val game = Game(
      season = 2021,
      neutral = false,
      playoff = "N",
      team1 = "ATL",
      team2 = "NYM",
      score1 = 3,
      score2 = 4
    )
    val request = Request
      .post(
        Body.fromString(game.toJson.toString),
        URL(!! / "games")
      )

    assertZ(
      app
        .runZIO(request)
        .provideLayer(DatabaseGameRepo.layer)
        .map(response => {
          response
        })
        .map(response => {
          response.status == Status.Ok
        }),
      "The game was not created"
    )
  }

  // Try to create a game with a bad body
  testZ("POST /games returns a bad request when the body is bad") {
    val request = Request
      .post(
        Body.fromString("bad body"),
        URL(!! / "games")
      )

    assertZ(
      app
        .runZIO(request)
        .provideLayer(DatabaseGameRepo.layer)
        .map(response => {
          response
        })
        .map(response => {
          response.status == Status.BadRequest
        }),
      "The game was not created"
    )
  }

  // Try to seed the database
  testZ("POST /seed seeds the database") {
    val request = Request
      .post(
        Body.fromString(""),
        URL(!! / "seed")
      )

    assertZ(
      app
        .runZIO(request)
        .provideLayer(DatabaseGameRepo.layer)
        .map(response => {
          response
        })
        .map(response => {
          response.status == Status.Ok
        }),
      "The database was not seeded"
    )
  }
}
