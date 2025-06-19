package com.example.btl_android.diem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android.R;

/**
 * @noinspection ALL
 */
public class DiemChiTietActivity extends AppCompatActivity {
    private ImageButton btnQuayLai;
    private TextView tvTenHp, tvMaHp, tvSoTc, tvHinhThucThi, tvLop, tvHeSo,
            tvTx1, tvTx2, tvGiuaKy, tvCuoiKy, tvKiVong, tvDiem10, tvDiem4, tvDiemChu,
            tvXepLoai, tvNghiLt, tvNghiTh, tvDieuKien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diem_chi_tiet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWidget();
        displayDiemChiTiet();
        btnQuayLai.setOnClickListener(v -> finish());
    }

    private void getWidget() {
        btnQuayLai = findViewById(R.id.imgQuayLai);
        tvTenHp = findViewById(R.id.tvTenHp);
        tvMaHp = findViewById(R.id.tvMaHp);
        tvSoTc = findViewById(R.id.tvSoTc);
        tvHinhThucThi = findViewById(R.id.tvHinhThucThi);
        tvLop = findViewById(R.id.tvLop);
        tvHeSo = findViewById(R.id.tvHeSo);
        tvTx1 = findViewById(R.id.tvTx1);
        tvTx2 = findViewById(R.id.tvTx2);
        tvGiuaKy = findViewById(R.id.tvGiuaKy);
        tvCuoiKy = findViewById(R.id.tvCuoiKy);
        tvKiVong = findViewById(R.id.tvKiVong);
        tvDiem10 = findViewById(R.id.tvDiem10);
        tvDiem4 = findViewById(R.id.tvDiem4);
        tvDiemChu = findViewById(R.id.tvDiemChu);
        tvXepLoai = findViewById(R.id.tvXepLoai);
        tvNghiLt = findViewById(R.id.tvNghiLt);
        tvNghiTh = findViewById(R.id.tvNghiTh);
        tvDieuKien = findViewById(R.id.tvDieuKien);
    }

    private void displayDiemChiTiet() {
        Intent intent = getIntent();
        Diem diem = (Diem) intent.getSerializableExtra("DiemChiTiet");
        tvTenHp.setText(diem.getTenHp());
        tvMaHp.setText(diem.getMaHp());
        tvSoTc.setText(String.valueOf(diem.getSoTinChiLt() + diem.getSoTinChiTh()));
        tvHinhThucThi.setText(diem.getHinhThucThi());
        tvLop.setText(diem.getMaLop());

        String[] heSo = diem.getHeSo().split("-");
        if (heSo.length <= 3) {
            tvHeSo.setText(heSo[0] + "(TX1)-" + heSo[1] + "(TX2)-" + heSo[2] + "(CK)");
        } else
            tvHeSo.setText(heSo[0] + "(TX1)-" + heSo[1] + "(TX2)-" + heSo[2] + "(GK)-" + heSo[3] + "(CK)");

        tvTx1.setText(diem.getTx1() != null ? String.format("%.1f", diem.getTx1()) : "-");
        tvTx2.setText(diem.getTx2() != null ? String.format("%.1f", diem.getTx2()) : "-");
        tvGiuaKy.setText(diem.getGiuaKy() != null ? String.format("%.1f", diem.getGiuaKy()) : "-");
        tvCuoiKy.setText(diem.getCuoiKy() != null ? String.format("%.1f", diem.getCuoiKy()) : "-");
        tvKiVong.setText(diem.getDiemKiVong() != null ? String.format("%.1f", diem.getDiemKiVong()) : "-");
        tvDiem10.setText(diem.getDiem10() != null ? String.format("%.1f", diem.getDiem10()) : "-");
        tvDiem4.setText(diem.getDiem4() != null ? String.format("%.1f", diem.getDiem4()) : "-");
        tvDiemChu.setText(diem.getDiemChu() != null ? diem.getDiemChu() : "-");
        tvXepLoai.setText(diem.getXepLoai());
        tvNghiLt.setText(String.valueOf(diem.getVangLt() * 2));
        tvNghiTh.setText(String.valueOf(diem.getVangTh() * 3));
        tvDieuKien.setText(diem.getDieuKien());
    }
}