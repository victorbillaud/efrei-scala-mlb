package main

import games.DatabaseGameRepo
import games.GameApp
import games.SeedGameTable
import zio._
import zio.http._

object MainApp extends ZIOAppDefault:
  def run: ZIO[Environment with ZIOAppArgs with Scope, Throwable, Any] =
    val httpApps = GameApp()
    
    Server
      .serve(
        httpApps.withDefaultErrorResponse
      )
      .provide(
        Server.defaultWithPort(4001),
        DatabaseGameRepo.layer
      )