package cn.winfxk.lexy.amp.view.main.view.model;

import cn.winfxk.lexy.amp.crucial.CrucialItem;
import cn.winfxk.lexy.amp.crucial.Inspectionl;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;
import cn.winfxk.lexy.amp.view.main.view.model.item.OKItemView;

public class OkItemAdapter extends BaseAdapter {
    private Inspectionl inspectionl;

    @Override
    public int getSize() {
        return inspectionl == null ? 0 : inspectionl.getOkItem().size() + 1;
    }

    @Override
    public CrucialItem getItem(int location) {
        return location == 0 ? null : inspectionl.getOkItem().get(location - 1);
    }

    @Override
    public OKItemView getView(int location) {
        return new OKItemView(getItem(location));
    }

    public synchronized void UpdateAdapter(Inspectionl inspectionl) {
        this.inspectionl = inspectionl;
        super.UpdateAdapter();
    }
}
