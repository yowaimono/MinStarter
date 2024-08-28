package com.reservoir.core.task;

import com.reservoir.core.utils.MinMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Component
@Slf4j
public class SpringEventProcessor {

    @EventListener
    public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
        log.info("Context Refreshed Event Received: {}", event);
        MinMap.set("start","true",100000L);
    }

    @EventListener
    public void handleContextStartedEvent(ContextStartedEvent event) {
        log.info("Context Started Event Received: {}", event);
    }

    @EventListener
    public void handleContextStoppedEvent(ContextStoppedEvent event) {
        log.info("Context Stopped Event Received: {}", event);
    }

    @EventListener
    public void handleContextClosedEvent(ContextClosedEvent event) {
        log.info("Context Closed Event Received: {}", event);
    }

    @EventListener
    public void handleRequestHandledEvent(RequestHandledEvent event) {
        log.info("Request Handled Event Received: {}", event);
    }

    @EventListener
    public void handleServletRequestHandledEvent(ServletRequestHandledEvent event) {
        log.info("Servlet Request Handled Event Received: {}", event);
    }

}