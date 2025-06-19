package com.example.btl_android.thong_bao;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;

import java.util.List;

/**
 * @noinspection ALL
 */
public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder> {
    private List<ThongBao> data;

    public ThongBaoAdapter(List<ThongBao> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ThongBaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customrv_thong_bao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        ThongBao item = data.get(pos);
        holder.tvTieuDe.setText(item.getTieuDe());
        holder.tvNoiDung.setText(item.getNoiDung());
        holder.tvThoiGian.setText(item.getThoiGian());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTieuDe, tvNoiDung, tvThoiGian;

        public ViewHolder(View view) {
            super(view);
            tvTieuDe = view.findViewById(R.id.tvTieuDe);
            tvNoiDung = view.findViewById(R.id.tvNoiDung);
            tvThoiGian = view.findViewById(R.id.tvThoiGian);
        }
    }
}
