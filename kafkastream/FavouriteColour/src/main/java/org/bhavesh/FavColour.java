package org.bhavesh;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;

public class FavColour {

    public static void main(String[] args) {

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG,"favourite-color");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String,String> datain = builder.stream("favcolourin");

        KStream<String, String> outputStream = datain.filter((key, value) -> value.contains(","))
                                                     .selectKey((key,value) -> value.split(",")[0].toLowerCase())
                                                     .mapValues(value -> value.split(",")[1].toLowerCase())
                                                     .filter((user, colour) -> Arrays.asList("green", "blue", "red").contains(colour));


       outputStream.to("fav-colour-int");

        KTable<String,String> distData = builder.table("fav-colour-int");


        distData.groupBy((user, colour) -> new KeyValue<>(colour, colour)).count()
                .toStream().to("fav-colour-out", Produced.with(Serdes.String(),Serdes.Long()));


        final Topology topology =  builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, config);

        //only in dev
        streams.cleanUp();

        streams.start();


        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        /*
        yahvi,blue
        bhavesh,white
        prerana,pink
        yahvi,nevy
        */







    }
}
