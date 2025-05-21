package net.bouraoui.technician.kafka;

import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumerService {

    private final String topic;
    private final String groupId;

    public KafkaConsumerService(String topic, String groupId) {
        this.topic = topic;
        this.groupId = groupId;
    }

    @KafkaListener(topics = "#{__listener.topic}", groupId = "#{__listener.groupId}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(Object message) {
        System.out.println("Consumed from [" + topic + "] in group [" + groupId + "]: " + message);
    }

    public String getTopic() {
        return topic;
    }

    public String getGroupId() {
        return groupId;
    }
}
