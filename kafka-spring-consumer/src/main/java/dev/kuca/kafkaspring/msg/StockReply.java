package dev.kuca.kafkaspring.msg;

public class StockReply {

    public String sku;
    public Cmd command;
    public int value;

    public StockReply(String sku, Cmd command, int value) {
        this.sku = sku;
        this.command = command;
        this.value = value;
    }
}
