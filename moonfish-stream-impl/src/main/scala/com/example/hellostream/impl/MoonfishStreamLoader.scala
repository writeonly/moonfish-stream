package com.example.hellostream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.example.hellostream.api.MoonfishStreamService
import com.example.hello.api.MoonfishService
import com.softwaremill.macwire._

class MoonfishStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new MoonfishStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new MoonfishStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[MoonfishStreamService])
}

abstract class MoonfishStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[MoonfishStreamService](wire[MoonfishStreamServiceImpl])

  // Bind the MoonfishService client
  lazy val moonfishService = serviceClient.implement[MoonfishService]
}
