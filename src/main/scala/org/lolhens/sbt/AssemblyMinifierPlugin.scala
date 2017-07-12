package org.lolhens.sbt

import com.typesafe.sbt.SbtProguard
import sbt.Keys.{javaOptions, mainClass}
import sbt.{AutoPlugin, Plugins, TaskKey, _}
import sbtassembly.AssemblyPlugin

/**
  * Created by pierr on 12.07.2017.
  */
object AssemblyMinifierPlugin extends AutoPlugin {

  object autoImport {
    val minifyAssembly: TaskKey[File] = TaskKey[File]("minifyAssembly")
  }

  override def requires: Plugins = AssemblyPlugin

  import AssemblyPlugin.autoImport._
  import SbtProguard._
  import autoImport._

  override lazy val projectSettings: Seq[Setting[_]] = proguardSettings ++ Seq[Setting[_]](
    ProguardKeys.proguardVersion in Proguard := "5.3.3",
    javaOptions in (Proguard, ProguardKeys.proguard) := Seq("-Xmx2G"),

    ProguardKeys.options ++= (mainClass in Compile).value.map(mainClass => ProguardOptions.keepMain(mainClass)).toList,

    ProguardKeys.inputs := Seq((assemblyOutputPath in assembly).value),

    ProguardKeys.inputFilter in Proguard := (_ => None),
    ProguardKeys.libraries in Proguard := Seq(file("<java.home>/lib/rt.jar")),
    ProguardKeys.merge in Proguard := false,

    (ProguardKeys.options in Proguard) ++= {
      val settings = Seq(
        "dontnote", "dontwarn", "ignorewarnings",
        "dontobfuscate",
        "dontoptimize",
        "keepattributes Signature, *Annotation*",
        "keepclassmembers class * {** MODULE$;}"
      )

      val excluded: Seq[String] = {
        val scala_2_12 = Seq(
          "scala.Symbol"
        )

        val akka = Seq(
          "* extends akka.dispatch.ExecutorServiceConfigurator",
          "* extends akka.dispatch.MessageDispatcherConfigurator",
          "* extends akka.remote.RemoteTransport",
          "* implements akka.actor.Actor",
          "* implements akka.actor.ActorRefProvider",
          "* implements akka.actor.ExtensionId",
          "* implements akka.actor.ExtensionIdProvider",
          "* implements akka.actor.SupervisorStrategyConfigurator",
          "* implements akka.dispatch.MailboxType",
          "* implements akka.routing.RouterConfig",
          "* implements akka.serialization.Serializer",
          "akka.*.*MessageQueueSemantics",
          "akka.actor.LightArrayRevolverScheduler",
          "akka.actor.LocalActorRefProvider",
          "akka.actor.SerializedActorRef",
          "akka.dispatch.MultipleConsumerSemantics",
          "akka.event.Logging$LogExt",
          "akka.event.Logging*",
          "akka.remote.DaemonMsgCreate",
          "akka.routing.ConsistentHashingPool",
          "akka.routing.RoutedActorCell$RouterActorCreator",
          "akka.event.DefaultLoggingFilter"
        )

        val sql = Seq(
          "* implements java.sql.Driver",
          "com.mysql.cj.core.**",
          "com.mysql.cj.api.**"
        )

        Seq(scala_2_12, akka, sql).flatten
      }

      settings.map(setting => s"-$setting") ++
        excluded.flatMap { clazz =>
          List(clazz) ++
            List(clazz)
              .filter(_.contains(" extends "))
              .map(_.replaceAllLiterally(" extends ", " implements "))
        }
          .flatMap(clazz => List(
            s"-keep class $clazz {*;}",
            s"-keep interface $clazz {*;}"
          ))
    },

    (ProguardKeys.proguard in Proguard) := (ProguardKeys.proguard in Proguard).dependsOn(assembly).value,

    minifyAssembly := (ProguardKeys.proguard in Proguard).value.head
  )
}
