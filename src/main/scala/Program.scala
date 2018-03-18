import io.prometheus.client._
import io.prometheus.client.exporter.PushGateway

object Program extends App {
  val registry = new CollectorRegistry
  val pg = new PushGateway("127.0.0.1:9091")


  private val histogram: Histogram = Histogram
    .build("experiment_histogram", "experiment histogram")
    .labelNames("experiment", "variant")
    .register(registry)


  val summary = Summary.build()
    .labelNames("experiment", "variant")
    .name("experiment_summaries")
    .help("experiment summaries")
    .register(registry);

  val duration = Gauge.build
    .name("experiment_allocations")
    .help("Experiment Allocations")
    .labelNames("experiment", "variant")
    .register(registry)


  val counter = Counter.build()
    .name("allocation_count")
    .help("allocation count")
    .labelNames("experiment", "variant")
    .register(registry);


  (1 to 100000) foreach { i =>
    Thread.sleep(20)
    val value1 = Math.floor(Math.random() * 500)
    val value2 = Math.floor(Math.random() * 1000)
    //    duration.labels("exp1111", "v2").inc(1)

    counter.labels("exp1111", "v2").inc(value1)
    counter.labels("exp1111", "v1").inc(value2)

    println(counter.labels("exp1111", "v2").get())
    //    histogram.labels("exp1111", "v2").observe(1)
    //    summary.labels("exp1111", "v2").observe(1)
    pg.pushAdd(registry, "my_batch_job")
  }
}
