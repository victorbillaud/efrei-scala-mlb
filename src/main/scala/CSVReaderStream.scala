package main

import com.github.tototoshi.csv._
import zio._
import java.io.File
import zio.stream.ZStream
import games.GameRepo.create
import games.Game
// import games.DatabaseGameRepo.seedDatabase

object CSVReaderStream {
  def readAndInsert(fileName: String): ZIO[Any, Throwable, Unit] =
    for {
      reader <- ZIO.succeed(CSVReader.open(new File(fileName)))

      stream <- ZStream
        .fromIterator[Seq[String]](reader.iterator.drop(1)) // drop the first line
        .map(createGameObject)
        .grouped(1000) // group by 1000
        //.foreach(chunk => seedDatabase(chunk.toList))
        .foreach(chunk => ZIO.succeed(chunk.foreach(game => println(game))))
        // .foreach(chunk => ZIO.succeed(chunk.foreach(game => create(game))))

      _ <- ZIO.succeed(reader.close())
    } yield ()


  def createGameObject(values: Seq[String]): Game =
    Game(
      values(1).toInt, // season
      values(2) == "1", // neutral
      values(3), // playoff
      values(4), // team1
      values(5), // team2
      values(6).toDouble, // score1
      values(7).toDouble // score2
    )
}
