package dev.kuca.kafkaspring;

import dev.kuca.kafkaspring.msg.StockReply;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "store", url = "${feign.store.url}")
public interface StoreClient {

    @RequestMapping(method = RequestMethod.GET, value = "/store/{sku}")
    StockReply getStock(@PathVariable("sku") String sku);

}
