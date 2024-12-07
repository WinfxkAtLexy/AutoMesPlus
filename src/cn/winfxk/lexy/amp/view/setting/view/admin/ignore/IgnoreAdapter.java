package cn.winfxk.lexy.amp.view.setting.view.admin.ignore;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;
import cn.winfxk.lexy.amp.view.setting.view.admin.admin.ItemView;

import java.util.ArrayList;
import java.util.List;

public class IgnoreAdapter extends BaseAdapter {
    private final List<String> list = new ArrayList<>();

    public IgnoreAdapter() {
        super();
        reload();
    }

    @Override
    public int getSize() {
        return list.size();
    }

    public void reload() {
        list.clear();
        list.addAll(Main.getConfig().getList("管理员", new ArrayList<>()));
    }

    @Override
    public String getItem(int location) {
        return list.get(location);
    }

    @Override
    public ItemView getView(int location) {
        String s = getItem(location);
        return new ItemView(s, view1 -> {
            if (s == null || s.isEmpty()) return;
            if (JOptionPane.showConfirmDialog(null, "您想要删除该用户的权限吗？", "提示", JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION) != 0) return;
            if (s.equals(Main.Username) || s.equals(Main.PCName)) {
                JOptionPane.showMessageDialog(null, "您不能删除自己的权限！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<String> list = Main.getConfig().getList("忽略权限", new ArrayList<>());
            list.remove(s);
            Main.getConfig().set("忽略权限", list).save();
            Log.i("已删除" + s + "的忽略权限。");
            JOptionPane.showMessageDialog(null, "操作成功！！", "提示", JOptionPane.PLAIN_MESSAGE);
            reload();
            UpdateAdapter();
        });
    }
}
