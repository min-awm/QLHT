package com.example.btl_android.dang_nhap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.btl_android.R;
import com.example.btl_android.cong_viec.CongViec;

import java.util.ArrayList;

public class MiniCongViecAdapter extends BaseAdapter {
    private final Context context;
    private final int layout;
    private final ArrayList<CongViec> congvieclist;

    public MiniCongViecAdapter(final Context context, final int layout, final ArrayList<CongViec> congvieclist) {
        this.context = context;
        this.layout = layout;
        this.congvieclist = congvieclist;
    }

    @Override
    public int getCount() {
        return this.congvieclist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(this.layout, null);
        final CongViec congViecx = this.congvieclist.get(i);
        final TextView tt = view.findViewById(R.id.tv_minitenviec);
        final TextView gio = view.findViewById(R.id.tv_minihangio);
        final TextView ngay = view.findViewById(R.id.tv_minihanngay);
        final CheckBox trangthai = view.findViewById(R.id.cb_minitrangthai);

        tt.setText(congViecx.getTenCongViec());
        gio.setText(congViecx.getThoiHanGio());
        ngay.setText(congViecx.getThoiHanNgay());
        trangthai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ((TrangChuActivity) context).saveTrangThai(congViecx, b);
            }
        });
        return view;
    }
}
