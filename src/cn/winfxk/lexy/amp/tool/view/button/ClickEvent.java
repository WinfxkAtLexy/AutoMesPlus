package cn.winfxk.lexy.amp.tool.view.button;

import java.awt.event.MouseEvent;

public class ClickEvent {
    private final Button button;
    private final MouseEvent event;

    public ClickEvent(Button button, MouseEvent event) {
        this.button = button;
        this.event = event;
    }

    public MouseEvent getEvent() {
        return event;
    }

    public Button getButton() {
        return button;
    }
}
