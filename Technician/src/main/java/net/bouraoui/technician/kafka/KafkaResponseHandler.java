package net.bouraoui.technician.kafka;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class KafkaResponseHandler {

    private final Map<String, CompletableFuture<Object>> pendingResponses = new ConcurrentHashMap<>();

    public void register(String correlationId) {
        pendingResponses.put(correlationId, new CompletableFuture<>());
    }

    public void complete(String correlationId, Object response) {
        CompletableFuture<Object> future = pendingResponses.remove(correlationId);
        if (future != null) {
            future.complete(response);
        }
    }

    public Object waitForResponse(String correlationId, long timeoutMs) throws Exception {
        CompletableFuture<Object> future = pendingResponses.get(correlationId);
        if (future != null) {
            return future.get(timeoutMs, TimeUnit.MILLISECONDS);
        }
        throw new TimeoutException("No response received for correlationId: " + correlationId);
    }
}

