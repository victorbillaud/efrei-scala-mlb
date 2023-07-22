package games

import munit._
import zio.Task
import zio.ZIO
import zio.http.*
import zio.json.*


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
      uuid = java.util.UUID.randomUUID(),
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
          response.status
        })
        .map(_ == Status.Ok)
    )
  }
}
