package com.example.messagingstompwebsocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private SimpMessagingTemplate template;

    private SimpUserRegistry simpUserRegistry;

    private Connections connections;

    public WebSocketEventListener(SimpMessagingTemplate template, SimpUserRegistry simpUserRegistry, Connections connections) {
        this.template = template;
        this.simpUserRegistry = simpUserRegistry;
        this.connections = connections;
    }

    @EventListener
    public void SessionConnectedEvent(SessionConnectedEvent event) {
        connections.increase();
        template.convertAndSend("/broadcast/onlineCount",  connections.getCount() );
    }

    @EventListener
    public void Session(SessionDisconnectEvent event) {
        connections.decrease();
        template.convertAndSend("/broadcast/onlineCount",connections.getCount());
    }
}
