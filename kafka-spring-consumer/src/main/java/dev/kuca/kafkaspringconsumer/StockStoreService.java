package dev.kuca.kafkaspringconsumer;

import dev.kuca.kafkaspring.msg.Cmd;
import dev.kuca.kafkaspring.msg.StockReply;
import dev.kuca.kafkaspring.msg.StockRequest;
import dev.kuca.kafkaspring.msg.StockState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static dev.kuca.kafkaspring.msg.Cmd.RESERVE;
import static dev.kuca.kafkaspring.msg.Cmd.SET;

@Component
public class StockStoreService {

    Map<String, Integer> store = new HashMap<>();


    public StockState getStock(String sku) {
        store.putIfAbsent(sku, 0);

        return new StockState(sku, store.get(sku) > 0, store.get(sku));
    }

    public StockReply updateStock(StockRequest request) {
        String sku = request.sku;
        int value = request.value;
        Cmd command = request.command;

        store.putIfAbsent(sku, 0);

        int curr = store.get(sku);
        if (curr < 0)
            throw new RuntimeException("cant be less then zero" + sku + " --- " + curr);

        switch (command) {
            case RESERVE:
                return reserveStock(sku, value, curr);
            case RELEASE:
                return releaseStock(sku, value, curr);
            case SET:
                return setStock(sku, value);
            default:
                throw new IllegalStateException("Unexpected value: " + command);
        }
    }

    private StockReply releaseStock(String sku, int value, int curr) {
        store.put(sku, value + curr);
        return new StockReply(sku, SET, value);
    }

    private StockReply setStock(String sku, int value) {
        store.put(sku, value);
        return new StockReply(sku, SET, value);
    }

    private StockReply reserveStock(String sku, int value, int curr) {
        if (curr < value) {
            store.put(sku, 0);
            return new StockReply(sku, RESERVE, curr);
        } else {
            store.put(sku, curr - value);
            return new StockReply(sku, RESERVE, value);
        }
    }
}
