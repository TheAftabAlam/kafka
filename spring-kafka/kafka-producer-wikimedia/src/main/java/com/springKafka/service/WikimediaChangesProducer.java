package com.springKafka.service;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;


@Service
public class WikimediaChangesProducer {

    private static  final Logger LOGGER = LoggerFactory.getLogger(WikimediaChangesProducer.class);

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void sendMessage() throws InterruptedException {
        String topic = "wikimedia";

        //to read real time stream data from wikimedia event source
        //okhttp eventsource maven
        //to deal with json add jackson-core and json bind
        EventHandler eventHandler = new WikimediaChangesHandler(topic,kafkaTemplate);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();

        eventSource.start();

        TimeUnit.MINUTES.sleep(10);


    }

}
