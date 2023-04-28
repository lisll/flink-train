package com.datapipeline;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Info;
import io.prometheus.client.Info.Builder;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.exporter.PushGateway;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.util.hash.Hash;

public class PushgatewayDemo {
  public static String url = "49.232.214.94:9091";

  public static void main(String[] args) throws Exception{
    //    push_gauge();
//    exampleExporter();
//    push_counter();
    push_Info();
  }

  private static void push_gauge() {
    try {
      CollectorRegistry registry = new CollectorRegistry();
      Gauge guage =
          Gauge.build("my_custom_metric", "This is my custom metric.")
              .labelNames("app", "date", "sum")
              .create();
      String date = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date());
      guage.labels("my-pushgateway-test-0", date).set(25);
      guage.labels("my-pushgateway-test-1", date, "90").dec();
      guage.labels("my-pushgateway-test-2", date, "0").dec(2);
      guage.labels("my-pushgateway-test-3", date, "89").inc();
      guage.labels("my-pushgateway-test-4", date, "78").inc(5);
      Collector register = guage.register(registry);
      PushGateway pg = new PushGateway(url);
      Map<String, String> groupingKey = new HashMap<>(); // 这个是可选的
      groupingKey.put("instance", "date_instance");
      pg.pushAdd(registry, "date_job", groupingKey);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void push_counter() {
    try {
      CollectorRegistry registry = new CollectorRegistry();
      Counter counter =
          Counter.build()
              .name("my_counter")
              .help("this is a counter")
              .labelNames("counter1", "counter2")
              .create();
      counter.labels("push-counter1", "push-counter2").inc(1.9);
      counter.labels("push-counter11", "push-counter22").inc(1.5);
      counter.labels("push-counter111", "push-counter222").inc();
      counter.register(registry);
      PushGateway pushGateway = new PushGateway(url);
      Map<String, String> groupingKey = new HashMap<>();
      groupingKey.put("instance", "counter_instance");
      pushGateway.push(registry, "counter_job", groupingKey);
    } catch (Exception ex) {
      System.out.println(ex);
    }
  }

  private static void push_Info() throws IOException {
    CollectorRegistry registry = new CollectorRegistry();
    Info info = Info.build("my_info", "this is info").labelNames("info1").create();
    info.labels("push-info1").info("this is info1","rr");
//    info.labels("push-info11").info("this is info11");
//    info.labels("push-info111").info("this is info111");
    info.register(registry);
    PushGateway pushGateway = new PushGateway(url);
    HashMap<String, String> groupingKey = new HashMap<>();
    groupingKey.put("instance","info_instance");
    pushGateway.push(registry,"info_job",groupingKey);
  }

  private static void exampleExporter() {
    Gauge gauge = Gauge.build("gauge", "blah").register();
    Counter counter = Counter.build("counter", "meh").register();
    Summary summary = Summary.build("summary", "meh").register();
    Histogram histogram = Histogram.build("histogram", "meh").register();
    Gauge l = Gauge.build().name("labels").help("blah").labelNames("l").register();
    try {
      HTTPServer httpServer = new HTTPServer(1234);
      gauge.set(1);
      counter.inc(2);
      summary.observe(3);
      histogram.observe(4);
      l.labels("foo").inc(5);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
