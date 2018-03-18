import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

import scala.concurrent.duration.Duration

object ProducerProgram extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")


  val done = Source.repeat(Math.random())
    .delay(Duration.apply(10, TimeUnit.MILLISECONDS))
    .map(_.toString)
    .map { elem =>
      println(s"$elem added to test1")
      new ProducerRecord[Array[Byte], String]("test1", elem)
    }
    .runWith(Producer.plainSink(producerSettings))

  Producer.plainSink(producerSettings,producerSettings.createKafkaProducer())

  done.andThen {
    case _ => system.terminate()
  }

}
