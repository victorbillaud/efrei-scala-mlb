package games

import zio.json.*

import java.util.UUID

case class Game(
  date: java.time.LocalDate,
  season: Int,
  neutral: Boolean,
  playoff: Option[String],
  team1: String,
  team2: String,
  elo1_pre: Double,
  elo2_pre: Double,
  elo_prob1: Double,
  elo_prob2: Double,
  elo1_post: Double,
  elo2_post: Double,
  rating1_pre: Double,
  rating2_pre: Double,
  pitcher1: String,
  pitcher2: String,
  pitcher1_rgs: Double,
  pitcher2_rgs: Double,
  pitcher1_adj: Double,
  pitcher2_adj: Double,
  rating_prob1: Double,
  rating_prob2: Double,
  rating1_post: Double,
  rating2_post: Double,
  score1: Int,
  score2: Int
)

object Game:
  given JsonEncoder[Game] =
    DeriveJsonEncoder.gen[Game]
  given JsonDecoder[Game] =
    DeriveJsonDecoder.gen[Game]

