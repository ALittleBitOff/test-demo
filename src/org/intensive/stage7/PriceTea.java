package org.intensive.stage7;

public class PriceTea implements Tea {
    @Override
    public Integer getPrice() {
        return 60;
    }

    @Override
    public String getDescription() {
        return "black tea";
    }
}
