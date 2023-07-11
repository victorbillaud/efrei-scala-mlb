package games

import com.github.tototoshi.csv._
import zio._

import java.io.File

object CSVManager {
  def readAndPrint(fileName: String): ZIO[Any, Throwable, List[List[String]]] =
    for {
      reader <- ZIO.succeed(CSVReader.open(new File(fileName)))
      lines <- ZIO.succeed(reader.all())
      _ <- ZIO.succeed(reader.close())
    } yield lines
}