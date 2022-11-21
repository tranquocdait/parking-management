package com.huyendieu.parking.trigger;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

public class MongoEventListener<E> extends AbstractMongoEventListener<E> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<E> event) {
        Object source = event.getSource();
        System.out.println("Updated Collection:" + event.getCollectionName());
    }

}
