package com.example.btl_android.dang_nhap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.btl_android.R;

import java.util.ArrayList;

public class MiniTimeTableAdapter extends BaseAdapter {
    private final Context context;
    private final int layout;
    private final ArrayList<MiniTimeTable> lichhoclist;

    public MiniTimeTableAdapter(final Context context, final int layout, final ArrayList<MiniTimeTable> lichhoclist) {
        this.context = context;
        this.layout = layout;
        this.lichhoclist = lichhoclist;
    }

    @Override
    public int getCount() {
        return this.lichhoclist.size();
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
        final MiniTimeTable lichhoc = this.lichhoclist.get(i);
        final TextView tenmonhoc = view.findViewById(R.id.tv_minitenmonhoc);
        final TextView tiethoc = view.findViewById(R.id.tv_minitiethoc);
        final TextView diadiem = view.findViewById(R.id.tv_minidiadiem);

        tenmonhoc.setText(lichhoc.getTenMonHoc());
        tiethoc.setText(lichhoc.getTietHoc());
        diadiem.setText(lichhoc.getDiaDiem());
        return view;
    }
}
