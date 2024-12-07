package cn.winfxk.lexy.amp.view.setting.view.mes;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.crucial.Crucial;
import cn.winfxk.lexy.amp.crucial.CrucialItem;
import cn.winfxk.lexy.amp.crucial.CrucialThread;
import cn.winfxk.lexy.amp.tool.Config;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.button.Button;
import cn.winfxk.lexy.amp.tool.view.button.ClickEvent;
import cn.winfxk.lexy.amp.tool.view.list.ListView;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.setting.view.Meslist;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class Keylist extends MyJPanel {
    private static final int defWidth = Math.min(1000, ScreenWidth), defHeight = Math.min(800, ScreenHeight);
    private static final Font maxfFont = new Font("楷体", Font.PLAIN, 20);
    private static final Font defFont = new Font("楷体", Font.PLAIN, 15);
    private static final Color clickColor = Color.GRAY;
    private final JFrame frame = new JFrame();
    private final ButtonView buttonView;
    private final TitleView titleView;
    private final Itemlist itemlist;
    private final ItemType itemType;
    private final EditView editView;
    private final Crucial crucial;

    /**
     * 电机保存按钮时调用
     *
     * @param event
     */
    private void onClickSave(ClickEvent event) {
        if (crucial == null) {
            JOptionPane.showMessageDialog(null, "请先创建一个型号！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (titleView.item == null) {
            JOptionPane.showMessageDialog(null, "请先创建一个物料！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        titleView.item.setCode(getList(editView.area.getText()));
        titleView.item.setInput(itemType.isInput());
        crucial.save();
        JOptionPane.showMessageDialog(null, "保存完成！", "提示", JOptionPane.PLAIN_MESSAGE);
    }

    public void openItem(CrucialItem item) {
        if (item == null) {
            titleView.setitemName("");
            editView.area.setText("");
            itemType.setInput(false);
            titleView.saveItem.setEnabled(true);
            itemlist.setEnabled(true);
        } else {
            titleView.setitemName(item.getName());
            editView.area.setText(getString(item.getCode()));
            itemType.setInput(item.isInput());
            setViewEnabled(true);
        }
        titleView.item = item;
    }

    public void setViewEnabled(boolean enabled) {
        editView.setEnabled(enabled);
        buttonView.setEnabled(enabled);
        itemType.setEnabled(enabled);
        itemlist.setEnabled(enabled);
        editView.setEnabled(enabled);
        editView.setEnabled(enabled);
    }

    public Keylist(Crucial crucial) {
        super();
        this.crucial = crucial;
        frame.setTitle(crucial == null ? "新建型号" : (crucial.getModel() + "关键零部件明细"));
        frame.setIconImage(AutoMes.frame.getIconImage());
        frame.setSize(defWidth, defHeight);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, Main.ScreenHeight / 2 - frame.getHeight() / 2);
        frame.addComponentListener(this);
        frame.setContentPane(this);
        frame.setMinimumSize(new Dimension(defWidth, defHeight));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        itemlist = new Itemlist();
        add(itemlist);
        itemType = new ItemType();
        add(itemType);
        buttonView = new ButtonView();
        add(buttonView);
        editView = new EditView();
        add(editView);
    }

    public JFrame getFrame() {
        return frame;
    }

    @Override
    public void start() {
        if (!frame.isVisible()) frame.setVisible(true);
        titleView.setSize(getWidth(), Math.min(120, Math.max(getHeight() / 8, 80)));
        titleView.start();
        itemlist.setLocation(0, titleView.getY() + titleView.getHeight());
        itemlist.setSize(Math.min(200, Math.max(getWidth() / 4, 150)), getHeight() - itemlist.getY());
        itemlist.start();
        itemType.setLocation(itemlist.getX() + itemlist.getWidth() + 10, titleView.getHeight());
        itemType.setSize(getWidth() - itemType.getX(), (int) Math.min(55, Math.max(40, getHeight() / 17.8)));
        itemType.start();
        buttonView.setSize(getWidth() - (itemlist.getX() + itemlist.getWidth()), Tool.getMath(60, 40, (int) (getHeight() / 13.4)));
        buttonView.setLocation(itemlist.getX() + itemlist.getWidth(), getHeight() - buttonView.getHeight() - 10);
        buttonView.start();
        editView.setLocation(itemlist.getX() + itemlist.getWidth(), itemType.getY() + itemType.getHeight() + 10);
        editView.setSize(getWidth() - editView.getX() - 10, buttonView.getY() - editView.getY() - 30);
        editView.start();
    }

    /**
     * 将带有换行符的文本转换为List对象，每行为一个元素
     *
     * @param s 带有换行符的文本
     * @return 转换完毕的对象
     */
    public static List<String> getList(String s) {
        List<String> list = new ArrayList<>();
        String[] strings = s.split("\n");
        for (String s1 : strings) {
            if (s1 == null || s1.isEmpty()) continue;
            list.add(s1);
        }
        return list;
    }

    /**
     * 将list对象转换为一个用换行符分割的字符串
     *
     * @param list 需要转换的list
     * @return 转换为换行符分割的字符串
     */
    public static String getString(List<String> list) {
        StringBuilder s = new StringBuilder();
        for (String ss : list) {
            if (ss == null || ss.isEmpty()) continue;
            s.append((s.length() == 0) ? "" : "\n").append(ss);
        }
        return s.toString();
    }

    /**
     * 封装编辑框的类
     */
    private static class EditView extends MyJPanel {
        private static final Color color = new Color(0xee, 0xee, 0xee);
        private final JLabel label, line;
        private final JTextArea area;

        private EditView() {
            super();
            setOpaque(true);
            setBackground(color);
            label = new JLabel("以下编辑框可以输入物料编码，同时也可以输入图号，可使用换行来添加多个");
            label.setLocation(0, 0);
            label.setFont(defFont);
            label.setOpaque(false);
            add(label);
            line = new JLabel("");
            line.setOpaque(true);
            line.setBackground(Color.black);
            add(line);
            area = new JTextArea();
            area.setFont(maxfFont);
            area.setOpaque(false);
            add(area);
        }

        @Override
        public void start() {
            label.setSize(getWidth(), 50);
            line.setSize(getWidth(), 2);
            line.setLocation(label.getX(), label.getY() + label.getHeight());
            area.setLocation(line.getX(), line.getY() + line.getHeight() + 5);
            area.setSize(getWidth(), getHeight() - area.getY() - 10);
        }
    }

    /**
     * 底部按钮区的封装类
     */
    private class ButtonView extends MyJPanel {
        private final Button save, remove;

        private ButtonView() {
            super();
            save = new Button("保存");
            save.setEnabled(false);
            save.setFont(maxfFont);
            save.setOnClickListener(Keylist.this::onClickSave);
            add(save);
            remove = new Button("删除");
            remove.setFont(maxfFont);
            remove.setOnClickListener(event -> {
                if (crucial == null) {
                    JOptionPane.showMessageDialog(null, "您还未打开任何型号！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (titleView.item == null) {
                    JOptionPane.showMessageDialog(null, "您还未打开任何物料！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (JOptionPane.showConfirmDialog(null, "您确定要从关键零部件清单中删除这个物料吗？", "警告", JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION) == 0) {
                    crucial.removeItem(titleView.item.getName());
                    crucial.save();
                    itemlist.adapter.UpdateAdapter();
                }
            });
            add(remove);
        }

        @Override
        public void start() {
            int width = Tool.getMath(200, 120, getWidth() / 7);
            save.setSize(width, getHeight());
            int pp = Tool.getMath(10, 50, getWidth() / 15);
            int padding = (getWidth() - width * 2 - pp) / 2;
            save.setLocation(padding, 0);
            save.start();
            remove.setSize(save.getSize());
            remove.setLocation(save.getX() + save.getWidth() + pp, 0);
            remove.start();
        }
    }

    /**
     * 封装物料类型的类
     */
    private static class ItemType extends MyJPanel {
        private final Button input, simple;
        private boolean isInput;

        private ItemType() {
            super();
            input = new Button("进口件");
            simple = new Button("常规件");
            input.setFont(maxfFont);
            input.setOnClickListener(event -> {
                simple.setOpaque(false);
                simple.updateUI();
                input.setOpaque(true);
                input.setBackground(clickColor);
                input.updateUI();
                isInput = true;
            });
            add(input);
            simple.setFont(maxfFont);
            simple.setOpaque(true);
            simple.setBackground(clickColor);
            simple.setOnClickListener(event -> {
                input.setOpaque(false);
                input.updateUI();
                simple.setOpaque(true);
                simple.setBackground(clickColor);
                simple.updateUI();
                isInput = false;
            });
            add(simple);
        }

        /**
         * 设置物料状态
         *
         * @param input 是否是进口件
         */
        public void setInput(boolean input) {
            isInput = input;
            if (!isInput) {
                this.input.setOpaque(false);
                this.simple.setOpaque(true);
                this.simple.setBackground(clickColor);
            } else {
                simple.setOpaque(false);
                this.input.setOpaque(true);
                this.input.setBackground(clickColor);
            }
            this.input.updateUI();
            this.simple.updateUI();
        }

        /**
         * @return 判断是否是进口件
         */
        public boolean isInput() {
            return isInput;
        }

        @Override
        public void start() {
            int paddingHeight = Math.min(10, Math.max(getHeight() / 120, 3));
            int padding = Math.min(10, Math.max(getWidth() / 120, 3));
            int width = (getWidth() - padding * 4) / 2;
            input.setLocation(padding, paddingHeight);
            input.setSize(width, getHeight() - paddingHeight * 2);
            input.start();
            simple.setSize(input.getSize());
            simple.setLocation(input.getWidth() + input.getX() + padding * 2, paddingHeight);
            simple.start();
        }
    }

    /**
     * 存储当前Model的零件列表
     */
    private class Itemlist extends MyJPanel {
        private final ListView listView;
        private final Adapter adapter;
        private final Button remove;

        private Itemlist() {
            super();
            listView = new ListView(adapter = new Adapter());
            listView.setLocation(0, 0);
            add(listView);
            remove = new Button("删除型号");
            remove.setFont(defFont);
            remove.setOnClickListener(event -> {
                if (crucial == null) {
                    JOptionPane.showMessageDialog(null, "您还未打开任何型号！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (JOptionPane.showConfirmDialog(null, "您确定要从关键零部件清单中删除个这型号吗？", "警告", JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION) == 0) {
                    Config config = new Config(Main.KeylistFile);
                    config.remove(crucial.getModel()).save();
                    CrucialThread.reload();
                    Meslist.reload();
                    frame.dispose();
                }
            });
            add(remove);
        }

        @Override
        public void start() {
            remove.setSize(Math.min(80, Math.max(getWidth() - 20, 50)), 40);
            remove.setLocation(Math.max(0, getWidth() / 2 - remove.getWidth() / 2), getHeight() - remove.getHeight() - 5);
            remove.start();
            listView.setSize(getWidth(), getHeight() - remove.getHeight() - 20);
            new Thread(() -> {
                Tool.sleep(300);
                adapter.UpdateAdapter();
            }).start();
        }

        /**
         * 显示零部件清单的Itemview
         */
        private class ItemView extends BaseItemView {
            private final JLabel label;

            private ItemView(CrucialItem item) {
                super();
                label = new JLabel(item == null ? "新建物料" : item.getName(), SwingConstants.CENTER);
                label.setOpaque(false);
                label.setFont(defFont);
                add(label);
                setItemClickListener(view -> openItem(item));
            }

            @Override
            public void start() {
                label.setSize(getSize());
            }
        }

        /**
         * 显示零件清单的Adapter
         */
        private class Adapter extends BaseAdapter {

            @Override
            public int getSize() {
                return (crucial == null ? 0 : crucial.getAllItems().size()) + 1;
            }

            @Override
            public CrucialItem getItem(int location) {
                return location == 0 ? null : crucial.getAllItems().get(location - 1);
            }

            @Override
            public ItemView getView(int location) {
                return new ItemView(getItem(location));
            }
        }
    }

    /**
     * 标题视图类
     */
    private class TitleView extends MyJPanel implements DocumentListener {
        private final JTextField modelName;
        private final JTextField itemName;
        private final JLabel itemLabel;
        private final JLabel modelLabel;
        private final Button openModel;
        private final Button saveModel;
        private final Button saveItem;
        private CrucialItem item;
        private String itemname;

        public void setitemName(String text) {
            itemName.setText(text == null ? "" : text);
            itemname = text == null ? "" : text;
        }

        private TitleView() {
            super();
            modelLabel = new JLabel("型号: ", SwingConstants.CENTER);
            modelLabel.setLocation(0, 0);
            modelLabel.setFont(defFont);
            add(modelLabel);
            modelName = new JTextField(crucial == null ? "" : crucial.getModel());
            modelName.setFont(defFont);
            modelName.getDocument().addDocumentListener(this);
            add(modelName);
            openModel = new Button("打开");
            openModel.setFont(defFont);
            openModel.setEnabled(false);
            openModel.setOnClickListener(event -> onOpenModel());
            add(openModel);
            saveModel = new Button("保存");
            saveModel.setFont(defFont);
            saveModel.setEnabled(false);
            saveModel.setOnClickListener(event -> onSaveMoel());
            add(saveModel);
            itemLabel = new JLabel("物料: ", SwingConstants.CENTER);
            itemLabel.setFont(defFont);
            add(itemLabel);
            itemName = new JTextField("");
            itemName.setFont(defFont);
            itemName.getDocument().addDocumentListener(new EditListener());
            add(itemName);
            saveItem = new Button("保存");
            saveItem.setFont(defFont);
            saveItem.setEnabled(false);
            saveItem.setOnClickListener(event -> onSaveItem());
            add(saveItem);
        }

        @Override
        public void start() {
            int width = Math.min(80, Math.max(50, getWidth() / 5));
            modelLabel.setSize(width, getHeight() / 2);
            saveModel.setSize(modelLabel.getSize());
            saveModel.setLocation(getWidth() - 10 - saveModel.getWidth(), 0);
            saveModel.start();
            openModel.setSize(modelLabel.getSize());
            openModel.setLocation(saveModel.getX() - openModel.getWidth() - 5, saveModel.getY());
            openModel.start();
            modelName.setLocation(modelLabel.getWidth() + 5, 0);
            modelName.setSize(openModel.getX() - modelName.getX(), modelLabel.getHeight());
            itemLabel.setSize(modelLabel.getSize());
            itemLabel.setLocation(modelLabel.getX(), modelLabel.getY() + modelLabel.getHeight());
            saveItem.setSize(itemLabel.getSize());
            saveItem.setLocation(getWidth() - saveItem.getWidth() - 10, itemLabel.getY());
            saveItem.start();
            itemName.setLocation(modelName.getX(), itemLabel.getY());
            itemName.setSize(saveItem.getX() - itemName.getX(), itemLabel.getY());
        }

        /**
         * 点击保存物料名称时调用
         */
        private void onSaveItem() {
            String string = itemName.getText();
            if (string == null || string.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入新的的零部件名称！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (itemname != null && itemname.equals(string)) {
                JOptionPane.showMessageDialog(null, "新零部件名称和旧零部件名称一致！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (crucial == null) {
                JOptionPane.showMessageDialog(null, "请先创建关键零部件后在添加物料！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            setitemName(string);
            if (item == null) {
                item = new CrucialItem(string, crucial, false, new ArrayList<>());
                crucial.addItem(item);
                Log.i("在" + crucial.getModel() + "内创建了一个新的关键物料" + item.getName());
            } else {
                Log.i("已将" + item.getName() + "更名为" + itemname);
                item.setName(itemname);
            }
            crucial.save();
            itemlist.adapter.UpdateAdapter();
        }

        /**
         * 点击保存型号按钮时调用
         */
        private void onSaveMoel() {
            String string = modelName.getText();
            if (string == null || string.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入新的的型号名称！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (crucial != null && crucial.getModel().equals(string)) {
                JOptionPane.showMessageDialog(null, "新型号名称和旧型号名称一致！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Config config = new Config(Main.KeylistFile);
            CrucialThread.reload();
            if (crucial != null) config.remove(crucial.getModel()).save();
            config.set(string, crucial == null ? new HashMap<>() : crucial.getMap()).save();
            Meslist.reload();
            Log.i("已" + (crucial == null ? "新建一个关键零部件清单。" : "将" + crucial.getModel() + "更名为" + string));
            Crucial crucial1 = CrucialThread.getCrucial(string);
            if (crucial1 == null) {
                JOptionPane.showMessageDialog(null, "无法打开已保存的关键零部件清单！请检查系统是否出现异常！", "错误", JOptionPane.ERROR_MESSAGE);
                Log.e("已" + (crucial == null ? "新建一个关键零部件清单" : "将" + crucial.getModel() + "更名为" + string + "，但是重新从数据库读取。"));
            } else new Keylist(crucial1).start();
            frame.dispose();
        }

        /**
         * 点击打开型号按钮时调用
         */
        private void onOpenModel() {
            String string = modelName.getText();
            if (string == null || string.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入想要打开的型号的名称！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (crucial != null && crucial.getModel().equals(string)) {
                JOptionPane.showMessageDialog(null, "想要打开的型号就是当前已打开的型号！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Crucial crucial = CrucialThread.getCrucial(string);
            if (crucial == null) {
                JOptionPane.showMessageDialog(null, "想要打开的信号不存在！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new Keylist(crucial).start();
            frame.dispose();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            reload();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            reload();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            reload();
        }

        /**
         * 但存放型号名称的编辑框被改变内容时调用
         */
        private void reload() {
            String string = modelName.getText();
            boolean isEnabled = string != null && !string.isEmpty() && (crucial == null || string.equals(crucial.getModel()));
            Keylist.this.setViewEnabled(isEnabled);
            itemName.setEnabled(isEnabled);
            saveItem.setEnabled(isEnabled);
            boolean button = string != null && !string.isEmpty() && (crucial == null || !string.equals(crucial.getModel()));
            saveModel.setEnabled(button);
            openModel.setEnabled(button);
        }

        /**
         * 用来实现物料名称编辑框改变事件
         */
        private class EditListener implements DocumentListener {
            /**
             * 但存放物料名称的编辑框被改变内容时调用
             */
            private void reload() {
                String string = itemName.getText();
                boolean isEnabled = string != null && !string.isEmpty() && (itemname == null || itemname.isEmpty() || string.equals(itemname));
                saveItem.setEnabled(isEnabled);
                Keylist.this.setViewEnabled(isEnabled);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                reload();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                reload();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                reload();
            }
        }
    }
}
