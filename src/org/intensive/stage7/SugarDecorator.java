package org.intensive.stage7;

public class SugarDecorator extends TeaDecorator {
    public SugarDecorator(Tea tea) {
        super(tea);
    }

    @Override
    public String getDescription() {
        return decoraterTea.getDescription()+" with Sugar";
    }

    @Override
    public Integer getPrice() {
        return decoraterTea.getPrice()+20;
    }
}
