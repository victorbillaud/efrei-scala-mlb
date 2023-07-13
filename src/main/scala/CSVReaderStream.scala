package main

import com.github.tototoshi.csv._
import zio._
import java.io.File
import zio.stream.ZStream
import games.GameRepo.create
import games.Game

object CSVReaderStream {
  def readAndPrint(fileName: String): ZIO[Any, Throwable, Unit] =
    for {
      reader <- ZIO.succeed(CSVReader.open(new File(fileName)))

      stream <- ZStream
        .fromIterator(reader.iterator.drop(1)) // drop the first line
        .map({ values => 
          Game(
            values(1).toInt, // season
            values(2) == "1", // neutral
            values(3), // playoff
            values(4), // team1
            values(5), // team2
            values(6).toDouble, // score1
            values(7).toDouble // score2
          )
        })
        .grouped(1000) // group by 1000
        .foreach(chunk => ZIO.succeed(chunk.foreach(game => println(game))))

      _ <- ZIO.succeed(reader.close())
    } yield stream
}
