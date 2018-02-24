import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;
import   org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.utils.*;
import sun.security.provider.VerificationProvider;

public class kafkaProducerDemo {

    public static void main(String args[])
    {
        Properties  properties = new Properties();
        //kafka-console-producer.sh --broker-list nn01.itversity.com:6667,nn02.itversity.com:6667,rm01.itversity.com:6667   --topic patel111

        //kafka bootstrap server
        properties.setProperty("bootstrap.servers", "nn01.itversity.com:6667,nn02.itversity.com:6667,rm01.itversity.com:6667");

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
       //producer ack
        properties.setProperty("acks", "1");

        properties.setProperty("retries", "3");
        properties.setProperty("linger.ms", "1");


        Producer<String,String> producer = new org.apache.kafka.clients.producer.KafkaProducer<String,String>(properties);

        for (int i =0;i<10;i++) {
            ProducerRecord<String, String> produceRecord = new ProducerRecord<String, String>("pate1",Integer.toString(i), "hello with key "+ i);
            producer.send(produceRecord);
        }
        producer.flush();
        producer.close();
        
        System.out.print("completed");
    }


}


