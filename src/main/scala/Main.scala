package dev.zio.quickstart

import users.InmemoryUserRepo
import users.PersistentUserRepo
import users.UserApp
import zio._
import zio.http._

object MainApp extends ZIOAppDefault:
  def run: ZIO[Environment with ZIOAppArgs with Scope, Throwable, Any] =
    val httpApps = UserApp()
    Server
      .serve(
        httpApps.withDefaultErrorResponse
      )
      .provide(
        Server.defaultWithPort(4001),

        // An layer responsible for storing the state of the `counterApp`
        ZLayer.fromZIO(Ref.make(0)),

        // To use the persistence layer, provide the `PersistentUserRepo.layer` layer instead
        PersistentUserRepo.layer
      )