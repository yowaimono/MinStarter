package com.reservoir.core.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;



@Component
public class EventPublisherUtil {

    private static ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ApplicationEventPublisher autowiredApplicationEventPublisher;

    @PostConstruct
    public void init() {
        applicationEventPublisher = autowiredApplicationEventPublisher;
    }

    public static void publishEvent(ApplicationEvent event) {
        if (applicationEventPublisher != null) {
            applicationEventPublisher.publishEvent(event);
        } else {
            throw new IllegalStateException("ApplicationEventPublisher is not initialized");
        }
    }
}