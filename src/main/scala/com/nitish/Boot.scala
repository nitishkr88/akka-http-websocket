package com.nitish

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.nitish.server.WebServer
import com.typesafe.config.ConfigFactory

import scala.util.{Failure, Success}

/**
  * Created by Nitish Kumar on 21-12-2016.
  */
object Boot extends App with WebServer {

  implicit val system = ActorSystem("websocket-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher


  val bindingFuture = Http().bindAndHandle(route, host, port)

  bindingFuture.onComplete {
    case Success(binding) =>
      println(s"Server is listening on ${host}:${port}/home/")
    case Failure(e) =>
      println(s"Binding failed with ${e.getMessage}")
      system.terminate()
  }

}
