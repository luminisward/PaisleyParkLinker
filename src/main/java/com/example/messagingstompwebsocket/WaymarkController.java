package com.example.messagingstompwebsocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WaymarkController {

    private SimpMessagingTemplate template;
    private SimpUserRegistry simpUserRegistry;
    private Connections connections;


    WaymarkController(SimpMessagingTemplate simpMessagingTemplate, SimpUserRegistry simpUserRegistry, Connections connections) {
        this.template = simpMessagingTemplate;
        this.simpUserRegistry = simpUserRegistry;
        this.connections = connections;
    }

    @RequestMapping(value = "/place", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String broadcastWaymark(@RequestBody String body, HttpServletRequest request) {
        template.convertAndSend("/broadcast/waymark", new BroadcastMessage(request.getRemoteAddr(), body));
        return body;
    }

    @GetMapping("/onlineCount")
    public Integer getOnlineCount() {
        return connections.getCount();
    }


    @PostMapping("/tts")
    public void broadcaskTts(@RequestBody String body) {
        template.convertAndSend("/broadcast/tts", body);
    }
}
