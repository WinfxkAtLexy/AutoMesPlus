package cn.winfxk.lexy.amp.view.setting.view.setting;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import javax.swing.*;

public abstract class BaseView extends MyJPanel {
    protected final JTextField edit;
    protected final JLabel label;
    protected final Entry entry;
    protected final Panel main;

    public BaseView(Panel main) {
        super();
        this.main = main;
        entry = new Entry(getKey(), getValue());
        label = main.getLabel(getHint());
        label.setLocation(0, 0);
        label.setToolTipText(getToolTipHint());
        add(label);
        edit = main.getJTextField();
        edit.setText(entry.getValueToString());
        edit.setToolTipText(getToolTipHint());
        add(edit);
        setToolTipText(getToolTipHint());
    }

    public abstract String getToolTipHint();

    /**
     * @return 从配置文件读取的值
     */
    public abstract Object getValue();

    /**
     * @return 读取配置文件的Key
     */
    public abstract String getKey();

    /**
     * @return label将会显示的文本
     */
    public abstract String getHint();

    @Override
    public void start() {
        label.setSize(main.getLabelWidth(), getHeight());
        edit.setLocation(label.getWidth(), 0);
        edit.setSize(getWidth() - label.getWidth(), getHeight());
    }

    public String getText() {
        return edit.getText();
    }

    public Entry getEntry() {
        entry.setValue(edit.getText());
        return entry;
    }
}
