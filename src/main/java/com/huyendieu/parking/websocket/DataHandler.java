package com.huyendieu.parking.websocket;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DataHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException, InterruptedException {
        log.info("Test message {}", message.toString());
        session.sendMessage( new TextMessage("Hello world"));
        Thread.sleep(1000);
    }
}
