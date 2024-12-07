package cn.winfxk.lexy.amp.view.main.view.model;

import cn.winfxk.lexy.amp.crucial.CrucialItem;
import cn.winfxk.lexy.amp.crucial.Inspectionl;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;
import cn.winfxk.lexy.amp.view.main.view.model.item.NGItemView;

public class NgItemAdapter extends BaseAdapter {
    private Inspectionl inspectionl;

    @Override
    public int getSize() {
        return inspectionl == null ? 0 : inspectionl.getNgItem().size() + 1;
    }

    @Override
    public CrucialItem getItem(int location) {
        return location == 0 ? null : inspectionl.getNgItem().get(location - 1);
    }

    @Override
    public NGItemView getView(int location) {
        return new NGItemView(getItem(location));
    }

    public synchronized void UpdateAdapter(Inspectionl inspectionl) {
        this.inspectionl = inspectionl;
        super.UpdateAdapter();
    }
}
