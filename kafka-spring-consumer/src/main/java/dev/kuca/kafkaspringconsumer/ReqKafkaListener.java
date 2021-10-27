package dev.kuca.kafkaspringconsumer;

import dev.kuca.kafkaspring.msg.StockReply;
import dev.kuca.kafkaspring.msg.StockRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class ReqKafkaListener {

    @Autowired
    StockStoreService stockStoreService;


    @KafkaListener(topics = "${kafka.topic.request-topic}", containerFactory = "requestListenerContainerFactory")
    @SendTo()
    public StockReply receive(StockRequest request) {
        return stockStoreService.updateStock(request);
    }

}
