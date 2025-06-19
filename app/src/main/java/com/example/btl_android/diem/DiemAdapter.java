package com.example.btl_android.diem;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;

import java.util.List;

/**
 * @noinspection ALL
 */
public class DiemAdapter extends RecyclerView.Adapter<DiemAdapter.ViewHolder> {
    private List<Diem> data;
    private OnItemClickListener listener;
    private Intent intent;

    public DiemAdapter(List<Diem> data, OnItemClickListener listener, Intent intent) {
        this.data = data;
        this.listener = listener;
        this.intent = intent;
    }

    @NonNull
    @Override
    public DiemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customrv_diem_hoc_phan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        Diem item = data.get(pos);
        holder.tvTenHp.setText(item.getTenHp());
        holder.tvTx1.setText(item.getTx1() == null ? "-" : String.format("%.1f", item.getTx1()));
        holder.tvTx2.setText(item.getTx2() == null ? "-" : String.format("%.1f", item.getTx2()));
        holder.tvGiuaKy.setText(item.getGiuaKy() == null ? "-" : String.format("%.1f", item.getGiuaKy()));
        holder.tvCuoiKy.setText(item.getCuoiKy() == null ? "-" : String.format("%.1f", item.getCuoiKy()));
        holder.tvTongKet.setText(item.getDiemChu());
        holder.tvLoai.setText(item.getLoai() == 0 ? "Tự chọn" : "Bắt buộc");
        holder.diemHp.setTag(pos);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout diemHp;
        public LinearLayout rvDiemHp;
        public TextView tvTenHp, btnCapNhat, tvTx1, tvTx2, tvGiuaKy, tvCuoiKy, tvTongKet, tvLoai;

        public ViewHolder(View view) {
            super(view);
            tvTenHp = view.findViewById(R.id.tvTenHp);
            btnCapNhat = view.findViewById(R.id.btnCapNhat);
            tvTx1 = view.findViewById(R.id.tvTx1);
            tvTx2 = view.findViewById(R.id.tvTx2);
            tvGiuaKy = view.findViewById(R.id.tvGiuaKy);
            tvCuoiKy = view.findViewById(R.id.tvCuoiKy);
            tvTongKet = view.findViewById(R.id.tvTongKet);
            tvLoai = view.findViewById(R.id.tvLoai);
            diemHp = view.findViewById(R.id.diemHp);

            diemHp.setOnClickListener(this);
            btnCapNhat.setOnClickListener(v -> {
                Diem diem = data.get((int) view.getTag());
                Intent capNhatDiemIntent = new Intent((Context) listener, CapNhatDiemActivity.class);
                capNhatDiemIntent.putExtra("Diem", diem);
                capNhatDiemIntent.putExtra("MaSv", intent.getStringExtra("MaSv"));
                startActivityForResult((Activity) listener, capNhatDiemIntent, 1, null);
            });
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                int pos = (int) view.getTag();
                listener.onItemClick(pos);
            }
        }
    }
}
