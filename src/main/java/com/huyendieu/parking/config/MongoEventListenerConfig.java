package com.huyendieu.parking.config;

import org.springframework.context.annotation.Bean;

import com.huyendieu.parking.trigger.MongoEventListener;

public class MongoEventListenerConfig {

    @Bean
    public MongoEventListener triggerMongoEventListener() {
        return new MongoEventListener();
    }
}
