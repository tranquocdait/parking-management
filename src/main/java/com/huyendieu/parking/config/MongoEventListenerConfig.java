package com.huyendieu.parking.config;

import com.huyendieu.parking.trigger.MongoEventListener;
import org.springframework.context.annotation.Bean;

public class MongoEventListenerConfig {

    @Bean
    public MongoEventListener triggerMongoEventListener() {
        return new MongoEventListener();
    }
}
