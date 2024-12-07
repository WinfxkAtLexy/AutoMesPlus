package cn.winfxk.lexy.amp.view.main.view.task;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.crucial.CrucialItem;
import cn.winfxk.lexy.amp.crucial.Inspectionl;
import cn.winfxk.lexy.amp.excel.factory.Task;
import cn.winfxk.lexy.amp.mes.MesItem;
import cn.winfxk.lexy.amp.tool.Config;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;
import cn.winfxk.lexy.amp.view.main.Panel;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.view.setting.view.mes.Keylist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskItemView extends BaseItemView {
    public final static Font buttonFont = new Font("楷体", Font.BOLD, 20);
    public final static Font TitleFont = new Font("楷体", Font.BOLD, 16);
    private final static Color unkownColor = new Color(0xea, 0xea, 0xea);
    private final JButton S1500, Model, OkCount, NgCount, AllCount, perature;
    private final Inspectionl inspectionl;
    private final Task task;
    public static ItemData itemData;

    public TaskItemView(Task task, List<MesItem> list) {
        super();
        if (task == null) {
            inspectionl = null;
            this.task = null;
            S1500 = new JButton("订单号");
            S1500.setLocation(0, 0);
            Model = new JButton("型号");
            OkCount = new JButton("已检数");
            OkCount.setFont(TitleFont);
            NgCount = new JButton("未检数");
            NgCount.setFont(TitleFont);
            NgCount.setBackground(Panel.Background);
            AllCount = new JButton("应检数");
            AllCount.setFont(TitleFont);
            AllCount.setBackground(Panel.Background);
            perature = new JButton("完成率");
            perature.setFont(TitleFont);
            perature.setBackground(Panel.Background);
        } else {
            inspectionl = new Inspectionl(task, list);
            inspectionl.start();
            this.task = task;
            S1500 = new JButton(task.S1500);
            S1500.setLocation(0, 0);
            S1500.addActionListener(this::onClickS1500);
            Model = new JButton(task.Model);
            Model.addActionListener(this::onClickModel);
            OkCount = new JButton(Tool.objToString(inspectionl.getOkItem().size()));
            if (!inspectionl.getOkItem().isEmpty()) OkCount.addActionListener(this::onCLickOkItem);
            NgCount = new JButton(Tool.objToString(inspectionl.getNgItem().size()));
            NgCount.setBackground(!inspectionl.getNgItem().isEmpty() ? Color.yellow : inspectionl.getCrucial() == null ? unkownColor : Panel.Background);
            if (!inspectionl.getNgItem().isEmpty()) NgCount.addActionListener(this::onClickNGItem);
            int allCount = inspectionl.getCrucial() == null ? 0 : inspectionl.getCrucial().getAllItems().size();
            AllCount = new JButton(allCount + "");
            AllCount.setBackground(Panel.Background);
            AllCount.addActionListener(this::onCLickAll);
            perature = new JButton(allCount == 0 ? "/" : getPer(allCount, inspectionl.getOkItem().size()));
            perature.setBackground(inspectionl.getNgItem().isEmpty() ? Panel.Background : Color.yellow);
            NgCount.setFont(buttonFont);
            AllCount.setFont(buttonFont);
            OkCount.setFont(buttonFont);
            perature.setFont(TitleFont);
        }
        S1500.setFont(buttonFont);
        S1500.setBackground(Panel.Background);
        add(S1500);
        Model.setFont(buttonFont);
        Model.setBackground(Panel.Background);
        add(Model);
        OkCount.setBackground(Panel.Background);
        add(OkCount);
        add(NgCount);
        add(AllCount);
        add(perature);
    }

    private void onCLickAll(ActionEvent actionEvent) {
        if (inspectionl.getCrucial() == null) return;
        final Config config = new Config(Main.IgnoreFile);
        String modelKey = task.S1500 + " / " + task.Model;
        Map<String, ItemClickListener> map = new HashMap<>();
        for (CrucialItem item : inspectionl.getCrucial().getAllItems()) {
            String itemKey = modelKey + " " + item.getName();
            map.put(item.getName() + (config.getBoolean(itemKey) ? "(已忽略)" : ""), view -> {
                if (config.getBoolean(itemKey)) {
                    int confirm = JOptionPane.showConfirmDialog(null, "您想要取消忽略这个物料的覆盖率检查吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (Main.isIgnore()) {
                        if (confirm == 0) {
                            config.set(itemKey, false).save();
                            JOptionPane.showMessageDialog(null, "操作完成！已成功取消F忽略物料: " + item.getName());
                            Log.i("已执行取消忽略操作！型号：" + task.Model + "] 订单号：[" + task.S1500 + "] 物料名称：[" + item.getName() + "]");
                        }
                    } else
                        JOptionPane.showMessageDialog(null, "您没有忽略物料检验的权限！", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    int confirm = JOptionPane.showConfirmDialog(null, "您想要忽略这个物料的覆盖率检查吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (!Main.isIgnore()) {
                        JOptionPane.showMessageDialog(null, "您没有忽略物料检验的权限！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (confirm == 0) {
                        config.set(itemKey, true).save();
                        Log.i("已执行忽略操作！型号：" + task.Model + "] 订单号：[" + task.S1500 + "] 物料名称：[" + item.getName() + "]");
                        JOptionPane.showMessageDialog(null, "操作完成！已成功忽略物料: " + item.getName());
                    }
                }
            });
        }
        new TipView(map, task.Model + "的所有关键零部件").start();
    }

    public static String getPer(int all, int ok) {
        return (int) ((double) ok / all * 100) + "%";
    }

    public JButton getS1500() {
        return S1500;
    }

    /**
     * @return 返回对应的滚动计划
     */
    public Task getTask() {
        return task;
    }

    /**
     * 点击订单号按钮时会触发的操作F
     *
     * @param actionEvent
     */
    //todo
    private void onClickS1500(ActionEvent actionEvent) {
        new Keylist(inspectionl.getCrucial()).start();
    }

    /**
     * 当点击型号按钮时会触发的操作
     *
     * @param actionEvent
     */
    private void onClickModel(ActionEvent actionEvent) {
        if (inspectionl.getTaskMesItems().isEmpty()) return;
        Map<String, ItemClickListener> map = new HashMap<>();
        for (MesItem mesItem : inspectionl.getTaskMesItems()) {
            map.put(mesItem.getName(), view -> JOptionPane.showMessageDialog(null, "订单号：" + mesItem.getS1500()
                    + "\n型号：" + mesItem.getModel() + "\n物料名称：" + mesItem.getName() + "\n客户：" + mesItem.getCustom() +
                    "\n供应商：" + mesItem.getSuppliers() + "\n物料编码：" + mesItem.getCode()));
        }
        new TipView(map, "MES明细").start();
    }

    /**
     * 点击已录入数按钮时执行的操作
     *
     * @param actionEvent
     */
    private void onCLickOkItem(ActionEvent actionEvent) {
        Map<String, ItemClickListener> map = new HashMap<>();
        final Config config = new Config(Main.IgnoreFile);
        String modelKey = task.S1500 + " / " + task.Model;
        for (CrucialItem item : inspectionl.getOkItem()) {
            String itemKey = modelKey + " " + item.getName();
            map.put(item.getName() + (config.getBoolean(itemKey) ? "(已忽略)" : ""), view -> {
                if (config.getBoolean(itemKey)) {
                    int confirm = JOptionPane.showConfirmDialog(null, "您想要取消忽略这个物料的覆盖率检查吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (!Main.isIgnore()) {
                        JOptionPane.showMessageDialog(null, "您没有忽略物料检验的权限！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (confirm == 0) {
                        config.set(itemKey, false).save();
                        JOptionPane.showMessageDialog(null, "操作完成！已成功取消F忽略物料: " + item.getName());
                        Log.i("已执行取消忽略操作！型号：" + task.Model + "] 订单号：[" + task.S1500 + "] 物料名称：[" + item.getName() + "]");
                    }
                } else {
                    int confirm = JOptionPane.showConfirmDialog(null, "您想要忽略这个物料的覆盖率检查吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (!Main.isIgnore()) {
                        JOptionPane.showMessageDialog(null, "您没有忽略物料检验的权限！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (confirm == 0) {
                        config.set(itemKey, true).save();
                        Log.i("已执行忽略操作！型号：" + task.Model + "] 订单号：[" + task.S1500 + "] 物料名称：[" + item.getName() + "]");
                        JOptionPane.showMessageDialog(null, "操作完成！已成功忽略物料: " + item.getName());
                    }
                }
            });
        }
        new TipView(map, modelKey + "  已检物料明细").start();
    }

    /**
     * 当点击未录入数的按钮时执行的操作
     *
     * @param actionEvent
     */
    private void onClickNGItem(ActionEvent actionEvent) {
        Map<String, ItemClickListener> map = new HashMap<>();
        final Config config = new Config(Main.IgnoreFile);
        String modelKey = task.S1500 + " / " + task.Model;
        for (CrucialItem item : inspectionl.getNgItem()) {
            String itemKey = modelKey + " " + item.getName();
            map.put(item.getName() + (config.getBoolean(itemKey) ? "(已忽略)" : ""), view -> {
                if (config.getBoolean(itemKey)) {
                    int confirm = JOptionPane.showConfirmDialog(null, "您想要取消忽略这个物料的覆盖率检查吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (!Main.isIgnore()) {
                        JOptionPane.showMessageDialog(null, "您没有忽略物料检验的权限！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (confirm == 0) {
                        config.set(itemKey, false).save();
                        JOptionPane.showMessageDialog(null, "操作完成！已成功取消F忽略物料: " + item.getName());
                        Log.i("已执行取消忽略操作！型号：" + task.Model + "] 订单号：[" + task.S1500 + "] 物料名称：[" + item.getName() + "]");
                    }
                } else {
                    int confirm = JOptionPane.showConfirmDialog(null, "您想要忽略这个物料的覆盖率检查吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (!Main.isIgnore()) {
                        JOptionPane.showMessageDialog(null, "您没有忽略物料检验的权限！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (confirm == 0) {
                        config.set(itemKey, true).save();
                        Log.i("已执行忽略操作！型号：" + task.Model + "] 订单号：[" + task.S1500 + "] 物料名称：[" + item.getName() + "]");
                        JOptionPane.showMessageDialog(null, "操作完成！已成功忽略物料: " + item.getName());
                    }
                }
            });
        }
        new TipView(map, modelKey + "  未检物料明细").start();
    }

    /**
     * @return 返回检验结果封装类
     */
    public Inspectionl getInspectionl() {
        return inspectionl;
    }

    @Override
    public void start() {
        int width = (int) (getWidth() / 3.5);
        S1500.setSize(width, getHeight());
        Model.setSize((getWidth() - S1500.getWidth()) / 3, S1500.getHeight());
        Model.setLocation(S1500.getX() + S1500.getWidth(), 0);
        OkCount.setSize((int) ((getWidth() - S1500.getWidth() - Model.getWidth()) / 4.3), getHeight());
        OkCount.setLocation(Model.getX() + Model.getWidth(), 0);
        NgCount.setSize(OkCount.getSize());
        NgCount.setLocation(OkCount.getX() + OkCount.getWidth(), 0);
        AllCount.setSize(NgCount.getSize());
        AllCount.setLocation(NgCount.getX() + NgCount.getWidth(), 0);
        perature.setLocation(AllCount.getWidth() + AllCount.getX(), 0);
        perature.setSize(getWidth() - perature.getX(), AllCount.getHeight());
        itemData = new ItemData();
        itemData.okSize = OkCount.getSize();
        itemData.ngSize = NgCount.getSize();
        itemData.allSize = AllCount.getSize();
        itemData.perSize = perature.getSize();
        itemData.okX = OkCount.getX();
        itemData.ngX = NgCount.getX();
        itemData.allX = AllCount.getX();
        itemData.perX = perature.getX();
    }

    public static class ItemData {
        public Dimension okSize, ngSize, allSize, perSize;
        public int okX, ngX, allX, perX;

        @Override
        public String toString() {
            return "ItemData{" +
                    "okSize=" + okSize +
                    ", ngSize=" + ngSize +
                    ", allSize=" + allSize +
                    ", perSize=" + perSize +
                    ", okX=" + okX +
                    ", ngX=" + ngX +
                    ", allX=" + allX +
                    ", perX=" + perX +
                    '}';
        }
    }
}
