package com.example.webflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class WebSocketConfiguration {

    @Bean
    Executor executor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    HandlerMapping handlerMapping(WebSocketHandler wsh) {
        return new SimpleUrlHandlerMapping() {
            {
                setUrlMap(Collections.singletonMap("/ws/profiles", wsh));
                setOrder(10);
            }
        };
    }

    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    WebSocketHandler webSocketHandler(ObjectMapper objectMapper, ProfileCreatedEventPublisher eventPublisher) {
        Flux<ProfileCreatedEvent> publish = Flux
                .create(eventPublisher)
                .share();

        return session -> {
            Flux<WebSocketMessage> messageFlux = publish
                    .map(evt -> {
                        try {
                            return objectMapper.writeValueAsString(evt.getSource());
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(session::textMessage);
            return session.send(messageFlux);
        };
    }

}
