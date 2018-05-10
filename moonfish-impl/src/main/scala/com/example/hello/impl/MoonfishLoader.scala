package com.example.hello.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.example.hello.api.MoonfishService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class MoonfishLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new MoonfishApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new MoonfishApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[MoonfishService])
}

abstract class MoonfishApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[MoonfishService](wire[MoonfishServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = MoonfishSerializerRegistry

  // Register the moonfish persistent entity
  persistentEntityRegistry.register(wire[MoonfishEntity])
}
