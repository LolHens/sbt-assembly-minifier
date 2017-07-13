package org.lolhens.sbt.assemblyminifier.filters

import org.lolhens.sbt.assemblyminifier.Filter
import org.lolhens.sbt.assemblyminifier.Rule.{Class, Subclasses}

/**
  * Created by pierr on 12.07.2017.
  */
object Akka {
  val filter: Filter = Filter.Module("com.typesafe.akka", "akka-actor")(
    Subclasses(Class("akka.dispatch.ExecutorServiceConfigurator")),
    Subclasses(Class("akka.dispatch.MessageDispatcherConfigurator")),
    Subclasses(Class("akka.remote.RemoteTransport")),
    Subclasses(Class("akka.actor.Actor", clazz = false)),
    Subclasses(Class("akka.actor.ActorRefProvider", clazz = false)),
    Subclasses(Class("akka.actor.ExtensionId", clazz = false)),
    Subclasses(Class("akka.actor.ExtensionIdProvider", clazz = false)),
    Subclasses(Class("akka.actor.SupervisorStrategyConfigurator", clazz = false)),
    Subclasses(Class("akka.dispatch.MailboxType", clazz = false)),
    Subclasses(Class("akka.routing.RouterConfig", clazz = false)),
    Subclasses(Class("akka.serialization.Serializer", clazz = false)),
    Class("akka.*.*MessageQueueSemantics"),
    Class("akka.actor.LightArrayRevolverScheduler"),
    Class("akka.actor.LocalActorRefProvider"),
    Class("akka.actor.SerializedActorRef"),
    Class("akka.dispatch.MultipleConsumerSemantics"),
    Class("akka.event.Logging$LogExt"),
    Class("akka.event.Logging*"),
    Class("akka.remote.DaemonMsgCreate"),
    Class("akka.routing.ConsistentHashingPool"),
    Class("akka.routing.RoutedActorCell$RouterActorCreator"),
    Class("akka.event.DefaultLoggingFilter")
  )
}
