package dev.kuca.kafkaspring;

import dev.kuca.kafkaspring.msg.StockReply;
import dev.kuca.kafkaspring.msg.StockRequest;
import dev.kuca.kafkaspring.msg.StockState;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class ApiController {

    
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${kafka.topic.request-topic}")
    private String requestTopic;

    @Value("${kafka.topic.reply-topic}")
    private String replyTopic;

    @Autowired
    private ReplyingKafkaTemplate<String, StockRequest, StockReply> requestReplyKafkaTemplate;

    @ResponseBody
    @PostMapping(value = "/update/{sku}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public StockReply reserve(@PathVariable("sku") String sku, @RequestBody StockRequest request) throws InterruptedException, ExecutionException {
        ProducerRecord<String, StockRequest> record = new ProducerRecord<>(requestTopic, request);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));

        ConsumerRecord<String, StockReply> rec = requestReplyKafkaTemplate.sendAndReceive(record).get();
        if (rec.value().value < 0) {
            LOG.info("!!!!!!!! {}: {}",rec.value().sku, rec.value().value);
        }
        return rec.value();
    }


    @ResponseBody
    @GetMapping(value = "/store/{sku}")
    public StockState stock(@PathVariable("sku") String sku) {
        var restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://localhost:8089/store/"+sku, StockState.class);
    }

}
