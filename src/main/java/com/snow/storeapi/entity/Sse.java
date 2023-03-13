package com.snow.storeapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Data
@NoArgsConstructor
public class Sse {
    private String clientId;
    private long timestamp;
    private SseEmitter sseEmitter;
    private String serverId;
}
