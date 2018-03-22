package br.com.danielpadua.crstracker.model.widget;

/**
 * Created by danielpadua on 16/03/2018.
 */

public class WidgetInfoOption {
    private int order;
    private InfoOption infoOption;

    public WidgetInfoOption() {
    }

    public WidgetInfoOption(int order, InfoOption infoOption) {
        setOrder(order);
        setInfoOption(infoOption);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public InfoOption getInfoOption() {
        return infoOption;
    }

    public void setInfoOption(InfoOption infoOption) {
        this.infoOption = infoOption;
    }
}
