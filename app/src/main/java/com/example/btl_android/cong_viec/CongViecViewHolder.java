package com.example.btl_android.cong_viec;

import android.view.ContextMenu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.btl_android.R;

public class CongViecViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener {
    TextView tt, chitiet, uutien, gio, ngay;
    CheckBox trangthai;
    LongClickListener longClickListener;

    public CongViecViewHolder(View view) {
        tt = view.findViewById(R.id.tv_tenviec);
        chitiet = view.findViewById(R.id.tv_chitietcv);
        uutien = view.findViewById(R.id.tv_mucuutien);
        gio = view.findViewById(R.id.tv_hancvgio);
        ngay = view.findViewById(R.id.tv_hancvngay);
        trangthai = view.findViewById(R.id.cb_trangthai);
        view.setOnLongClickListener(this);
        view.setOnCreateContextMenuListener(this);
    }

    public void setLongClickListener(LongClickListener lc) {
        this.longClickListener = lc;
    }

    @Override
    public boolean onLongClick(View view) {
        this.longClickListener.onItemLongClick();
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

    }
}
