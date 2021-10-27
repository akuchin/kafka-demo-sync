package dev.kuca.kafkaspring.msg;

public class StockState {

    public String sku;
    public boolean instock;
    public int available;

    public StockState(String sku, boolean instock, int available) {
        this.sku = sku;
        this.instock = instock;
        this.available = available;
    }
}
