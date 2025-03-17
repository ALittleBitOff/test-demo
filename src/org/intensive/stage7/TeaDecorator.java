package org.intensive.stage7;

public abstract class TeaDecorator implements Tea{
    protected Tea decoraterTea;

    public TeaDecorator(Tea tea) {
        this.decoraterTea = tea;
    }

    @Override
    public Integer getPrice() {
        return decoraterTea.getPrice();
    }

    @Override
    public String getDescription() {
        return decoraterTea.getDescription();
    }
}
