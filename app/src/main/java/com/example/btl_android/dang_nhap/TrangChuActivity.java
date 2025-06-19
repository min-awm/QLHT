package com.example.btl_android.dang_nhap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;
import com.example.btl_android.cong_viec.CongViec;
import com.example.btl_android.cong_viec.CongViecActivity;
import com.example.btl_android.diem.DiemActivity;
import com.example.btl_android.hoc_phan_du_kien.HocPhanDuKienActivity;
import com.example.btl_android.lich_hoc.TimeTable;
import com.example.btl_android.thong_bao.ThongBaoActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @noinspection ALL
 */
public class TrangChuActivity extends AppCompatActivity {

    LinearLayout btnCongViec, btnHocPhan, btnDiem, btnLichHoc;
    ImageView btnThongBao;
    BroadcastReceiver receiver;
    ListView lvCongViec;
    ArrayList<CongViec> congViecList = new ArrayList<>();
    ListView lvLichHoc;
    ArrayList<MiniTimeTable> lichHocList = new ArrayList<>();
    TextView txtUserName;
    MiniCongViecAdapter cvAdapter;
    MiniTimeTableAdapter miniTimeTableAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_trang_chu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.trangChuActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCongViec = findViewById(R.id.btnCongViec);
        btnHocPhan = findViewById(R.id.btnHocPhan);
        btnDiem = findViewById(R.id.btnDiem);
        btnThongBao = findViewById(R.id.imgThongBao);
        btnLichHoc = findViewById(R.id.btnLichHoc);
        txtUserName = findViewById(R.id.txtUsername);
        txtUserName.setText(getIntent().getStringExtra("TenSv"));

        dbHelper = new DatabaseHelper(this);

        lichHocList = dbHelper.getLichHocLite("2024-06-19");
        showlvLichHoc();

        congViecList = dbHelper.getAllCongViec(getIntent().getStringExtra("MaSv"));
        softCongViecList(congViecList);
        showlvCongViec();

        btnCongViec.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, CongViecActivity.class);
            intent.putExtra("MaSv", getIntent().getStringExtra("MaSv"));
            intent.putExtra("TenSv", getIntent().getStringExtra("TenSv"));
            startActivity(intent);
        });

        btnDiem.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, DiemActivity.class);
            intent.putExtra("MaSv", getIntent().getStringExtra("MaSv"));
            startActivityForResult(intent, 0);
        });

        btnHocPhan.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, HocPhanDuKienActivity.class);
            startActivity(intent);
        });

        btnLichHoc.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, TimeTable.class);
            startActivity(intent);
        });

        btnThongBao.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, ThongBaoActivity.class);
            intent.putExtra("MaSv", getIntent().getStringExtra("MaSv"));
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            btnThongBao.setImageResource(R.drawable.bell_new_update);
        }
        if (requestCode == 1 && resultCode == 1) {
            btnThongBao.setImageResource(R.drawable.bell);
        }
    }

    void showlvCongViec() {
        Context x = this;
        lvCongViec = findViewById(R.id.lv_minicongviec);
        cvAdapter = new MiniCongViecAdapter(x, R.layout.customlv_cong_viec_can_lam, congViecList);
        lvCongViec.setAdapter(cvAdapter);
    }

    void showlvLichHoc() {
        Context x = this;
        lvLichHoc = findViewById(R.id.lv_minilichhoc);
        miniTimeTableAdapter = new MiniTimeTableAdapter(x, R.layout.customlv_lich_hoc_hom_nay, lichHocList);
        lvLichHoc.setAdapter(miniTimeTableAdapter);
    }

    public void softCongViecList(ArrayList<CongViec> congViecList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        ArrayList<CongViec> trangThai1List = new ArrayList<>();
        ArrayList<CongViec> trangThai0List = new ArrayList<>();

        for (CongViec cv : congViecList) {
            if (cv.getTrangThai() == 1) {
                trangThai1List.add(cv);
            } else {
                trangThai0List.add(cv);
            }
        }
        for (int i = 0; i < trangThai0List.size() - 1; i++) {
            for (int j = i + 1; j < trangThai0List.size(); j++) {
                CongViec cv1 = trangThai0List.get(i);
                CongViec cv2 = trangThai0List.get(j);
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = dateFormat.parse(cv1.getThoiHanNgay() + " " + cv1.getThoiHanGio());
                    date2 = dateFormat.parse(cv2.getThoiHanNgay() + " " + cv2.getThoiHanGio());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date1 != null && date2 != null) {
                    if (date1.after(date2) || (date1.equals(date2) && Integer.parseInt(cv1.getMucUuTien()) < Integer.parseInt(cv2.getMucUuTien()))) {
                        trangThai0List.set(i, cv2);
                        trangThai0List.set(j, cv1);
                    }
                }
            }
        }
        congViecList.clear();
        congViecList.addAll(trangThai0List);
    }

    void saveTrangThai(CongViec congViec, boolean b) {
        int stt = congViecList.indexOf(congViec);
        if (b) {
            congViec.setTrangThai(1);
            Toast.makeText(this, "Hoàn thành " + congViec.getTenCongViec(), Toast.LENGTH_SHORT).show();
        } else {
            congViec.setTrangThai(0);
        }
        dbHelper.updateCongViec(congViec);
        congViecList.set(stt, congViec);
        softCongViecList(congViecList);
        cvAdapter.notifyDataSetChanged();
    }
}