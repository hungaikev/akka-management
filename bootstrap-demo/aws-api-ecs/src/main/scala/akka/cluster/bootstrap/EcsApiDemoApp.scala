/*
 * Copyright (C) 2017-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.cluster.bootstrap

import java.net.InetAddress

import akka.actor.ActorSystem
import akka.discovery.awsapi.ecs.AsyncEcsServiceDiscovery
import akka.management.AkkaManagement
import akka.management.cluster.bootstrap.ClusterBootstrap
import com.typesafe.config.ConfigFactory

object EcsApiDemoApp {

  def main(args: Array[String]): Unit = {
    val privateAddress = getPrivateAddressOrExit
    val config = ConfigFactory
      .systemProperties()
      .withFallback(
        ConfigFactory.parseString(s"""
             |akka {
             |  actor.provider = "cluster"
             |  management {
             |    cluster.bootstrap.contact-point.fallback-port = 8558
             |    http.hostname = "${privateAddress.getHostAddress}"
             |  }
             |  discovery.method = aws-api-ecs-async
             |  remote.netty.tcp.hostname = "${privateAddress.getHostAddress}"
             |}
           """.stripMargin)
      )
    val system = ActorSystem("ecsBootstrapDemoApp", config)
    AkkaManagement(system).start()
    ClusterBootstrap(system).start()
  }

  private[this] def getPrivateAddressOrExit: InetAddress =
    AsyncEcsServiceDiscovery.getContainerAddress match {
      case Left(error) =>
        System.err.println(s"$error Halting.")
        sys.exit(1)

      case Right(value) =>
        value
    }

}
