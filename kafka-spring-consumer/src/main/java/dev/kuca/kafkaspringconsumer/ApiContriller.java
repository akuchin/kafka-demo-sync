package dev.kuca.kafkaspringconsumer;

import dev.kuca.kafkaspring.msg.StockReply;
import dev.kuca.kafkaspring.msg.StockState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiContriller {

    @Autowired
    StockStoreService stockStoreService;

    @ResponseBody
    @GetMapping(value = "/store/{sku}")
    public StockState stock(@PathVariable("sku") String sku) {
        return stockStoreService.getStock(sku);
    }

}
