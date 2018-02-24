import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.util.Arrays;
import java.util.Properties;

public class kafkaConsumerDemo {

    public static void main(String args[])
    {
        Properties  properties = new Properties();
        //kafka-console-consumer.sh --zookeeper nn01.itversity.com:2181,nn02.itversity.com:2181,rm01.itversity.com:2181 --bootstrap-server nn01.itversity.com:6667,nn02.itversity.com:6667,rm01.itversity.com:6667 --topic patel111 --from-beginning
        //kafka bootstrap server
        properties.setProperty("bootstrap.servers", "nn01.itversity.com:6667,nn02.itversity.com:6667,rm01.itversity.com:6667");

        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", "test");

        properties.setProperty("enable.auto.commit", "true");

        properties.setProperty("enable.commit.interval.ms", "1000");
        properties.setProperty("auto.offset.reset", "earliest");

        KafkaConsumer<String,String>  kafkaConsumer = new KafkaConsumer<String, String>(properties);
        kafkaConsumer.subscribe(Arrays.asList("pate1"));

        while(true)
        {
           ConsumerRecords<String,String> consumerRecords =  kafkaConsumer.poll(1000);
            for (ConsumerRecord<String,String> consumerRecord  : consumerRecords )
            {
                /*System.out.println(consumerRecord.partition());
                System.out.println(consumerRecord.offset());
                System.out.println( consumerRecord.value());
                System.out.println(consumerRecord.topic());
                System.out.println(consumerRecord.timestamp());*/
                //System.out.println("==========================");

                System.out.println("Partition :" + consumerRecord.partition() +
                                    " Offset :" + consumerRecord.offset() +
                                    " Key :" + consumerRecord.key() +
                                    " Value :" + consumerRecord.value()
                                    );
            }

        }










    }


}
