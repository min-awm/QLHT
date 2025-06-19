package com.example.btl_android.cong_viec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import java.util.List;

/**
 * @noinspection ALL
 */
public class CongViecAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final List<CongViec> congvieclist;

    public CongViecAdapter(final Context context, final int layout, final List<CongViec> congvieclist) {
        this.context = context;
        this.layout = layout;
        this.congvieclist = congvieclist;
    }

    @Override
    public int getCount() {
        return this.congvieclist.size();
    }

    @Override
    public Object getItem(final int i) {
        return null;
    }

    @Override
    public long getItemId(final int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup viewGroup) {
        final LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(this.layout, null);

        final CongViec congViecx = this.congvieclist.get(i);
        CongViecViewHolder holder = new CongViecViewHolder(convertView);
        holder.tt.setText(congViecx.getTenCongViec());
        holder.chitiet.setText(congViecx.getChiTietCongViec());
        if (congViecx.getMucUuTien().equals("1")) {
            holder.uutien.setText("Không quan trọng");
        }
        if (congViecx.getMucUuTien().equals("2")) {
            holder.uutien.setText("Quan trọng");
        }
        if (congViecx.getMucUuTien().equals("3")) {
            holder.uutien.setText("Rất quan trọng");
        }
        holder.gio.setText(congViecx.getThoiHanGio());
        holder.ngay.setText(congViecx.getThoiHanNgay());
        if (congViecx.trangThai == 1) {
            holder.trangthai.setChecked(true);
        }
        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick() {
//                Toast.makeText(context, ""+i, Toast.LENGTH_SHORT).show();
                ((CongViecActivity) context).setSelectedItemPosition(i);
            }
        });
        holder.trangthai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Toast.makeText(context, b+" "+congViecx.tenCongViec, Toast.LENGTH_SHORT).show();
                ((CongViecActivity) context).savetrangthai(congViecx, b);
            }
        });
        return convertView;
    }
}