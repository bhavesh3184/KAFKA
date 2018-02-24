package org.bhavesh.stream;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;

public class StreamsStarterApp {

    public static void main(String[] args) {

        Properties  config = new Properties();

        config.put(StreamsConfig.APPLICATION_ID_CONFIG,"kafka-starter-app1");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());


        StreamsBuilder builder = new StreamsBuilder();

        KStream<String,String> wordCountInput =  builder.stream("word-count-input");

        KTable<String,Long> wordCount = wordCountInput.mapValues(x -> x.toLowerCase())
                                                      .flatMapValues(value -> Arrays.asList(value.split(" ")))
                                                      .selectKey((k,v)-> v)
                                                      .groupByKey()
                                                      .count();

        KStream<String,Long> wordcountStream = wordCount.toStream();

        wordcountStream.to("word-count-output", Produced.with(Serdes.String(),Serdes.Long()));

        final Topology topology = builder.build();

        final KafkaStreams streams = new KafkaStreams(topology, config);

        streams.start();

        System.out.println(streams.toString());

/*
        // Update:
        // print the topology every 10 seconds for learning purposes
        while(true){
            System.out.println(streams.toString());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }

*/
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }
}
