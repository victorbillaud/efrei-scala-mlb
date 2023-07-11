package games

import zio.json.*

import java.util.UUID

case class Game(
  season: Int,
  neutral: Boolean,
  playoff: Option[String],
  team1: String,
  team2: String,
  score1: Int,
  score2: Int
)

object Game:
  given JsonEncoder[Game] =
    DeriveJsonEncoder.gen[Game]
  given JsonDecoder[Game] =
    DeriveJsonDecoder.gen[Game]
    

