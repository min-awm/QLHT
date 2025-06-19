package com.example.btl_android.lich_hoc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;

import java.util.ArrayList;

/**
 * @noinspection ALL
 */
public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> tb_mon, tb_thu, tb_ngay, tb_giangvien, tb_phong, tb_tiet, tb_diadiem;
    private ArrayList<Integer> tb_id;

    TimeTableAdapter(Context context,
                     ArrayList<Integer> tb_id,
                     ArrayList<String> tb_mon,
                     ArrayList<String> tb_thu,
                     ArrayList<String> tb_ngay,
                     ArrayList<String> tb_giangvien,
                     ArrayList<String> tb_phong,
                     ArrayList<String> tb_tiet,
                     ArrayList<String> tb_diadiem) {
        this.context = context;
        this.tb_id = tb_id;
        this.tb_mon = tb_mon;
        this.tb_thu = tb_thu;
        this.tb_ngay = tb_ngay;
        this.tb_giangvien = tb_giangvien;
        this.tb_phong = tb_phong;
        this.tb_tiet = tb_tiet;
        this.tb_diadiem = tb_diadiem;
    }

    @NonNull
    @Override
    public TimeTableAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_timetable, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableAdapter.MyViewHolder holder, int position) {

        holder.tb_mon_txt.setText(tb_mon.get(position));
        holder.tb_thu_txt.setText(tb_thu.get(position));
        holder.tb_ngay_txt.setText(tb_ngay.get(position));
        holder.tb_giangvien_txt.setText(tb_giangvien.get(position));
        holder.tb_phong_txt.setText(tb_phong.get(position));
        holder.tb_tiet_txt.setText(tb_tiet.get(position));
        holder.tb_diadiem_txt.setText(tb_diadiem.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Update_Timetable.class);
                int id = tb_id.get(holder.getAbsoluteAdapterPosition());
                intent.putExtra("id", id);
                intent.putExtra("mon", tb_mon.get(holder.getAbsoluteAdapterPosition()));
                intent.putExtra("thu", tb_thu.get(holder.getAbsoluteAdapterPosition()));
                intent.putExtra("ngay", tb_ngay.get(holder.getAbsoluteAdapterPosition()));
                intent.putExtra("giangvien", tb_giangvien.get(holder.getAbsoluteAdapterPosition()));
                intent.putExtra("phong", tb_phong.get(holder.getAbsoluteAdapterPosition()));
                intent.putExtra("tiet", tb_tiet.get(holder.getAbsoluteAdapterPosition()));
                intent.putExtra("diadiem", tb_diadiem.get(holder.getAbsoluteAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tb_mon.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tb_mon_txt, tb_thu_txt, tb_ngay_txt, tb_giangvien_txt, tb_phong_txt, tb_tiet_txt, tb_diadiem_txt;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tb_mon_txt = itemView.findViewById(R.id.txtSubject);
            tb_thu_txt = itemView.findViewById(R.id.txtDay);
            tb_ngay_txt = itemView.findViewById(R.id.txtDate);
            tb_giangvien_txt = itemView.findViewById(R.id.txtTeacher);
            tb_phong_txt = itemView.findViewById(R.id.txtRoom);
            tb_tiet_txt = itemView.findViewById(R.id.txtTime);
            tb_diadiem_txt = itemView.findViewById(R.id.txtLocation);
            mainLayout = itemView.findViewById(R.id.mainlayout);
        }
    }
}