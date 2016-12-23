package com.nitish.server

import java.nio.file.{Path, Paths}

import akka.NotUsed
import akka.http.scaladsl.model.ws.TextMessage.Strict
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Framing, Sink, Source}
import akka.util.ByteString
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.concurrent.forkjoin.ThreadLocalRandom

/**
  * Created by Nitish Kumar on 21-12-2016.
  */
trait WebServer extends Directives {
  val config = ConfigFactory.load()
  val host = config.getString("app.host")
  val port = config.getInt("app.port")
  val tickInterval = config.getInt("app.tickInterval")
  val delimiter = config.getString("app.delimiter")

  val file: Path = Paths.get("prepped_data_remaining.csv")
  val fileSource: Source[ByteString, Future[IOResult]] =
    FileIO.fromPath(file)
      .via(Framing.delimiter(ByteString(delimiter), Int.MaxValue))

  val delayedSource: Source[String, Future[IOResult]] = fileSource
    .map { x =>
      Thread.sleep(tickInterval)
      println(x.utf8String)
      x.utf8String
    }.map { line =>
    val lineArray = line.split(",")
    val result = "{ \"country\" : " + lineArray(0) + ", \"country_code\" : " + lineArray(1) +  ", \"region\" : " + lineArray(2) +
      ", \"gdp\" : " + lineArray(3) + ", \"life_expectancy\" : " + lineArray(4) +  ", \"fertility_rate\" : " + lineArray(5) + " }"
    result
  }

  val delayedSourceWS: Source[Strict, Future[IOResult]] = delayedSource.map(TextMessage(_))

  val route =
    assets ~ api

  def assets =
    path("") {
      getFromFile("d3-react-websocket/build/index.html")
    } ~ {
      getFromDirectory("d3-react-websocket/build")
    }


  def api =
    path("register") {
      parameter('name) { name =>
        println(s"Registered client $name")
        Thread.sleep(3000)
        println(s"Starting to send response to ${name}......")
        extractUpgradeToWebSocket { upgrade =>
          complete(upgrade.handleMessagesWithSinkSource(Sink.ignore, delayedSourceWS))
        }
      }
    }

}
