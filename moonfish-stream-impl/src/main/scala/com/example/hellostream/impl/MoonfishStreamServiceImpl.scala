package com.example.hellostream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.example.hellostream.api.MoonfishStreamService
import com.example.hello.api.MoonfishService

import scala.concurrent.Future

/**
  * Implementation of the MoonfishStreamService.
  */
class MoonfishStreamServiceImpl(moonfishService: MoonfishService) extends MoonfishStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(moonfishService.hello(_).invoke()))
  }
}
