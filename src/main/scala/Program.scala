import akka.actor.ActorSystem
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{ConsumerSettings, ProducerMessage, ProducerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, ByteArraySerializer, StringDeserializer, StringSerializer}

object Program extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")

  val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
    .withGroupId("G1")
    .withBootstrapServers("localhost:9092")

  val consumptionDone = Consumer.committableSource(consumerSettings, Subscriptions.topics("test1"))
    .map { msg =>
      println(s"topic1 -> topic2: $msg")
      ProducerMessage.Message(new ProducerRecord[Array[Byte], String](
        "topic2",
        msg.record.value
      ), msg.committableOffset)
    }
    .runWith(Producer.commitableSink(producerSettings))

    consumptionDone.andThen {
    case _ => system.terminate()
  }

}
