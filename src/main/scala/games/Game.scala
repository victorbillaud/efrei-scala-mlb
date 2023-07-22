package games

import zio.json.*

import java.util.UUID

case class Game(
  season: Int,
  neutral: Boolean,
  playoff: String | Null,
  team1: String,
  team2: String,
  score1: Double,
  score2: Double
)

object Game:
  given JsonEncoder[Game] =
    DeriveJsonEncoder.gen[Game]
  given JsonDecoder[Game] =
    DeriveJsonDecoder.gen[Game]
    

